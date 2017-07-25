package com.temp.tool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

public class Send_Class {
    public static int MAX_CONNECTION_PERROUTE = 1;//最大连接数
    public static int SOCKET_TIMEOUT = 60000;//超时时间
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            String time1= sdf.format(new Date());
            System.out.println(time1);
            Send_Class sc=new Send_Class();
            sc.test();
            time1= sdf.format(new Date());
            System.out.println(time1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public void test(){
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(MAX_CONNECTION_PERROUTE);
        Builder builder = RequestConfig.custom();
        RequestConfig config = builder.setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(SOCKET_TIMEOUT)
                .setStaleConnectionCheckEnabled(true).build();
        CloseableHttpClient client = HttpClients.custom()
                .setMaxConnPerRoute(MAX_CONNECTION_PERROUTE).disableConnectionState()
                .setDefaultRequestConfig(config)
                .setConnectionManager(cm).build();
        String url="http://www.youtube.com";
        HttpPost post = new HttpPost(url);
        post.setProtocolVersion(HttpVersion.HTTP_1_1);
        post.setConfig(config);
        List<NameValuePair> formpair = new ArrayList<NameValuePair>();
        formpair.add(new BasicNameValuePair("name", "张三"));
        HttpEntity entity = new UrlEncodedFormEntity(formpair, Consts.UTF_8);
        if (entity != null) {
            post.setEntity(entity);
        }
        try {   
            client.execute(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
