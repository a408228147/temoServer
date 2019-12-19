package com.creams.temo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Consumer;

/**
 *  基于WebClient封装客户端请求工具类
 */
public class WebClientUtil {

    private WebClient webClient;
    private static Logger logger = LoggerFactory.getLogger("fileInfoLog");

    public WebClientUtil(String baseUrl, Map<String, String> headers, Map<String, String> cookies){
        // 函数式编程，遍历请求头构造参数
        Consumer<HttpHeaders> headersConsumer = null;
        Consumer<MultiValueMap<String, String>> cookiesConsumer = null;
        if (headers != null){
            headersConsumer = n->{
                for (Map.Entry<String,String> entry:headers.entrySet()){
                    n.add(entry.getKey(),entry.getValue());
                }
            };
            headersConsumer.accept(new HttpHeaders());
        }
        if (cookies != null){
            cookiesConsumer = n->{
                for (Map.Entry<String,String> entry:cookies.entrySet()){
                    n.add(entry.getKey(), entry.getValue());
                }
            };
            cookiesConsumer.accept(new LinkedMultiValueMap<>());
        }
        if (headersConsumer != null && cookiesConsumer != null){
            webClient = WebClient.builder()
                    .baseUrl(baseUrl)
                    .defaultHeaders(headersConsumer)
                    .defaultCookies(cookiesConsumer)
                    .build();
        }else if (headersConsumer != null){
            webClient = WebClient.builder()
                    .baseUrl(baseUrl)
                    .defaultHeaders(headersConsumer)
                    .build();
        }else if (cookiesConsumer != null) {
            webClient = WebClient.builder()
                    .baseUrl(baseUrl)
                    .defaultCookies(cookiesConsumer)
                    .build();
        }else {
            webClient = WebClient.builder()
                    .baseUrl(baseUrl)
                    .build();
        }

    }

    /**
     * get请求
     * @param url 请求地址
     * @return
     */
    public Map get(String url,Map<String,String> headers,Map<String,String> cookies){
        // 函数式编程，遍历请求头构造参数
        Consumer<HttpHeaders> headersConsumer = null ;
        Consumer<MultiValueMap<String, String>> cookiesConsumer = null;
        headersConsumer = n->{
            for (Map.Entry<String,String> entry: headers.entrySet()){
                n.add(entry.getKey(),entry.getValue());
            }
        };
        headersConsumer.accept(new HttpHeaders());
        cookiesConsumer = n->{
            for (Map.Entry<String,String> entry:cookies.entrySet()){
                n.add(entry.getKey(), entry.getValue());
            }
        };
        cookiesConsumer.accept(new LinkedMultiValueMap<>());
        Mono<Map> response = webClient.get().uri(url).headers(headersConsumer).cookies(cookiesConsumer).retrieve()
                .onStatus(HttpStatus::isError, res -> Mono.error(new RuntimeException(res.statusCode().value() + ":" + res.statusCode().getReasonPhrase())))
                .bodyToMono(Map.class).timeout(Duration.of(10, ChronoUnit.SECONDS))
                .doAfterSuccessOrError((obj, ex) -> {

                    if(ex != null){
                        logger.info("请求方式：GET" +"\n"
                                +"请求地址："+url+"\n"
                                +"请求头："+headers.toString()+"\n"
                                +"Cookies:"+cookies.toString()+"\n"
                                +",响应异常为:"+ex);
                    }else{
                        logger.info("请求方式：GET" +"\n"
                                +"请求地址："+url+"\n"
                                +"请求头："+headers.toString()+"\n"
                                +"Cookies:"+cookies.toString()+"\n"
                                +",响应结果为:"+obj);
                    }
                });
        return response.block();
    }

