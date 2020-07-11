package com.jacoffee.learning.boot.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * UserDocument
 * @author jacoffee
 * @date 2020-07-05
 */
@Getter
@Setter
@Document(indexName = "user", type = "_doc", createIndex = false)
public class UserDocument extends ESDocument {

    @Id
    private String id;

    // name表示在es中的字段
    @Field(type = FieldType.Keyword, name = "project_id")
    private String projectId;

    @Field(type = FieldType.Keyword, name = "uid")
    private Integer uid;

    // name索引和搜索时，均使用ik_max_word分词器
    @Field(type = FieldType.Text, name = "name", analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.Date, name = "last_login_time")
    private Long lastLoginTime;

    public static String buildDocId(String project, String uid) {
        return String.format("%s_%s", project, uid);
    }

}
