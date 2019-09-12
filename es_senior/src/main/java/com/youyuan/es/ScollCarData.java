package com.youyuan.es;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author zhangyu
 * @version 1.0
 * @description transport Client 操作scoll批量查询
 * @date 2019/2/19 10:23
 */
public class ScollCarData {

    private static String hostName="192.168.123.252";//es主机

    private static String INDEX="car_shop";//index

    private static String TYPE="sales";//type

    public static void main(String[] args) throws UnknownHostException {
        //设置setting
        Settings settings=Settings.builder()
                .put("cluster.name","elasticsearch")
                .build();

        //创建transport Client
        TransportClient transportClient=new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostName),9300));

        //查询
        SearchResponse searchResponse=transportClient.prepareSearch(INDEX)//index
                .setTypes(TYPE)//type
                .setScroll(TimeValue.timeValueSeconds(60000))//超时时间
                .setSize(1)//每次查询数量
                .setQuery(QueryBuilders.termQuery("brand","宝马"))
                .get();

        //计数器
        int preCount=0;
        //在此批量查询
        do {
            //遍历
            for (SearchHit searchHits:searchResponse.getHits().getHits()){
                System.out.println("这是第"+ ++preCount+"条件记录");
                System.out.println(searchHits.getSourceAsString());

                //批量搜索一批数据后进行处理，不能全部搜索出来，否则jvm也承受不了
            }

            searchResponse=transportClient.prepareSearchScroll(searchResponse.getScrollId())
                    .setScroll(TimeValue.timeValueSeconds(60000))
                    .execute()
                    .actionGet();
        }while (searchResponse.getHits().getHits().length!=0);
    }

}
