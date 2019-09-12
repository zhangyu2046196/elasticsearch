package com.youyuan.es;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author zhangyu
 * @version 1.0
 * @description   transport  client 实现mget批量查询操作
 * @date 2019/2/18 20:52
 */
public class MultiGetCarInfo {

    private static String hostName="192.168.1.109";//es主机

    private static String INDEX="car_shop";//index

    private static String TYPE="cars";//type

    public static void main(String[] args) throws UnknownHostException {
        Settings settings=Settings.builder()
                .put("cluster.name","elasticsearch")
                .build();//设置setting值

        TransportClient transportClient=new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostName),9300));//建立连接

        MultiGetResponse multiGetItemResponses= transportClient.prepareMultiGet()
                .add(INDEX,TYPE,"1")
                .add(INDEX,TYPE,"2")
                .get();//批量查询且获取返回结果

        //遍历查询结果
        for (MultiGetItemResponse multiGetItemResponse:multiGetItemResponses){
            GetResponse getResponse= multiGetItemResponse.getResponse();
            if (getResponse.isExists()){
                System.out.println(getResponse.getSourceAsString());
            }
        }

    }

}
