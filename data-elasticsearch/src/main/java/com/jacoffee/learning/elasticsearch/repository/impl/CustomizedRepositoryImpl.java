package com.jacoffee.learning.elasticsearch.repository.impl;

import static com.jacoffee.learning.elasticsearch.utils.Constants.ES_ID;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.jacoffee.learning.elasticsearch.dto.Order;
import com.jacoffee.learning.elasticsearch.dto.Page;
import com.jacoffee.learning.elasticsearch.dto.PageList;
import com.jacoffee.learning.elasticsearch.repository.CustomizedRepository;
import com.jacoffee.learning.elasticsearch.model.ESDocument;
import com.jacoffee.learning.elasticsearch.utils.JsonUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * CustomizedRepositoryImpl 注意相应Repository的实现一定要以Impl结尾
 * The most important part of the class name that corresponds to the fragment interface is the
 * <code>Impl postfix</code>.
 * @see <a href="https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/">spring repository</a>
 * @author jacoffee
 * @date 2020-07-05
 */
public class CustomizedRepositoryImpl<T extends ESDocument> implements CustomizedRepository<T> {

    @Autowired
    public RestHighLevelClient elasticsearchClient;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    private List<SortBuilder<?>> buildESSortBuilder(Page page) {
        if (page.getOrders().isEmpty()) {
            return Lists.newArrayList();
        } else {
            List<SortBuilder<?>> sortBuilders =
                page.getOrders().stream().map(order -> {
                    SortOrder sortOrder =
                        order.getDirection().equals(Order.OrderDirection.DESC) ? SortOrder.DESC : SortOrder.ASC;

                    return SortBuilders.fieldSort(order.getName()).order(sortOrder);
                }).collect(Collectors.toList());

            sortBuilders.add(SortBuilders.fieldSort(ES_ID).order(SortOrder.DESC));

            return sortBuilders;
        }
    }

    @Override
    public PageList<T> doSearchAfterPageQuery(
        QueryBuilder queryBuilder, Page page, Class<T> clazz
    ) throws IOException {
        String indexName = elasticsearchOperations.getPersistentEntityFor(clazz).getIndexName();

        Object[] searchAfter = page.getSearchAfter();
        List<SortBuilder<?>> sortBuilders = buildESSortBuilder(page);

        if (
            searchAfter.length != sortBuilders.size()
        ) {
            throw new IllegalArgumentException("Invalid search after parameter");
        }

        SearchRequest searchRequest = new SearchRequest().indices(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);

        // 注意 ===> `from` parameter must be set to 0 when `search_after` is used.
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(page.getPageSize());

        searchSourceBuilder.searchAfter(searchAfter);
        sortBuilders.forEach(sortBuilder -> {
            searchSourceBuilder.sort(sortBuilder);
        });
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits searchHits = searchResponse.getHits();
        List<T> matchedDocuments =
            Arrays.asList(searchHits.getHits()).stream().map(searchHit -> {
                T userESDocument =
                    Optional.of(searchHit.getSourceAsString()).transform(json -> {
                        T resultESDocument = JsonUtils.readValueAs(json, clazz);
                        if (
                            Objects.nonNull(searchHit.getSortValues()) &&
                            searchHit.getSortValues().length > 0
                        ) {
                            resultESDocument.setSort(searchHit.getSortValues());
                        }

                        return resultESDocument;
                    }).orNull();
                return userESDocument;
            }).collect(Collectors.toList());

        Object[] sortValues = new Object[0];
        if (!matchedDocuments.isEmpty()) {
            T lastOne = matchedDocuments.get(matchedDocuments.size() - 1);
            if (Objects.nonNull(lastOne.getSort())) {
                sortValues = lastOne.getSort();
            }
        }

        Page resultPage = new Page();
        resultPage.setPageNum(page.getPageNum());
        resultPage.setPageSize(page.getPageSize());
        resultPage.setTotalRows(searchHits.getTotalHits());
        resultPage.setTotalPage((searchHits.getTotalHits() / page.getPageSize()) + 1);
        resultPage.setSearchAfter(sortValues);

        return new PageList<>(resultPage, matchedDocuments);
    }

    /**
     * 如果排序为空，则不做处理；如果加了排序，默认拼接es_id作为tie_breaker_id
     * @param page page
     * @return
     */
    private Sort buildSpringESSort(Page page) {
        if (page.getOrders().isEmpty()) {
            return Sort.unsorted();
        } else {
            List<Sort.Order> orders =
                page.getOrders().stream().map(order -> {
                    Sort.Direction direction =
                        order.getDirection().equals(Order.OrderDirection.DESC) ?
                            Sort.Direction.DESC : Sort.Direction.ASC;
                    return new Sort.Order(direction, order.getName());
                }).collect(Collectors.toList());

            orders.add(new Sort.Order(Sort.Direction.DESC, ES_ID));

            return Sort.by(orders);
        }
    }

    @Override
    public PageList<T> doNormalPageQuery(QueryBuilder queryBuilder, Page page, Class<T> clazz) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryBuilder);
        // 排序字段
        Sort sort = buildSpringESSort(page);

        // Page转换 注意需要-1，Elasticsearch中的from从0开始
        PageRequest pageRequest = PageRequest.of(page.getPageNum() - 1, page.getPageSize(), sort);

        nativeSearchQuery.setPageable(pageRequest);
        org.springframework.data.domain.Page<T> domainPage =
            elasticsearchOperations.queryForPage(nativeSearchQuery, clazz);

        Object[] sortValues = new Object[0];
        if (sort.isSorted()) {
            List<T> matchedDocuments = domainPage.getContent();
            if (!matchedDocuments.isEmpty()) {
                T lastOne = matchedDocuments.get(matchedDocuments.size() - 1);
                if (Objects.nonNull(lastOne.getSort())) {
                    sortValues = lastOne.getSort();
                }
            }
        }

        Page resultPage = new Page();
        resultPage.setPageNum(pageRequest.getPageNumber() + 1);
        resultPage.setPageSize(pageRequest.getPageSize());
        resultPage.setTotalRows(domainPage.getTotalElements());
        resultPage.setTotalPage(Long.valueOf(domainPage.getTotalPages()));
        resultPage.setSearchAfter(sortValues);

        return new PageList<>(resultPage, domainPage.getContent());
    }

}
