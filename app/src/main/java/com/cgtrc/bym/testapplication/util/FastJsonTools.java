package com.cgtrc.bym.testapplication.util;

import com.alibaba.fastjson.JSON;

/**
 * Created by BYM on 2016/2/15.
 */
public class FastJsonTools {

    public static <T> T getArtical(String jsonString, Class<T> cls) {
        T t = null;

        try {
            t = JSON.parseObject(jsonString,cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }



}
