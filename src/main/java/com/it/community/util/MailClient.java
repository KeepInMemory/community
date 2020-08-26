package com.it.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

//将发邮件委托给SMTP服务器: smtp.163.com去做
@Component
public class MailClient {
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    //核心组件是JavaMailSender,直接注入就可以用
    //通过构建MimeMessage对象,调用send方法发出MimeMessage
    @Autowired
    private JavaMailSender javaMailSender;

    //发邮件的用户,application.properties中配置的
    @Value("${spring.mail.username}")
    private String from;

    /*
    * to:收件人
    * subject:邮件标题
    * content:邮件内容
    * */
    public void sendMail(String to,String subject,String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            //html : true表示支持html文本
            helper.setText(content,true);

            javaMailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败" + e.getMessage());
        }
    }
}
