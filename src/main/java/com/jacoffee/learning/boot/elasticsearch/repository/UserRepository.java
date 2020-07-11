package com.jacoffee.learning.boot.elasticsearch.repository;

import com.jacoffee.learning.boot.model.UserDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * UserRepository
 * @author jacoffee
 * @date 2020-07-05
 */
public interface UserRepository extends
    ElasticsearchRepository<UserDocument, String>, CustomizedRepository<UserDocument> {
}
