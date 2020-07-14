package com.jacoffee.learning.elasticsearch.service.impl;

import com.jacoffee.learning.elasticsearch.config.ElasticsearchConfig;
import com.jacoffee.learning.elasticsearch.dto.Page;
import com.jacoffee.learning.elasticsearch.dto.PageList;
import com.jacoffee.learning.elasticsearch.dto.UserDTO;
import com.jacoffee.learning.elasticsearch.repository.UserRepository;
import com.jacoffee.learning.elasticsearch.model.UserDocument;
import com.jacoffee.learning.elasticsearch.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserServiceImpl
 * @author jacoffee
 * @date 2020-07-05
 */
@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private ElasticsearchConfig elasticsearchConfig;

    @Autowired
    private UserRepository userRepository;

    // 利用属性注解构建查询QueryBuilder
    private QueryBuilder buildFromUserDTO(UserDTO dto) throws IllegalAccessException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolean allFieldsEmpty = true;

        if (!StringUtils.isBlank(dto.getProjectId())) {
            allFieldsEmpty = false;
            boolQueryBuilder.filter(
                QueryBuilders.termQuery("project_id", dto.getProjectId())
            );
        }

        if (!StringUtils.isBlank(dto.getUid())) {
            allFieldsEmpty = false;
            boolQueryBuilder.filter(
                QueryBuilders.termQuery("uid", dto.getUid())
            );
        }

        if (!StringUtils.isBlank(dto.getName())) {
            allFieldsEmpty = false;
            boolQueryBuilder.filter(
                QueryBuilders.matchQuery("name", dto.getName())
            );
        }

        if (!StringUtils.isBlank(dto.getLastLoginTime())) {
            allFieldsEmpty = false;
            boolQueryBuilder.filter(
                QueryBuilders
                    .rangeQuery("last_login_time")
                    .gte(dto.getLastLoginTime())
                    .format("yyyy-MM-dd") // 使用yyyy-MM-dd对于底层的登录时间查询
            );
        }

        if (allFieldsEmpty) {
            return QueryBuilders.matchAllQuery();
        }

        return boolQueryBuilder;
    }

    @Override
    public PageList<UserDocument> searchPageList(UserDTO userDTO) throws Exception {
        Page page = userDTO.getPage();

        QueryBuilder queryBuilder = buildFromUserDTO(userDTO);
        // 注意需要-1，Elasticsearch中的from从0开始
        if (page.getPageNum() - 1 + page.getPageSize() > elasticsearchConfig.getMaxResultWindow()) {
            return userRepository.doSearchAfterPageQuery(queryBuilder, page, UserDocument.class);
        } else {
            return userRepository.doNormalPageQuery(queryBuilder, page, UserDocument.class);
        }
    }

}
