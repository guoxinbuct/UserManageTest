package com.test.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class HttpUtil {
    public static CookieStore cookieStore=new BasicCookieStore();

    public static void httpReqConfig(HttpRequestBase httpRequestBase){
        //header配置
        httpRequestBase.setHeader("User-Agent","Mozilla/5.0");
        httpRequestBase.setHeader("Accept","*/*");
        httpRequestBase.setHeader("Accept-Encoding","gzip,deflate");

        //请求超时设置
        RequestConfig config=RequestConfig.custom()
                .setConnectionRequestTimeout(20000)
                .build();
        httpRequestBase.setConfig(config);
    }

    public static String get(String url,String param) throws IOException {
        System.out.println("getRequest=========================");
        String result="";
        String httpURL=url+"?"+param;
        CloseableHttpClient httpClient =null;
        CloseableHttpResponse response=null;
        //CloseableHttpClient httpClient= HttpClients.createDefault();
        try {
            httpClient = HttpClients.custom()
                    .setDefaultCookieStore(cookieStore)
                    .build();
            HttpGet httpGet = new HttpGet(httpURL);
            httpReqConfig(httpGet);
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity());
            }
            return result;
        }finally {
            response.close();
            httpClient.close();

        }
    }

    public static String post(String url,String param) throws IOException {
        System.out.println("postRequest=======================");
        String result="";
        CloseableHttpResponse response=null;
        CloseableHttpClient httpClient=null;
        List<Cookie> cookieList=cookieStore.getCookies();
        for(Cookie cookie:cookieList){
            System.out.println(cookie.getName()+"="+cookie.getValue());
        }
        try {
            httpClient = HttpClients.custom()
                    .setDefaultCookieStore(cookieStore)
                    .build();
            HttpPost httpPost = new HttpPost(url);
            httpReqConfig(httpPost);
            httpPost.setEntity(new StringEntity(param, "utf-8"));
            if(ParseJsonUtil.isJsonString(param)){
                httpPost.setHeader("content-type", "application/json");
            }else{
                System.out.println("请求入餐不是json");
                httpPost.addHeader("content-type", "application/x-www-form-urlencoded;charset=utf-8");
            }
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity());
            }
            return result;
        }finally {
            try {
                if(response!=null){
                    response.close();
                }
                if(httpClient!=null){
                    httpClient.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
