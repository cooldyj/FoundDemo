package com.chinayszc.mobile.utils;

import com.fasterxml.jackson.databind.JavaType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Json解析类
 * Created by Jerry on 2017/3/30.
 */

public class ParseJason {

    public static String getJsonValue(String json, String key) {
        String value = null;
        try {
            JSONObject jo = new JSONObject(json);
            value = jo.optString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * JSON字符串转List
     * jsonString JSON字符串
     */
    public static <T> List<T> convertToList(String jsonString, Class<T> tclass) {
        JavaType javaType = JacksonMapper.getInstance().getTypeFactory().constructParametricType(List.class, tclass);
        try {
            return JacksonMapper.getInstance().readValue(jsonString, javaType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * JSON字符串转实体类
     * jsonString JSON字符串
     */
    public static <T> T convertToEntity(String jasonString, Class<T> tclass) {

        try {
            return JacksonMapper.getInstance().readValue(jasonString, tclass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * List 转JSON字符串转
     */
    public static String convertToJson(Object object) {

        try {
            return JacksonMapper.getInstance().writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
