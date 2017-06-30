package com.chinayszc.mobile.okhttp;

import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.okhttp.builder.GetBuilder;
import com.chinayszc.mobile.okhttp.builder.HeadBuilder;
import com.chinayszc.mobile.okhttp.builder.OtherRequestBuilder;
import com.chinayszc.mobile.okhttp.builder.PostFileBuilder;
import com.chinayszc.mobile.okhttp.builder.PostFormBuilder;
import com.chinayszc.mobile.okhttp.builder.PostStringBuilder;
import com.chinayszc.mobile.okhttp.callback.Callback;
import com.chinayszc.mobile.okhttp.request.RequestCall;
import com.chinayszc.mobile.okhttp.utils.Platform;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * OkHttp 工具类
 */
public class OkHttpUtils {
    public static final long DEFAULT_MILLISECONDS = 10_000L;  // 默认超时时间10秒
    public static final int CACHE_SIZE = 10 * 1024 * 1024;  // 缓存大小10 MB
    private volatile static OkHttpUtils mInstance;  //OkHttpUtils实例
    private OkHttpClient mOkHttpClient;  //OkHttpClient实例
    private Platform mPlatform;

    /**
     * 构造方法
     */
    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }

        mPlatform = Platform.get();
    }

    /**
     * 用给定的 OkHttpClient 初始化 OkHttpUtils 实例
     */
    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    /**
     * 用默认的 OkHttpClient 初始化 OkHttpUtils 实例
     */
    public static OkHttpUtils getInstance() {
        return initClient(null);
    }

    /**
     * 获取Executor
     */
    public Executor getDelivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    /**
     * 获取当前 OkHttpClient 实例
     */
    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 返回 get 请求的 Builder
     */
    public static GetBuilder get() {
        return new GetBuilder();
    }

    /**
     * 返回 post 提交 json字符串 请求的 Builder
     */
    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    /**
     * 返回 post 上传 File 请求的 Builder
     */
    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    /**
     * 返回 Post 表单形式上传 参数和 File 请求的 Builder
     */
    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    /**
     * (暂时不用)返回 PUT 请求的 Builder
     */
    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static HeadBuilder head() {
        return new HeadBuilder();
    }

    /**
     * (暂时不用)返回 DELETE 请求的 Builder
     */
    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    /**
     * (暂时不用)返回 PATCH 请求的 Builder
     */
    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }

    /**
     * 被 RequestCall 类所调用,执行请求
     * 返回String类型
     */
    public void execute(final RequestCall requestCall, Callback callback) {
        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sendFailResultCallback(call, e, finalCallback, id);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    if (call.isCanceled()) {
                        sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                        return;
                    }

                    if (!finalCallback.validateReponse(response, id)) {
                        sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback, id);
                        return;
                    }

                    //警告：此处只做了String类型返回的处理，并未对其他返回类型，如file之类的,还未处理
                    //对返回的String进行解析，生成不同的对象，并判断业务码是否正确
                    //正确回调sendSuccessResultCallback()
                    //错误回调sendFailResultCallback()
                    Object o = finalCallback.parseNetworkResponse(response, id);
                    if (finalCallback.getResponseCode() == Urls.ERROR_CODE){
                        sendFailResultCallback(call, new Exception(finalCallback.getErrorMessage()), finalCallback, id);
                    }else {
                        sendSuccessResultCallback(o, finalCallback, id);
                    }
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalCallback, id);
                } finally {
                    if (response.body() != null)
                        response.body().close();
                }

            }
        });
    }

    /**
     * 被 RequestCall 类所调用,执行请求
     * 返回下载文件
     */
    public void executeDownload(final RequestCall requestCall, Callback callback) {
        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sendFailResultCallback(call, e, finalCallback, id);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    if (call.isCanceled()) {
                        sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                        return;
                    }

                    if (!finalCallback.validateReponse(response, id)) {
                        sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback, id);
                        return;
                    }

                    Object o = finalCallback.parseNetworkResponse(response, id);
                    sendSuccessResultCallback(o, finalCallback, id);
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalCallback, id);
                } finally {
                    if (response.body() != null)
                        response.body().close();
                }

            }
        });
    }

    /**
     * 处理异步失败回调,对自定义的Callback做回调
     */
    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback, final int id) {
        if (callback == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e, id);
                callback.onAfter(id);
            }
        });
    }

    /**
     * 处理异步成功回调,对自定义的Callback做回调
     */
    public void sendSuccessResultCallback(final Object object, final Callback callback, final int id) {
        if (callback == null) return;
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object, id);
                callback.onAfter(id);
            }
        });
    }

    /**
     * 按TAG来取消请求,把队列中和正在运行的请求取消
     */
    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 方法变量名
     */
    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }
}

