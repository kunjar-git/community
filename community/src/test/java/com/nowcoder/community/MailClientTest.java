package com.nowcoder.community;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.MailClientUtil;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailClientTest {
    @Autowired
    MailClientUtil mailClientUtil;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    UserService userService;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Test
    public void testSendEmail() {
        mailClientUtil.sendMail("kunjar518@163.com","test", "hi,kun~");
    }
    @Test
    public void testHtmlEmail() {
        Context context = new Context();
        context.setVariable("username", "Sunday");
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);
        mailClientUtil.sendMail("kunjar518@163.com","HTML", content);
    }
    @Test
    public void testHtmlEmail2() {
        Context context = new Context();
        context.setVariable("email", "kunjar518@163.com");
        // http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/" + "activation/" + "101" + "/" + "234";
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        System.out.println(content);
        mailClientUtil.sendMail("kunjar518@163.com","HTML", content);
    }
    @Test
    public void testHtmlEmail3() {
        User user = new User();
        user.setUsername("kunjartest");
        user.setPassword("123");
        user.setEmail("kunjar518@163.com");
        Map<String, Object> register = userService.register(user);
        System.out.println(register);
    }
}
