package com.chinayszc.mobile.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JacksonMapper
 */

class JacksonMapper {

    private static ObjectMapper mapper;

    private JacksonMapper(){

    }

    public static ObjectMapper getInstance(){
        if (mapper == null) {
            mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return mapper;
    }
}
