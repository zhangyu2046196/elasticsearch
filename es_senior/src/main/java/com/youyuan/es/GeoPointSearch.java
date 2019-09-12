package com.youyuan.es;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyu
 * @version 1.0
 * @description transport Client geo point检索
 * @date 2019/2/19 14:06
 */
public class GeoPointSearch {

    private static String hostName="192.168.123.252";//es主机

    private static String INDEX="car_shop";//index

    private static String TYPE="shops";//type

    public static void main(String[] args) throws UnknownHostException {
        //设置setting
        Settings settings=Settings.builder()
                .put("cluster.name","elasticsearch")
                .build();

        //设置连接
        TransportClient transportClient=new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostName),9300));

        //第一个需求 搜索两个坐标组成的区域
        SearchResponse searchResponse=transportClient.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setQuery(QueryBuilders.geoBoundingBoxQuery("pin.location").setCorners(40.73, -74.1, 40.01, -71.12))
                .get();
        for (SearchHit searchHit:searchResponse.getHits().getHits()){
            System.out.println(searchHit.getSourceAsString());
        }
        System.out.println("=====搜索两个坐标组成的区域=====");

        //第二个需求 指定一个区域，由三个坐标点，组成，比如上海大厦，东方明珠塔，上海火车站
        List<GeoPoint> points = new ArrayList<GeoPoint>();
        points.add(new GeoPoint(40.73, -74.1));
        points.add(new GeoPoint(40.01, -71.12));
        points.add(new GeoPoint(50.56, -90.58));

        searchResponse=transportClient.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setQuery(QueryBuilders.geoPolygonQuery("pin.location",points))
                .get();
        for (SearchHit searchHit:searchResponse.getHits().getHits()){
            System.out.println(searchHit.getSourceAsString());
        }
        System.out.println("=====搜索指定区域=====");

        //第三个需求 搜索距离当前位置在200公里内的4s店
        searchResponse=transportClient.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setQuery(QueryBuilders.geoDistanceQuery("pin.location").point(40, -70).distance(200, DistanceUnit.KILOMETERS))
                .get();
        for (SearchHit searchHit:searchResponse.getHits().getHits()){
            System.out.println(searchHit.getSourceAsString());
        }
        System.out.println("=====搜索距离当前位置指定距离=====");

        transportClient.close();
    }


}