    /**
     * post 表单请求
     * @param url 请求地址
     * @param formData  表单参数
     * @return
     */
    public Map postByFormData(String url, MultiValueMap<String,String> formData,Map<String,String> headers,Map<String,String> cookies){

        // 函数式编程，遍历请求头构造参数
        Consumer<HttpHeaders> headersConsumer = null ;
        Consumer<MultiValueMap<String, String>> cookiesConsumer = null;
        headersConsumer = n->{
            for (Map.Entry<String,String> entry:headers.entrySet()){
                n.add(entry.getKey(),entry.getValue());
            }
        };
        headersConsumer.accept(new HttpHeaders());
        cookiesConsumer = n->{
            for (Map.Entry<String,String> entry:cookies.entrySet()){
                n.add(entry.getKey(), entry.getValue());
            }
        };
        cookiesConsumer.accept(new LinkedMultiValueMap<>());
        Mono<Map> response = webClient.post().uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .headers(headersConsumer)
                .cookies(cookiesConsumer)
                .retrieve()
                .bodyToMono(Map.class).timeout(Duration.of(10, ChronoUnit.SECONDS))
                .doAfterSuccessOrError((obj, ex) -> {

                    if(ex != null){
                        logger.info("请求方式：POST" +"\n"
                                +"请求地址："+url+"\n"
                                +"请求头："+headers.toString()+"\n"
                                +"Cookies:"+cookies.toString()+"\n"
                                +"FormData:"+formData.toString()+"\n"
                                +",响应异常为:"+ex);
                    }else{
                        logger.info("请求方式：POST" +"\n"
                                +"请求地址："+url+"\n"
                                +"请求头："+headers.toString()+"\n"
                                +"Cookies:"+cookies.toString()+"\n"
                                +"FormData:"+formData.toString()+"\n"
                                +",响应结果为:"+obj);
                    }
                });;

        return response.block();
    }

