package com.nowcoder.community;

import com.nowcoder.community.util.MailClientUtil;
import org.junit.Test;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailClientTest {
    @Autowired
    MailClientUtil mailClientUtil;
    @Autowired
    private TemplateEngine templateEngine;
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
}
