package com.youyuan.es;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author zhangyu
 * @version 1.0
 * @description transport Client 全文检索、组合检索、精确检索、前缀检索
 * @date 2019/2/19 11:29
 */
public class FullTextCarSearch {

    private static String hostName="192.168.123.252";//es主机

    private static String INDEX="car_shop";//index

    private static String TYPE="cars";//type

    public static void main(String[] args) throws UnknownHostException {
        //设置setting
        Settings settings=Settings.builder()
                .put("cluster.name","elasticsearch")
                .build();

        //设置transportClient
        TransportClient transportClient=new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostName),9300));

        //全文检索
        SearchResponse searchResponse=transportClient.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setQuery(QueryBuilders.matchQuery("brand","宝马"))
                .get();
        for (SearchHit searchHits:searchResponse.getHits().getHits()){
            System.out.println("全文检索结果:"+searchHits.getSourceAsString());
        }
        System.out.println("=================================================");

        //组合搜索
        searchResponse=transportClient.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setQuery(QueryBuilders.multiMatchQuery("宝马","brand","name"))
                .get();
        for (SearchHit searchHits:searchResponse.getHits().getHits()){
            System.out.println("组合检索结果:"+searchHits.getSourceAsString());
        }
        System.out.println("=================================================");

        //精确检索
        searchResponse=transportClient.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setQuery(QueryBuilders.termQuery("name.raw","宝马318"))
                .get();
        for (SearchHit searchHits:searchResponse.getHits().getHits()){
            System.out.println("精确检索结果:"+searchHits.getSourceAsString());
        }
        System.out.println("=================================================");

        //前缀检索
        searchResponse=transportClient.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setQuery(QueryBuilders.prefixQuery("name","宝"))
                .get();
        for (SearchHit searchHits:searchResponse.getHits().getHits()){
            System.out.println("前缀检索结果:"+searchHits.getSourceAsString());
        }
        System.out.println("=================================================");


    }

}