    /**
     *  post Json请求
     * @param url 请求地址
     * @param json  json字符串
     * @return
     */
    public Map postByJson(String url, String json,Map<String,String> headers,Map<String,String> cookies){
        // 函数式编程，遍历请求头构造参数
        Consumer<HttpHeaders> headersConsumer = null ;
        Consumer<MultiValueMap<String, String>> cookiesConsumer = null;
        headersConsumer = n->{
            for (Map.Entry<String,String> entry:headers.entrySet()){
                n.add(entry.getKey(),entry.getValue());
            }
        };
        headersConsumer.accept(new HttpHeaders());
        cookiesConsumer = n->{
            for (Map.Entry<String,String> entry:cookies.entrySet()){
                n.add(entry.getKey(), entry.getValue());
            }
        };
        cookiesConsumer.accept(new LinkedMultiValueMap<>());
        Mono<Map> response = webClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(json))
                .headers(headersConsumer)
                .cookies(cookiesConsumer)
                .retrieve()
                .bodyToMono(Map.class).timeout(Duration.of(10, ChronoUnit.SECONDS))
                .doAfterSuccessOrError((obj, ex) -> {
                    if(ex != null){
                        logger.info("请求方式：POST" +"\n"
                                +"请求地址："+url+"\n"
                                +"请求头："+headers.toString()+"\n"
                                +"Cookies:"+cookies.toString()+"\n"
                                +"Body:"+json.toString()+"\n"
                                +",响应异常为:"+ex);
                    }else{
                        logger.info("请求方式：POST" +"\n"
                                +"请求地址："+url+"\n"
                                +"请求头："+headers.toString()+"\n"
                                +"Cookies:"+cookies.toString()+"\n"
                                +"Body:"+json.toString()+"\n"
                                +",响应结果为:"+obj);
                    }
                });
        return response.block();
    }

    /**
     *  put请求
     * @param url 请求地址
     * @param json  json字符串
     * @return
     */
    public Map put(String url, String json,Map<String,String> headers,Map<String,String> cookies){
        // 函数式编程，遍历请求头构造参数
        Consumer<HttpHeaders> headersConsumer = null ;
        Consumer<MultiValueMap<String, String>> cookiesConsumer = null;
        headersConsumer = n->{
            for (Map.Entry<String,String> entry:headers.entrySet()){
                n.add(entry.getKey(),entry.getValue());
            }
        };
        headersConsumer.accept(new HttpHeaders());
        cookiesConsumer = n->{
            for (Map.Entry<String,String> entry:cookies.entrySet()){
                n.add(entry.getKey(), entry.getValue());
            }
        };
        cookiesConsumer.accept(new LinkedMultiValueMap<>());
        Mono<Map> response = webClient.put().uri(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(json))
                .cookies(cookiesConsumer)
                .headers(headersConsumer)
                .retrieve()
                .bodyToMono(Map.class).timeout(Duration.of(10, ChronoUnit.SECONDS))
                .doAfterSuccessOrError((obj, ex) -> {
                    if(ex != null){
                        logger.info("请求方式：PUT" +"\n"
                                +"请求地址："+url+"\n"
                                +"请求头："+headers.toString()+"\n"
                                +"Cookies:"+cookies.toString()+"\n"
                                +"Body:"+json.toString()+"\n"
                                +",响应异常为:"+ex);
                    }else{
                        logger.info("请求方式：PUT" +"\n"
                                +"请求地址："+url+"\n"
                                +"请求头："+headers.toString()+"\n"
                                +"Cookies:"+cookies.toString()+"\n"
                                +"Body:"+json+"\n"
                                +",响应结果为:"+obj);
                    }
                });
        return response.block();
    }

    /**
     *  delete请求
     * @param url 请求地址
     * @return
     */
    public Map delete(String url,Map<String,String> headers,Map<String,String> cookies){
        // 函数式编程，遍历请求头构造参数
        Consumer<HttpHeaders> headersConsumer = null ;
        Consumer<MultiValueMap<String, String>> cookiesConsumer = null;
        headersConsumer = n->{
            for (Map.Entry<String,String> entry:headers.entrySet()){
                n.add(entry.getKey(),entry.getValue());
            }
        };
        headersConsumer.accept(new HttpHeaders());
        cookiesConsumer = n->{
            for (Map.Entry<String,String> entry:cookies.entrySet()){
                n.add(entry.getKey(), entry.getValue());
            }
        };
        cookiesConsumer.accept(new LinkedMultiValueMap<>());
        Mono<Map> response = webClient.delete().uri(url)
                .headers(headersConsumer)
                .cookies(cookiesConsumer)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .doAfterSuccessOrError((obj, ex) -> {
                    if(ex != null){
                        logger.info("请求方式：DELETE" +"\n"
                                +"请求地址："+url+"\n"
                                +"请求头："+headers.toString()+"\n"
                                +"Cookies:"+cookies.toString()+"\n"
                                +",响应异常为:"+ex);
                    }else{
                        logger.info("请求方式：DELETE" +"\n"
                                +"请求地址："+url+"\n"
                                +"请求头："+headers.toString()+"\n"
                                +"Cookies:"+cookies.toString()+"\n"
                                +",响应结果为:"+obj);
                    }
                });;
        return response.block();
    }

    public static void main(String[] args) {
        WebClientUtil webClientUtil = new WebClientUtil("http://129.204.148.24:8080/temo",new HashMap<>(),new HashMap<>());
        System.out.println(webClientUtil.get("/project/1?filter=",new HashMap<>(),new HashMap<>()));
       webClientUtil.put("/project/a3c948f2-bd99-4315-8e7c-1c1dd9991a8b", "{\n" +
               "\t\"pid\": \"a3c948f2-bd99-4315-8e7c-1c1dd9991a8b\",\n" +
               "\t\"envs\": [],\n" +
               "\t\"pname\": \"测试webClientAAA\"\n" +
               "}",new HashMap<>(),new HashMap<>());
        webClientUtil.delete("/prject/69cce7db-7b7f-4fbc-b1f8-d0f8e5dea6f4",new HashMap<>(),new HashMap<>());
    }
}
