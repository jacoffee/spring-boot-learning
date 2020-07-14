package com.jacoffee.learning.elasticsearch.repository;

import com.jacoffee.learning.elasticsearch.model.UserDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * UserRepository
 * @author jacoffee
 * @date 2020-07-05
 */
public interface UserRepository extends
    ElasticsearchRepository<UserDocument, String>, CustomizedRepository<UserDocument> {
}
