package com.chinayszc.mobile.okhttp.request;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 自定义类，封装了 Get 请求
 */
public class GetRequest extends OkHttpRequest
{
    public GetRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, int id)
    {
        super(url, tag, params, headers,id);
    }

    @Override
    protected RequestBody buildRequestBody()
    {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody)
    {
        return builder.get().build();
    }


}
