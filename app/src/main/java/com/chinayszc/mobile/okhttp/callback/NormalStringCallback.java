package com.chinayszc.mobile.okhttp.callback;

import android.text.TextUtils;

import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.utils.Logs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * 自定义的 String CallBack 返回 String
 */
public abstract class NormalStringCallback extends Callback<String> {
    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException {
        String resp = response.body().string();
        Logs.i("StringArrayCallback---" + resp);
        if (!TextUtils.isEmpty(resp)) {
            try {
                JSONObject jsonObject = new JSONObject(resp);
                int respCode = jsonObject.optInt("result");
                String errorMessage = jsonObject.optString("msg");
                JSONArray data = jsonObject.optJSONArray("body");
                if (respCode == Urls.SUCCESS_CODE) {
                    if (null != data) {
                        return data.toString();
                    } else {
                        return null;
                    }
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
