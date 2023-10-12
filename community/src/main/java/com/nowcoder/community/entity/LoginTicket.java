package com.nowcoder.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginTicket {

    private int id;
    // 就是user的id
    private int userId;
    // 登陆凭证，UUID
    private String ticket;
    // 0: 有效，1: 失效
    private int status;
    // 过期时间
    private Date expired;
}
