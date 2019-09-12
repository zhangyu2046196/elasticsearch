package com.youyuan.es;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author zhangyu
 * @version 1.0
 * @description  transport Client 实现bulk批量操作
 * @date 2019/2/18 22:11
 */
public class BulkCarSale {

    private static String hostName="192.168.1.109";//es主机

    private static String INDEX="car_shop";//index

    private static String TYPE="sales";//type

    public static void main(String[] args) throws IOException {
        //设置setting
        Settings settings=Settings.builder()
                .put("cluster.name","elasticsearch")
                .build();

        //设置transport Client
        TransportClient transportClient=new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostName),9300));

        //创建BulkRequestBuilder对象
        BulkRequestBuilder bulkRequestBuilder=transportClient.prepareBulk();

        //添加document信息需要放到bulkRequestBuilder中
        IndexRequestBuilder indexRequestBuilder=transportClient.prepareIndex(INDEX,TYPE,"3")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                            .field("brank","奔驰")
                            .field("name","奔驰C200")
                            .field("price",350000)
                            .field("produce_date","2017-01-05")
                            .field("sale_price",320000)
                            .field("sale_date","2017-02-18")
                        .endObject()
                );
        //添加到批量处理中
        bulkRequestBuilder.add(indexRequestBuilder);

        //修改请求的document数据
        UpdateRequestBuilder updateRequestBuilder=transportClient.prepareUpdate(INDEX,TYPE,"1")
                .setDoc(XContentFactory.jsonBuilder()
                    .startObject()
                        .field("sale_price",290000)
                    .endObject()
                );

        //添加到批量处理中
        bulkRequestBuilder.add(updateRequestBuilder);

        //删除document请求
        DeleteRequestBuilder deleteRequestBuilder=transportClient.prepareDelete(INDEX,TYPE,"2");

        //添加到批量处理中
        bulkRequestBuilder.add(deleteRequestBuilder);

        //发起批量执行请求
        BulkResponse bulkResponse=bulkRequestBuilder.get();
        for (BulkItemResponse bulkItemResponse:bulkResponse.getItems()){
            System.out.println(bulkItemResponse.getVersion());
        }

        transportClient.close();
    }

}
