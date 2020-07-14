package com.jacoffee.learning.elasticsearch.config;

import com.jacoffee.learning.elasticsearch.mapper.CustomResultMapper;
import lombok.Getter;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * ElasticsearchConfig
 * @author jacoffee
 * @date 2020-07-05
 */
@Getter
@Configuration
// 指定扫描repository路径
@EnableElasticsearchRepositories(basePackages = "com.jacoffee.learning.elasticsearch.repository")
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.nodes}")
    private String[] nodes;

    @Value("${elasticsearch.username}")
    private String restUserName;

    @Value("${elasticsearch.password}")
    private String restPassword;

    @Value("${elasticsearch.proxy:}")
    private String proxy;

    // 调整成较小的值以便触发search after查询
    @Value("${elasticsearch.maxResultWindow:3}")
    private Integer maxResultWindow;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        return RestClients.create(
            ClientConfiguration.builder()
                .connectedTo(nodes)
                .withBasicAuth(restUserName, restPassword)
                .build()
        ).rest();
    }

    /**
     * 注册自定义结果映射，主要是为了支持sort_values值注入
     * @return
     */
    @Bean
    public ResultsMapper resultsMapper() {
        return new CustomResultMapper(elasticsearchMappingContext(), entityMapper());
    }

}
