package com.nowcoder.community.config;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.elasticsearch.DiscussPostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;

@Configuration
public class ElasticSearchConfig {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class);

    @Autowired
    private DiscussPostRepository discussRepository;

    @Autowired
    private DiscussPostMapper discussMapper;

    @PostConstruct
    public void init() {
        discussRepository.saveAll(discussMapper.selectDiscussPosts(101, 0, 100, 0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(102, 0, 100, 0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(103, 0, 100, 0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(111, 0, 100, 0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(112, 0, 100, 0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(131, 0, 100, 0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(132, 0, 100, 0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(133, 0, 100, 0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(134, 0, 100, 0));
        logger.info("初始化elastic数据库: ");

    }
}
