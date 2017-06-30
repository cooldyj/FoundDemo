package com.chinayszc.mobile.okhttp.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 自定义的 Callback
 */
public abstract class Callback<T> {

    /**
     * 返回码
     */
    public int responseCode;
    /**
     * 错误信息
     */
    public String errorMessage;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * UI Thread
     * 在发起请求前的回调方法
     */
    public void onBefore(Request request, int id) {
    }

    /**
     * 请求回调成功或失败后,再回调的方法
     */
    public void onAfter(int id) {
    }

    /**
     * 正在下载等方法中的实时回调
     */
    public void inProgress(float progress, long total, int id) {

    }

    /**
     * 返回该response是否成功
     */
    public boolean validateReponse(Response response, int id) {
        return response.isSuccessful();
    }

    /**
     * 子类重写,对返回内容进行不同的分装
     */
    public abstract T parseNetworkResponse(Response response, int id) throws Exception;

    /**
     * 错误回调
     */
    public abstract void onError(Call call, Exception e, int id);

    /**
     * 成功回调
     */
    public abstract void onResponse(T response, int id);


    public static Callback CALLBACK_DEFAULT = new Callback() {

        @Override
        public Object parseNetworkResponse(Response response, int id) throws Exception {
            return null;
        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(Object response, int id) {

        }
    };

}