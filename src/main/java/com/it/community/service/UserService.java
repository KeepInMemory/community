package com.it.community.service;

import com.it.community.dao.UserMapper;
import com.it.community.entity.User;
import com.it.community.util.CommunityConstant;
import com.it.community.util.CommunityUtil;
import com.it.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    //发邮件需要mailClient 和 模板引擎
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;

    //激活码需要域名
    //到application.properties中匹配对应的值
    @Value("${community.path.domain}")
    private String domain;
    //激活码需要项目名
    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    //返回内容包括密码不能为空,账户已存在,邮箱不能为空等,所以放到map里
    public Map<String,Object> register(User user) {
        Map<String,Object> map = new HashMap<>();

        //对空值判断
        if(user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        //帐号为空不是一个错误,而是业务上不允许的,所以返回map告诉前端这样是不正确的
        if(StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg","邮箱不能为空");
            return map;
        }

        //验证帐号是否已存在
        //按username查因为前端传过来的对象没有id
        User u = userMapper.selectByName(user.getUsername());
        if(u != null) {
            map.put("usernameMsg","该帐号已存在");
            return map;
        }
        //验证邮箱是否存在
        u = userMapper.selectByEmail(user.getEmail());
        if(u != null) {
            map.put("emailMsg","该邮箱已被注册");
            return map;
        }

        //注册用户
        //生成的随机字符串很长,选择前五位
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        //重新覆盖用户密码
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        //普通用户
        user.setType(0);
        //未激活
        user.setStatus(0);
        //设置激活码
        user.setActivationCode(CommunityUtil.generateUUID());
        //设置默认的随机头像
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        //设置创建时间
        user.setCreateTime(new Date());

        userMapper.insertUser(user);

        //给用户发激活邮件
        //Context模板引擎上下文
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        // http://localhost:8080/community/activation/用户id/激活码
        //传入的user是没有id的,当insert后Mybatis自动生成了id
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);

        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(),"激活帐号",content);

        //如果返回map为空表示注册成功
        return map;
    }

    //点激活链接会传过来userId,激活码
    public int activation(int userId,String code) {
        User user = userMapper.selectById(userId);

        if(user.getStatus() == 1) {
            //表示已经激活
            return ACTIVATION_REPEAT;
        } else if(user.getActivationCode().equals(code)) {
            //表示激活码正确
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        } else {
            //激活码错误
            return ACTIVATION_FAILURE;
        }
    }
}
