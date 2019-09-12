package com.youyuan.es;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

/**
 * @author zhangyu
 * @version 1.0
 * @description 汽车信息操作
 * 模拟场景
 * 1、upsert操作  数据不存在就保存，存在就更新
 * @date 2019/2/15 16:03
 */
public class CarService {

    private static String hostName="192.168.123.252";//es主机

    private static String INDEX="car_shop";//index

    private static String TYPE="cars";//type

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Settings settings=Settings.builder()
                .put("cluster.name", "elasticsearch")//设置集群名字
                //.put("client.transport.sniff",true)//设置打开client集群自动探查功能，可以自动探查到集群中全部节点
                .build();

        TransportClient client=new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostName),9300));

        IndexRequest indexRequest=new IndexRequest(INDEX,TYPE,"1")
                .source(XContentFactory.jsonBuilder()
                    .startObject()
                        .field("brand","宝马")
                        .field("name","宝马320")
                        .field("price", 320000)
                        .field("produce_date", "2017-01-01")
                    .endObject());
        UpdateRequest updateRequest=new UpdateRequest(INDEX,TYPE,"1")
                .doc(XContentFactory.jsonBuilder()
                .startObject()
                    .field("price",310000)
                .endObject())
                .upsert(indexRequest);//绑定indexRequest 如果内容不存在执行indexRequest如果内容存在执行updateRequest
        UpdateResponse updateResponse=client.update(updateRequest).get();
        System.out.println("返回结果:"+updateResponse.getVersion());
    }
}
