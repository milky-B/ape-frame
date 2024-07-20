package com.airport.ape.tool;

import com.alibaba.fastjson.JSONObject;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: leen
 * @Description: httpClient 工具类，策略模式
 * @Date: 2023/11/15 10:04
 */
@Slf4j
public class HttpClientUtils {
    // 创建一个静态的 CloseableHttpClient，该实例在整个应用程序生命周期中共享
    private static CloseableHttpClient httpClient;
    /*
    * public T get() {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }
    * */
    public static final ThreadLocal<ParamMethod> CURRENT_REQUEST_METHOD = new ThreadLocal<>();
    private static final String FILE_PREFIX="file";
    private static final ContentType STRING_CONTENT_TYPE = ContentType.create("text/plain", StandardCharsets.UTF_8);
    static {
        // 注册连接工厂，支持 HTTP 和 HTTPS
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())  // 注册明文连接工厂
                .register("https", SSLConnectionSocketFactory.getSocketFactory())  // 注册 SSL/TLS 连接工厂
                .build();

        // 创建连接池管理器，并配置最大连接数、每路由最大连接数、默认连接配置等
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(500);  // 最大总连接数
        connectionManager.setDefaultMaxPerRoute(500);  // 每路由的最大连接数
        connectionManager.setDefaultSocketConfig(
                SocketConfig.custom().setSoTimeout(15, TimeUnit.SECONDS)  // 设置 Socket 超时时间
                        .setTcpNoDelay(true).build()
        );
        connectionManager.setValidateAfterInactivity(TimeValue.ofSeconds(15));  // 在指定时间内不活动后，验证连接的有效性

