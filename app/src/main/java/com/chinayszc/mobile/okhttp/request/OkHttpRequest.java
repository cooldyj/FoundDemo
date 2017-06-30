package com.chinayszc.mobile.okhttp.request;

import com.chinayszc.mobile.okhttp.callback.Callback;
import com.chinayszc.mobile.okhttp.utils.Exceptions;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 自定义 OkHttpRequest 类,包装了url,tag,params,headers等
 * 一般由子类实现,构造具体请求体
 */
public abstract class OkHttpRequest {
    protected String url;      //请求的url
    protected Object tag;      //请求的tag,可以根据该tag取消这个请求
    protected Map<String, String> params;      //请求体参数集合
    protected Map<String, String> headers;      //请求头集合
    protected int id;

    protected Request.Builder builder = new Request.Builder();//OkHttp Request Builder实例

    protected OkHttpRequest(String url, Object tag,
                            Map<String, String> params, Map<String, String> headers, int id) {
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
        this.id = id;

        if (url == null) {
            Exceptions.illegalArgument("url can not be null.");
        }

        initBuilder();
    }


    /**
     * 初始化一些基本参数 url , tag , headers
     */
    private void initBuilder() {
        builder.url(url).tag(tag);
        appendHeaders();
    }

    /**
     * 由子类实现构造请求体 RequestBody
     */
    protected abstract RequestBody buildRequestBody();

    /**
     * 由子类实现,包装传入的请求体 RequestBody,
     * 若不需要包装,则不必重写,继续返回传入的请求体 RequestBody
     */
    protected RequestBody wrapRequestBody(RequestBody requestBody, final Callback callback) {
        return requestBody;
    }

    /**
     * 由子类实现,通过传入的请求体 RequestBody 构造请求实例 Request
     */
    protected abstract Request buildRequest(RequestBody requestBody);

    /**
     * 传入 OkHttpRequest 本身,构造 RequestCall
     */
    public RequestCall build() {
        return new RequestCall(this);
    }

    /**
     * 生成OkHttp请求实例Request,
     * callback仅用于wrapRequestBody()方法,若子类不需要包装,没实现该方法,则callback在此方法中无用
     */
    public Request generateRequest(Callback callback) {
        RequestBody requestBody = buildRequestBody();
        RequestBody wrappedRequestBody = wrapRequestBody(requestBody, callback);
        Request request = buildRequest(wrappedRequestBody);
        return request;
    }

    /**
     * 添加 Headers
     */
    protected void appendHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;

        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    /**
     * 获取 id
     */
    public int getId() {
        return id;
    }

}
