package com.youyuan;

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
 * @description
 * @date 2019/1/25 11:05
 */
public class EmployeeSearchTestApp {
    public static void main(String[] args) throws UnknownHostException {
        //设置集群名字
        Settings settings=Settings.builder().put("cluster.name","elasticsearch").build();
        //创建es交互客户端
        TransportClient client=new PreBuiltTransportClient(settings);
        //设置es地址,因为通过transport方式与es交互，所以使用的端口为9300
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.123.252"),9300));

        SearchResponse response= client.prepareSearch("company").setTypes("employee")
                .setQuery(QueryBuilders.matchQuery("country","china"))
                .setFrom(0)
                .setSize(1)
                .get();
        SearchHit[] searchHits=response.getHits().getHits();
        for (int i=0;i<searchHits.length;i++){
            System.out.println(searchHits[i].getSourceAsString());
        }

        client.close();
    }
}
