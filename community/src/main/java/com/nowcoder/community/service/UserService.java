package com.nowcoder.community.service;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClientUtil;
import com.nowcoder.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;

//@SuppressWarnings(value = "all")
@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private MailClientUtil mailClientUtil;

    @Autowired
    RedisTemplate redisTemplate;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
//    @Autowired
//    private LoginTicketMapper loginTicketMapper;
    public User findUserById(int id) {
//        return userMapper.selectById(id);
        User user = getCache(id);
        if (user == null) {
            user = initCache(id);
        }
        return user;
    }

    public Map<String, Object> register(User user) {

        Map<String, Object> map = new HashMap<>();
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        if (StringUtils.isBlank(username)) {
            map.put("username","账号不能为空！");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("password","密码不能为空！");
            return map;
        }
        if (StringUtils.isBlank(email)) {
            map.put("email","邮箱不能为空！");
            return map;
        }
        // 验证账号
        User u = userMapper.selectByName(username);
        if (u != null) {
            map.put("usernameMsg","该账号已存在！");
            return map;
        }
        // 验证邮箱
        u = userMapper.selectByEmail(email);
        if (u != null) {
            map.put("emailMsg","该邮箱已被注册！");
            return map;
        }

        // 注册用户
        String salt = CommunityUtil.generateUUID().substring(0, 5);
        user.setSalt(salt);
        password = CommunityUtil.generateMD5(password + salt);
        user.setPassword(password);
        // 未激活
        user.setStatus(0);
        // 普通用户
        user.setType(0);
        // 激活码
        user.setActivationCode(CommunityUtil.generateUUID());
        // 设置默认头像
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        // 时间戳
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        // id和activationCode在插入之后才有，不是在最初从用户的表单中获取的，注意定义的顺序
        String id = user.getId() + "";
        String activationCode = user.getActivationCode();
        // 激活邮件
        Context context = new Context();
        context.setVariable("email", email);
        // http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/" + "activation/" + id + "/" + activationCode;
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClientUtil.sendMail(email,"激活账号", content);
        return map;
    }
    // 激活用户
    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user == null) return ACTIVATION_FAILURE;
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else {
            if (user.getActivationCode().equals(code)) {
                userMapper.updateStatus(userId, 1);
                clearCache(userId);
                return ACTIVATION_SUCCESS;
            } else {
                return ACTIVATION_FAILURE;
            }
        }
    }
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在!");
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活!");
            return map;
        }

        // 验证密码
        password = CommunityUtil.generateMD5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确!");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000L));
//        loginTicketMapper.insertLoginTicket(loginTicket);
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);
        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket) {
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
//        loginTicketMapper.updateStatus(ticket, 1);
    }


    public LoginTicket findLoginTicket(String ticket) {
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
//        return loginTicketMapper.selectByTicket(ticket);
    }

    public int updateHeader(int userId, String headerUrl) {
//        return userMapper.updateHeader(userId, headerUrl);
        int rows = userMapper.updateHeader(userId, headerUrl);
        clearCache(userId);
        return rows;
    }

    public int updatePassword(int userId, String password) {

//        return userMapper.updatePassword(id, password);
        int rows = userMapper.updatePassword(userId, password);
        clearCache(userId);
        return rows;
    }

    public User findUserByName(String name) {
        return userMapper.selectByName(name);
    }
    // 另一种方法，不删除，直接更新缓存，最后再更新数据库。可能会导致并发问题，没采用。
    // 1.优先从缓存中取值
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    // 2.取不到时初始化缓存数据
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    // 3.数据变更时清除缓存数据
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

    // 查询用户身份
    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);

        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 1:
                        return AUTHORITY_ADMIN;
                    case 2:
                        return AUTHORITY_MODERATOR;
                    default:
                        return AUTHORITY_USER;
                }
            }
        });
        return list;
    }
}
