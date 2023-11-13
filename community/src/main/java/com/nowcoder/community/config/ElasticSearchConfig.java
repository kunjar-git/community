package com.nowcoder.community.config;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.elasticsearch.DiscussPostRepository;
import com.nowcoder.community.service.DiscussPostService;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class ElasticSearchConfig {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class);

    @Autowired
    private DiscussPostRepository discussRepository;

    @Autowired
    private DiscussPostMapper discussMapper;

    @Autowired
    private DiscussPostService discussPostService;
    @PostConstruct
    public void init() {
        List<Integer> list = discussPostService.findUniqueUserIdCount();
        for (int i : list) {
            discussRepository.saveAll(discussMapper.selectDiscussPosts(i, 0, 100, 0));
        }

        logger.info("初始化elastic数据库: ");

    }
}