        // 配置请求超时、连接请求超时、响应超时等
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(1))  // 连接超时时间
                .setConnectionRequestTimeout(Timeout.ofSeconds(1))  // 连接请求超时时间
                .setResponseTimeout(Timeout.ofSeconds(1))  // 响应超时时间
                .build();

        // 创建 HttpClientBuilder，并配置连接管理器、默认请求配置、禁用自动重试等
        httpClient = HttpClients.custom().setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .disableAutomaticRetries()  // 禁用自动重试
                .build();
    }

    public static void setCurrentRequestMethod(PostParamMethod postParamMethod) {
        CURRENT_REQUEST_METHOD.set(postParamMethod);
    }

    public static void setCurrentRequestMethod(GetParamMethod getParamMethod) {
        CURRENT_REQUEST_METHOD.set(getParamMethod);
    }

    public static String post(String url, List<BasicNameValuePair> pairList, Map<String, String> headerMap) {
        if (!(CURRENT_REQUEST_METHOD.get() instanceof PostParamMethod)) {
            setCurrentRequestMethod(HttpClientUtils::jsonParam);
        }
        HttpUriRequestBase httpPost = CURRENT_REQUEST_METHOD.get().buildParam(url, pairList);
        return requestHandler(httpPost, headerMap);
    }

    public static String post(String url, List<BasicNameValuePair> pairList, Map<String, String> headerMap, PostParamMethod postParamMethod) {
        CURRENT_REQUEST_METHOD.set(postParamMethod);
        return post(url, pairList, headerMap);
    }
    public static String multipleFilePost(String url,Map<String,Object> paramMap,Map<String,String> headerMap){
        HttpPost httpPost = fileParam(url, paramMap);
        return requestHandler(httpPost,headerMap);
    }
    public static String get(String url, List<BasicNameValuePair> pairList, Map<String, String> headerMap, GetParamMethod getParamMethod) {
        CURRENT_REQUEST_METHOD.set(getParamMethod);
        return post(url, pairList, headerMap);
    }
    // 可以添加方法获取共享的 httpClient 实例

    public static String get(String url, List<BasicNameValuePair> pairList, Map<String, String> headerMap) {
        if (!(CURRENT_REQUEST_METHOD.get() instanceof GetParamMethod)) {
            setCurrentRequestMethod(HttpClientUtils::queryParamGet);
        }
        HttpUriRequestBase httpGet = CURRENT_REQUEST_METHOD.get().buildParam(url, pairList);
        return requestHandler(httpGet, headerMap);
    }

    public static String requestHandler(HttpUriRequestBase httpUriRequestBase, Map<String, String> headerMap) {
        if (!CollectionUtils.isEmpty(headerMap)) {
            headerMap.forEach((key, value) -> {
                httpUriRequestBase.setHeader(key, value);
            });
        }
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpUriRequestBase);
            return EntityUtils.toString(response.getEntity());
        } catch (ParseException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            if (null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static HttpGet queryParamGet(String url, List<BasicNameValuePair> pairList) {
        url = buildUrl(url, pairList);
        return new HttpGet(url);
    }
    public static HttpPost queryParam(String url, List<BasicNameValuePair> pairList) {
        url = buildUrl(url, pairList);
        return new HttpPost(url);
    }
    private static String buildUrl(String url, List<BasicNameValuePair> pairList) {
        if (!CollectionUtils.isEmpty(pairList)) {
            StringJoiner stringJoiner = new StringJoiner("&");
            pairList.forEach(pair -> {
                stringJoiner.add(pair.getName() + "=" + pair.getValue());
            });
            url += "?"+stringJoiner;
        }
        return url;
    }

    public static HttpPost formParam(String url, List<BasicNameValuePair> pairList) {
        HttpPost httpPost = new HttpPost(url);
        if (!CollectionUtils.isEmpty(pairList)) {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairList);
            httpPost.setEntity(urlEncodedFormEntity);
        }
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        return httpPost;
    }

    public static HttpPost jsonParam(String url, List<BasicNameValuePair> pairList) {
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonBody = new JSONObject();
        if (!CollectionUtils.isEmpty(pairList)) {
            pairList.forEach(pair -> {
                jsonBody.put(pair.getName(), pair.getValue());
            });
        }
        StringEntity requestEntity = new StringEntity(jsonBody.toJSONString(), ContentType.APPLICATION_JSON);
        httpPost.setEntity(requestEntity);
        httpPost.setHeader("Content-Type", "application/json");
        return httpPost;
    }

    public static HttpPost fileParam(String url,Map<String, Object> paramMap){
        HttpPost httpPost = new HttpPost(url);
        if(!CollectionUtils.isEmpty(paramMap)){
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            //设置请求的编码格式
            builder.setCharset(StandardCharsets.UTF_8);
            // 设置 Content-Type
            builder.setContentType(ContentType.MULTIPART_FORM_DATA);
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                String key = entry.getKey();
                Object value = paramMap.get(key);
                // 添加请求参数
                addMultipartBody(builder, key, value);
            }
            HttpEntity entity = builder.build();
            // 将构造好的 entity 设置到 HttpPost 对象中
            httpPost.setEntity(entity);
        }
        return httpPost;
    }
    private static void addMultipartBody(MultipartEntityBuilder builder, String key, Object value) {
        if (value == null) {
            return;
        }
        // MultipartFile 是 spring mvc 接收到的文件。
        if (value instanceof MultipartFile) {
            MultipartFile file = (MultipartFile) value;
            try {
                builder.addBinaryBody(FILE_PREFIX, file.getInputStream(), ContentType.MULTIPART_FORM_DATA, file.getOriginalFilename());
            } catch (IOException e) {
                log.error("read file err.", e);
            }
        } else if (value instanceof File) {
            File file = (File) value;
            builder.addBinaryBody(FILE_PREFIX, file, ContentType.MULTIPART_FORM_DATA, file.getName());
        } else if (value instanceof List) {
            // 列表形式的参数，要一个一个 add
            List<?> list = (List<?>) value;
            for (Object o : list) {
                addMultipartBody(builder, key, o);
            }
        } else if (value instanceof Date) {
            // 日期格式的参数，使用约定的格式
            builder.addTextBody(key, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value));
        } else {
            // 使用 UTF_8 编码的 ContentType，否则可能会有中文乱码问题
            builder.addTextBody(key, value.toString(),STRING_CONTENT_TYPE);
        }

    }

    public interface ParamMethod {
        HttpUriRequestBase buildParam(String url, List<BasicNameValuePair> pairList);
    }

    public interface PostParamMethod extends ParamMethod {
        HttpPost buildParam(String url, List<BasicNameValuePair> pairList);
    }

    public interface GetParamMethod extends ParamMethod {
        HttpGet buildParam(String url, List<BasicNameValuePair> pairList);
    }
}
