package com.chinayszc.mobile.okhttp.builder;

import com.chinayszc.mobile.okhttp.request.PostStringRequest;
import com.chinayszc.mobile.okhttp.request.RequestCall;

import okhttp3.MediaType;

/**
 * post 提交 json 字符串请求的 Builder
 */
public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder>
{
    private String content;
    private MediaType mediaType;


    public PostStringBuilder content(String content)
    {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType)
    {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build()
    {
        return new PostStringRequest(url, tag, params, headers, content, mediaType,id).build();
    }


}
