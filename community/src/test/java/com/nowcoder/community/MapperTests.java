package com.nowcoder.community;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
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
    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println(user);
        user = userMapper.selectByName("liubei");
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
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for (DiscussPost discussPost : list)  {
            System.out.println(discussPost);
        }
        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }
}
