package com.it.community.controller;

import com.it.community.entity.User;
import com.it.community.service.UserService;
import com.it.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }

    //前端传入用户帐号密码邮箱,可以通过三个参数去接收,
    // 也可以用User对象,会自动注入User对象,<input name=username >这里的name要和User.username对应就可以自动传入
    //Model用于存数据传递给模板
    //注意User也会自动注入到Model中
    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if(map == null || map.isEmpty()) {
            //表示注册成功
            model.addAttribute("msg","注册成功,我们已经向您的邮箱发送了一封激活邮件，请尽快激活");
            model.addAttribute("target","/index");
            return "site/operate-result";
        } else {
            //这里有三种情况,帐号重复 邮箱重复 帐号为空
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }
    }

    // http://localhost:8080/community/activation/用户id/激活码
    //用Get请求就行,因为不需要表单提交,在url上附带参数即可
    //本身相当于获取、查询行为,获取自己成功还是失败
    @RequestMapping(path = "/activation/{userId}/{code}" ,method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId,@PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if(result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg","激活成功,您的帐号已经可以正常使用了");
            model.addAttribute("target","/login");
        } else if(result == ACTIVATION_REPEAT) {
            model.addAttribute("msg","无效操作，该帐号已经激活过了");
            model.addAttribute("target","/index");
        } else {
            model.addAttribute("msg","激活失败，您提供激活码不正确");
            model.addAttribute("target","/index");
        }
        return "site/operate-result";
    }
}
