package com.chinayszc.mobile.okhttp.callback;

/**
 * (暂时不用)
 */
public interface IGenericsSerializator {
    <T> T transform(String response, Class<T> classOfT);
}
