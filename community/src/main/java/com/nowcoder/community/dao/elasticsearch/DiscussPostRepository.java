package com.nowcoder.community.dao.elasticsearch;

import com.nowcoder.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
// Integer: 主键类型
// 增删改查自动实现好了（By Spring）
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {

}
