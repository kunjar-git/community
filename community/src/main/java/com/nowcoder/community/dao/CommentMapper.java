package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    // 查询某实体获得的所有评论
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    // 查询某用户的所有回复
    List<Comment> selectCommentsByUserId(int userId, int entityType, int offset, int limit);

    int selectCommentsCountByUserId (int userId, int entityType);
    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

    Comment selectCommentById(int id);

}