package com.map.common.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * httpclient工具类
 */
@Slf4j
public class HttpClientUtil {

    public static <R> R doGet(String serverURL, Object request, Class<R> clazz) {

        List<NameValuePair> params = new ArrayList<>();
        setParams(request, request.getClass(), params);

        String format = URLEncodedUtils.format(params, "utf-8");

        String result = null;
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();

            log.info("请求信息:{}", serverURL + "?" + format);

            HttpGet httpGet = new HttpGet(serverURL + "?" + format);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(180 * 1000)
                    .setConnectionRequestTimeout(180 * 1000)
                    .setSocketTimeout(180 * 1000)
                    .setRedirectsEnabled(true)
                    .build();
            httpGet.setConfig(requestConfig);

            response = httpclient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                result = URLDecoder.decode(EntityUtils.toString(resEntity), "UTF-8");
            } else {
                throw new IllegalStateException("Method failed: " + response.getStatusLine());
            }
        } catch (Exception e) {
            log.error("HttpClient请求GET发生错误", e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                log.error("HttpClient关闭资源发生错误", e);
            }
        }

        log.info("返回信息:{}", result);

        return (R) JSON.parseObject(result, clazz);
    }


    public static <T> void doGet2(String url, T request) {
        List<NameValuePair> params = new ArrayList<>();
        setParams(request, request.getClass(), params);
        try {
            URL httpUrl = new URL(null, url + "?" + URLEncodedUtils.format(params, "UTF-8"), new sun.net.www.protocol.http.Handler());

            HttpURLConnection urlConnection = (HttpURLConnection) httpUrl.openConnection();
            urlConnection.setDoInput(true);//允许读
            urlConnection.setDoOutput(true);//允许写
            urlConnection.setConnectTimeout(10000);

            urlConnection.setRequestMethod("GET");


            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream, "utf-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String str2;
                while ((str2 = br.readLine()) != null) {
                    sb.append(str2);
                }
                System.out.println(sb.toString());
            }

        } catch (Exception e) {
            log.error("请求错误", e);
        }
    }


    public static <P> void setParams(P request, Class clazz, List<NameValuePair> list) {
        if (clazz != Object.class) {
            setParams(request, clazz.getSuperclass(), list);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object o = field.get(request);
                    if (o != null) {
                        log.info("请求参数：{}-{}", field.getName(), o);
                        list.add(new BasicNameValuePair(field.getName(), String.valueOf(o)));
                    }
                } catch (IllegalAccessException e) {
                    log.error("错误信息:", e);
                }

            }
        }
    }
}
