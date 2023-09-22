package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    // userId为了扩展“我的贴子数量”而定义，动态SQL
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);
    // @Param：给参数起别名
    // 如果需要动态拼条件且只有一个参数，那么必须用
    int selectDiscussPostRows(@Param("userId") int userId);
}
