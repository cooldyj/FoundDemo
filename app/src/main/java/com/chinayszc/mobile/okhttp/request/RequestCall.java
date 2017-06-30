package com.chinayszc.mobile.okhttp.request;

import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.Callback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 对 OkHttpRequest 的封装，对外提供更多的接口：cancel(),readTimeOut()...
 */
public class RequestCall {
    private OkHttpRequest okHttpRequest;  //okHttpRequest实例
    private Request request;  //Request实例
    private Call call;  //Call实例

    private long readTimeOut;  //读操作超时时间
    private long writeTimeOut;  //写操作超时时间
    private long connTimeOut;  //连接超时时间

    private OkHttpClient clone;  //OkHttpClient实例

    /**
     * RequestCall 构造方法,传入 OkHttpRequest
     */
    public RequestCall(OkHttpRequest request) {
        this.okHttpRequest = request;
    }

    /**
     * 设置读操作超时时间
     */
    public RequestCall readTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    /**
     * 设置写操作超时时间
     */
    public RequestCall writeTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    /**
     * 设置连接超时时间
     */
    public RequestCall connTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
        return this;
    }

    /**
     * 构造Call实例
     */
    public Call buildCall(Callback callback) {
        request = generateRequest(callback);

        if (readTimeOut > 0 || writeTimeOut > 0 || connTimeOut > 0) {
            readTimeOut = readTimeOut > 0 ? readTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            writeTimeOut = writeTimeOut > 0 ? writeTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            connTimeOut = connTimeOut > 0 ? connTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;

            clone = OkHttpUtils.getInstance().getOkHttpClient().newBuilder()
                    .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                    .connectTimeout(connTimeOut, TimeUnit.MILLISECONDS)
                    .build();

            call = clone.newCall(request);
        } else {
            call = OkHttpUtils.getInstance().getOkHttpClient().newCall(request);
        }
        return call;
    }

    /**
     * 用 okHttpRequest 实例生成 Request
     */
    private Request generateRequest(Callback callback) {
        return okHttpRequest.generateRequest(callback);
    }

    /**
     *
     *----------引擎开启!执行请求操作----------
     * 　　　┏┓　　　┏┓
     * 　　┏┛┻━━━┛┻┓
     * 　　┃　　　　　　　┃
     * 　　┃　　　━　　　┃
     * 　　┃　┳┛　┗┳　┃
     * 　　┃　　　　　　　┃
     * 　　┃　　　┻　　　┃
     * 　　┃　　　　　　　┃
     * 　　┗━┓　　　┏━┛
     * 　　　　┃　　　┃神兽保佑
     * 　　　　┃　　　┃代码无BUG！
     * 　　　　┃　　　┗━━━┓
     * 　　　　┃　　　　　　　┣┓
     * 　　　　┃　　　　　　　┏┛
     * 　　　　┗┓┓┏━┳┓┏┛
     * 　　　　　┃┫┫　┃┫┫
     * 　　　　　┗┻┛　┗┻┛
     * ━━━━━━神兽出没━━━━━━
     */
    public void execute(Callback callback) {
        buildCall(callback);

        if (callback != null) {
            callback.onBefore(request, getOkHttpRequest().getId());
        }

        OkHttpUtils.getInstance().execute(this, callback);
    }

    /**
     * ----------引擎开启!执行下载请求操作----------
     */
    public void excuteDownload(Callback callback){
        buildCall(callback);

        if (callback != null) {
            callback.onBefore(request, getOkHttpRequest().getId());
        }

        OkHttpUtils.getInstance().executeDownload(this, callback);
    }

    /**
     * 获取 Call 实例
     */
    public Call getCall() {
        return call;
    }

    /**
     * 获取 Request 实例
     */
    public Request getRequest() {
        return request;
    }

    /**
     * 获取 OkHttpRequest 实例
     */
    public OkHttpRequest getOkHttpRequest() {
        return okHttpRequest;
    }

    /**
     * 同步执行请求,阻塞进程
     */
    public Response execute() throws IOException {
        buildCall(null);
        return call.execute();
    }

    /**
     * 取消单个请求
     */
    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }


}
