package com.it.community;


import com.it.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {

    @Autowired
    private MailClient mailClient;
    //Spring管理的模板引擎
    @Autowired
    private TemplateEngine templateEngine;
    @Test
    public void testMail() {
        mailClient.sendMail("759593310@qq.com","Test","This is JavaMailSender test");
    }

    //测试的是mail/demo.html
    @Test
    public void testHtmlMail() {
        //模板引擎上下文
        Context context = new Context();
        context.setVariable("username","2020-08-26");

        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);
        mailClient.sendMail("759593310@qq.com","Html Mail",content);
    }
}
