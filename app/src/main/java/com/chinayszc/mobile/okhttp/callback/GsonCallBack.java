package com.chinayszc.mobile.okhttp.callback;

import android.text.TextUtils;

import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.ParseJason;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * 自定义的 Gson CallBack 返回 传入的实体类对象
 * Created by jerry on 2016/8/10.
 */
public abstract class GsonCallBack<T> extends Callback<T> {

    private Class<T> cls;

    public GsonCallBack(Class<T> cls) {
        this.cls = cls;
    }

    @Override
    public T parseNetworkResponse(Response response, int id) throws IOException {
        String resp = response.body().string();
        Logs.i("response--" + resp);
        if (!TextUtils.isEmpty(resp)) {
            try {
                JSONObject jsonObject = new JSONObject(resp);
                int respCode = jsonObject.optInt("result");
                String errorMessage = jsonObject.optString("msg");
                JSONObject data = jsonObject.optJSONObject("body");
                if (respCode == Urls.SUCCESS_CODE) {
                    return ParseJason.convertToEntity(data.toString(), cls);
                } else if(respCode == Urls.NOT_LOGGED_IN_CODE){
                    return ParseJason.convertToEntity(data.toString(), cls);
                } else {
                    setResponseCode(Urls.ERROR_CODE);
                    setErrorMessage(errorMessage);
                    return null;
                }

            } catch (JSONException e) {
                setResponseCode(Urls.ERROR_CODE);
                setErrorMessage(e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        setResponseCode(Urls.ERROR_CODE);
        setErrorMessage("Response Data Is Null");
        return null;
    }
}
