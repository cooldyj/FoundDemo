package com.chinayszc.mobile.okhttp.builder;

import android.text.TextUtils;

import com.chinayszc.mobile.MyApplication;
import com.chinayszc.mobile.module.common.Env;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.okhttp.request.RequestCall;
import com.chinayszc.mobile.utils.Logs;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Get Post 请求的父类 Builder
 */
public abstract class OkHttpRequestBuilder<T extends OkHttpRequestBuilder>
{
    protected String url;      //请求的url
    protected Object tag;      //请求的tag,可以根据该tag取消这个请求
    protected Map<String, String> headers;      //请求头集合
    protected Map<String, String> params;      //请求体参数集合
    protected int id;

    public T id(int id)
    {
        this.id = id;
        return (T) this;
    }

    public T url(String url)
    {
        this.url = url;
        return (T) this;
    }


    public T tag(Object tag)
    {
        this.tag = tag;
        return (T) this;
    }

    public T headers(Map<String, String> headers)
    {
        this.headers = headers;
        return (T) this;
    }

    public T addHeader(String key, String val)
    {
        if (this.headers == null)
        {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return (T) this;
    }

    public T addCommonHeaderAndBody(){
        addCommonHeader();
        addCommonBody();
        return (T) this;
    }

    private void addCommonHeader(){
        if (this.headers == null)
        {
            headers = new LinkedHashMap<>();
        }
        headers.put("Sign", "test");
        if(Env.isLoggedIn && !TextUtils.isEmpty(Env.token)){
            headers.put("Token", Env.token);
        } else {
            headers.put("Token", "144711D33E25F926124B8C89671E74FE");
        }

        headers.put("Accept-Number", String.valueOf(System.currentTimeMillis()));
    }

    private void addCommonBody(){
        if (this.params == null)
        {
            params = new LinkedHashMap<>();
        }
        params.put("version", Env.versionName);
        params.put("deviceToken", Env.deviceToken);
        params.put("deviceType", Urls.DEVICE_TYPE);
    }

    public abstract RequestCall build();
}
