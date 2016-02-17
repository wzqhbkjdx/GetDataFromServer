package com.cgtrc.bym.testapplication.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by BYM on 2016/2/15.
 */
public class FastJsonTools {

    /**
     * 将json字符串解析为一个JavaBean
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T getArtical(String jsonString, Class<T> cls) {
        T t = null;

        try {
            t = JSON.parseObject(jsonString,cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 将json字符串解析为一个List<JavaBean>
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> getArticals(String jsonString, Class<T> cls){
        List<T> list = new ArrayList<T>();
        try{
            list = JSON.parseArray(jsonString,cls);
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static List<Map<String,Object>> getListMap(String jsonString){
        List<Map<String, Object>> list = new ArrayList<>();
        try{
            list = JSON.parseObject(jsonString, new TypeReference<List<Map<String, Object>>>(){});
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }



}
