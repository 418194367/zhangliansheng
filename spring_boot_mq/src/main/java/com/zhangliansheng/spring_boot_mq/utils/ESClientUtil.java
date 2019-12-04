package com.zhangliansheng.spring_boot_mq.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>项目名称：zhangliansheng
 * 类名称：ESClientUtil
 * 类描述：
 * 创建人：zhangliansheng
 * 创建时间：2019/11/26 10:38
 */
@SpringBootConfiguration
public class ESClientUtil {

    private static RestHighLevelClient client;

    private static final String type = "_doc";

    @Bean
    public static RestHighLevelClient getClient() {
        if (client == null) {
            client = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost("192.168.8.201", 9200, "http")));
        }
        return client;
    }

    /**
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author zhangliansheng
     * @Description 根据id查询单条数据
     * @Date 16:29 2019/12/2
     * @Param [index, id]
     **/
    protected static Map<String, Object> getById(String index, String id) throws IOException {
        getClient();
        GetRequest getRequest = new GetRequest(index, type, id);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        if (getResponse.isExists()) {
            return getResponse.getSourceAsMap();
        }
        return null;
    }

    /**
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author zhangliansheng
     * @Description 根据多个id查询
     * @Date 16:30 2019/12/2
     * @Param [index, ids]
     **/
    protected static List<Map<String, Object>> getByIds(String index, List<String> ids) throws IOException {
        getClient();
        List<Map<String, Object>> results = new ArrayList<>();
        MultiGetRequest request = new MultiGetRequest();
        ids.stream().forEach(id -> {
            request.add(new MultiGetRequest.Item(index, type, id));
        });
        MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);
        GetResponse getResponse;
        for (int i = 0; i < response.getResponses().length; i++) {
            getResponse = response.getResponses()[i].getResponse();
            if (getResponse.isExists()) {
                results.add(getResponse.getSourceAsMap());
            }
        }
        return results;
    }

    /**
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author zhangliansheng
     * @Description 条件查询
     * @Date 16:33 2019/12/2
     * @Param [index, queryBuilder, pageNo, pageSize]
     **/
    protected static List<Map<String, Object>> getByWhere(String index, QueryBuilder queryBuilder, int pageNo, int pageSize) throws IOException {
        getClient();
        List<Map<String, Object>> results = new ArrayList<>();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(pageNo);
        searchSourceBuilder.size(pageSize);

        SearchRequest searchRequest = new SearchRequest(index).types(type);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            results.add(hit.getSourceAsMap());
        }
        return results;
    }

    /**
     * @param queryBuilder
     * @param indexs
     * @return count
     * @throws IOException
     */
    public static long count(QueryBuilder queryBuilder, String... indexs) throws IOException {
        getClient();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);

        CountRequest countRequest = new CountRequest(indexs);
        countRequest.source(searchSourceBuilder);

        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
        long count = countResponse.getCount();
        return count;
    }

    /**
     * @return java.lang.Double
     * @Author zhangliansheng
     * @Description 根据字段求最大的
     * @Date 16:37 2019/12/2
     * @Param [queryBuilder, field, indexs]
     **/
    public static Double getMax(QueryBuilder queryBuilder, String field, String... indexs) throws IOException {
        getClient();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(0);

        AggregationBuilder aggregationBuilder = AggregationBuilders.max("agg").field(field);
        searchSourceBuilder.aggregation(aggregationBuilder);

        SearchRequest searchRequest = new SearchRequest(indexs).types(type);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Max agg = searchResponse.getAggregations().get("agg");
        return agg.getValue();
    }

    /**
     * @Author zhangliansheng
     * @Description 一个字段分组
     * @Date 16:52 2019/12/2
     * @Param [queryBuilder, field, indexs]
     * @return java.util.Map<java.lang.String,java.lang.Long>
     **/
    public static Map<String, Long> getTermsAgg(QueryBuilder queryBuilder, String field, String... indexs) throws IOException {
        Map<String, Long> groupMap = new HashMap<>();
        getClient();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(0);

        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("agg").field(field);
        searchSourceBuilder.aggregation(aggregationBuilder);

        SearchRequest searchRequest = new SearchRequest(indexs).types(type);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Terms terms = searchResponse.getAggregations().get("agg");
        for (Terms.Bucket entry : terms.getBuckets()) {
            groupMap.put(entry.getKey().toString(), entry.getDocCount());
        }
        return groupMap;
    }

    /**
     * @return void
     * @Author zhangliansheng
     * @Description 示例一个字段分组
     * @Date 16:50 2019/12/2
     * @Param [index]
     **/
    protected static void testGetTermsAgg(String index) {
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("name.keyword", "部门*");
        try {
            Map<String, Long> groupMap = getTermsAgg(queryBuilder, "level", index);
            groupMap.forEach((key, value) -> System.out.println(key + " -> " + value.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Long>>
     * @Author zhangliansheng
     * @Description 多个字段分组查询
     * @Date 16:51 2019/12/2
     * @Param [queryBuilder, field1, field2, indexs]
     **/
    public static Map<String, Map<String, Long>> getTermsAggTwoLevel(QueryBuilder queryBuilder, String field1, String field2, String... indexs) throws IOException {
        Map<String, Map<String, Long>> groupMap = new HashMap<>();
        getClient();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(0);

        AggregationBuilder agg1 = AggregationBuilders.terms("agg1").field(field1);
        AggregationBuilder agg2 = AggregationBuilders.terms("agg2").field(field2);
        agg1.subAggregation(agg2);
        searchSourceBuilder.aggregation(agg1);

        SearchRequest searchRequest = new SearchRequest(indexs).types(type);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Terms terms1 = searchResponse.getAggregations().get("agg1");
        Terms terms2;
        for (Terms.Bucket bucket1 : terms1.getBuckets()) {
            terms2 = bucket1.getAggregations().get("agg2");
            Map<String, Long> map2 = new HashMap<>();
            for (Terms.Bucket bucket2 : terms2.getBuckets()) {
                map2.put(bucket2.getKey().toString(), bucket2.getDocCount());
            }
            groupMap.put(bucket1.getKey().toString(), map2);
        }
        return groupMap;
    }

    /**
     * @Author zhangliansheng
     * @Description 示例多个字段分组查询
     * @Date 16:52 2019/12/2
     * @Param [index]
     * @return void
     **/
    protected static void testGetTermsAgg2(String index) {
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("name.keyword", "部门*");
        try {
            Map<String, Map<String, Long>> groupMap = getTermsAggTwoLevel(queryBuilder, "level", "status", index);
            groupMap.forEach((key, value) -> System.out.println(key + " -> " + value.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
