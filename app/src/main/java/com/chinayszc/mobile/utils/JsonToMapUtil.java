package com.chinayszc.mobile.utils;

/**
 * Created by lWX410139 on 2016/12/22.
 */


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonPrimitive;

/**
 * 使用Gson把json字符串转成Map
 * @author lianqiang
 * @date 2014/06/12
 */
public class JsonToMapUtil {

    /**
     * 获取JsonObject
     * @param json
     * @return
     */
    public static JsonObject parseJson(String json){
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = null;
        try {
            jsonObj = parser.parse(json).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    /**
     * 根据json字符串返回Map对象
     * @param json
     * @return
     */
    public static Map<String,Object> toMap(String json){
        return JsonToMapUtil.toMap(JsonToMapUtil.parseJson(json));
    }

    /**
     * 将JSONObjec对象转换成Map-List集合
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(JsonObject json){
        Map<String, Object> map = new HashMap<String, Object>();
        Set<Entry<String, JsonElement>> entrySet = json.entrySet();
        for (Iterator<Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext(); ){
            Entry<String, JsonElement> entry = iter.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof JsonArray)
                map.put((String) key, toList((JsonArray) value));
            else if(value instanceof JsonObject)
                map.put((String) key, toMap((JsonObject) value));
            else if(value instanceof JsonPrimitive) {
                if (((JsonPrimitive) value).isString())
                    map.put((String) key, ((JsonPrimitive) value).getAsString());
                else if (((JsonPrimitive) value).isBoolean())
                    map.put((String) key, ((JsonPrimitive) value).getAsBoolean());
                else if (((JsonPrimitive) value).isNumber())
                    map.put((String) key, ((JsonPrimitive) value).getAsNumber());
                else if (((JsonPrimitive) value).isJsonNull())
                    map.put((String) key, "");
            } else if (value instanceof JsonNull) {
                map.put((String) key, "");
            }else
                map.put((String) key, value);
        }
        return map;
    }

    /**
     * 将JSONArray对象转换成List集合
     * @param json
     * @return
     */
    public static List<Object> toList(JsonArray json){
        List<Object> list = new ArrayList<Object>();
        for (int i=0; i<json.size(); i++){
            Object value = json.get(i);
            if(value instanceof JsonArray){
                list.add(toList((JsonArray) value));
            }
            else if(value instanceof JsonObject){
                list.add(toMap((JsonObject) value));
            }
            else{
                list.add(value);
            }
        }
        return list;
    }

}