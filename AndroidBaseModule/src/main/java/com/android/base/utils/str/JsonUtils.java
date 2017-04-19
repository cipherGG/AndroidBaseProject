package com.android.base.utils.str;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Fan-pc on 2015/11/13.
 * describe json工具类
 */
public class JsonUtils {

    private static Gson instance;

    public static Gson getGSON() {
        if (instance == null) {
            synchronized (JsonUtils.class) {
                if (instance == null) {
                    instance = new GsonBuilder()
                            // 配置
                            .create();
                }
            }
        }
        return instance;
    }

    public static <T> List<T> getList(JSONObject object, String key) {
        JSONArray array = object.optJSONArray(key);
        return getList(array, getType());
    }

    public static <T> List<T> getList(JSONArray array) {
        return instance.fromJson(array.toString(), getType());
    }

    private static <T> List<T> getList(JSONArray array, Type type) {
        return instance.fromJson(array.toString(), type);
    }

    private static <T> Type getType() {
        return new TypeToken<List<T>>() {
        }.getType();
    }

    public static List<Map<String, Object>> getList(String jsonString, String key) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tempObject = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<>();
                Iterator<String> iterator = tempObject.keys();
                while (iterator.hasNext()) {
                    String json_key = iterator.next();
                    Object json_value = tempObject.get(json_key);
                    if (json_value == null)
                        json_value = "";
                    map.put(json_key, json_value);
                }
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Map<String, Object>> getTime(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject resultObject = jsonObject.getJSONObject("result");
            JSONArray listArray = resultObject.getJSONArray("list");
            for (int i = 0; i < listArray.length(); i++) {
                JSONObject listObject = listArray.getJSONObject(i);
                JSONArray trArray = listObject.getJSONArray("tr");
                for (int m = 0; m < trArray.length(); m++) {
                    JSONObject trObject = trArray.getJSONObject(m);

                    Iterator<String> iterator = trObject.keys();
                    Map<String, Object> map = new HashMap<>();
                    while (iterator.hasNext()) {
                        String json_key = iterator.next();
                        Object json_value = trObject.get(json_key);
                        if (json_value == null)
                            json_value = "";
                        map.put(json_key, json_value);
                    }
                    list.add(map);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
