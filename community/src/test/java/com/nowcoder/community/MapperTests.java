package com.nowcoder.community;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.yaml.snakeyaml.external.com.google.gdata.util.common.base.UnicodeEscaper;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
// 运行的类名称
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {
    @Autowired
    private  UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println(user);
        user = userMapper.selectByName("niuke");
        System.out.println(user);
        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }
    @Test
    public void testInsertUser() {
        User user = new User();
        user.setEmail("test@qq.com");
        user.setSalt("abc");
        user.setPassword("123456");
        user.setUsername("test");
        user.setStatus(0);
        user.setType(1);
        user.setActivationCode(null);
        user.setHeaderUrl("http://www.test.com");
        user.setCreateTime(new Date());
        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }
    @Test
    public void testUpdateUser() {
        int rows = userMapper.updateStatus(149, 1);
        System.out.println(rows);
        rows = userMapper.updateHeader(149, "http://images.nowcoder.com/head/149s.png");
        System.out.println(rows);
        rows = userMapper.updatePassword(149, "abce124c4ba2de3be92dc9a3bc1ea75b");
        System.out.println(rows);
    }
    @Test
    public void testSelectPosts() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 10, 0);
        for (DiscussPost discussPost : list)  {
            System.out.println(discussPost);
        }
        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }
    @Test
    public void testInsertLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testSelectLoginTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
        loginTicketMapper.updateStatus("abc", 1);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
    }
    @Test
    public void testInsertDiscussPost() {
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(101);
        discussPost.setStatus(0);
        discussPost.setType(0);
        discussPost.setContent("test11111");
        discussPost.setCreateTime(new Date());
        discussPost.setScore(0.0);
        discussPost.setCommentCount(0);
        discussPost.setTitle("ctmd");
        System.out.println(discussPostService.addDiscussPost(discussPost));
    }

    @Test
    public void testSelectLetters() {
        List<Message> list = messageMapper.selectConversations(111, 0, 20);
        for (Message message : list) {
            System.out.println(message);
        }

        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        list = messageMapper.selectLetters("111_112", 0, 10);
        for (Message message : list) {
            System.out.println(message);
        }

        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);

        count = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(count);

    }
}
