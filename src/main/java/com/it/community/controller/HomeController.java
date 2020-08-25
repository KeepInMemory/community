package com.it.community.controller;

import com.it.community.entity.DiscussPost;
import com.it.community.entity.Page;
import com.it.community.entity.User;
import com.it.community.service.DiscussPostService;
import com.it.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
//Controller的访问路径可以省略
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private DiscussPostService discussPostService;

    /*
    这里查出来DiscussPost后,因为对象有一个userId作为外键,需要通过userMapper的selectById查出username
    不需要userId,需要username
    page是我们封装的分页工具
    */
    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page) {

        //方法调用之前会自动实例化model和page,并将page注入model
        //所以在thymeleaf中就可以直接访问到page对象的数据,不需要 model.addAttribute("page",page);

        //查出总行数
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPost(0, page.getOffset(), page.getLimit());
        List<Map<String,Object>>  discussPosts = new ArrayList<>();

        if(list != null) {
            for(DiscussPost discussPost: list) {
                Map<String,Object> map = new HashMap<>();

                map.put("post",discussPost);
                User user = userService.findUserById(discussPost.getUserId());
                map.put("user",user);
                System.out.println(user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        return "index";
    }
}
