package com.youyuan.es;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author zhangyu
 * @version 1.0
 * @description transport Client bool组合多个条件检索
 * @date 2019/2/19 13:42
 */
public class BoolQuerySearch {

    private static String hostName="192.168.123.252";//es主机

    private static String INDEX="car_shop";//index

    private static String TYPE="cars";//type

    public static void main(String[] args) throws UnknownHostException {
        //设置setting
        Settings settings=Settings.builder()
                .put("cluster.name","elasticsearch")
                .build();

        //设置transport Client
        TransportClient transportClient=new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostName),9300));

        //构建QueryBulider
        QueryBuilder queryBuilder= QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("brand","宝马"))
                .mustNot(QueryBuilders.termQuery("name","宝马318"))
                .should(QueryBuilders.rangeQuery("produce_date").gte("2017-01-01").lte("2017-01-28"))
                .filter(QueryBuilders.rangeQuery("price").gte(290000).lte(320000));

        //查询
        SearchResponse searchResponse=transportClient.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setQuery(queryBuilder)
                .get();

        //遍历
        for (SearchHit searchHit:searchResponse.getHits().getHits()){
            System.out.println(searchHit.getSourceAsString());
        }
    }
}
