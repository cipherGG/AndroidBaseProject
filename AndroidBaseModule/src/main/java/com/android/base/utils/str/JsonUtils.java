package com.android.base.utils.str;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by gg on 2017/4/3.
 * Json管理类
 */
public class JsonUtils {

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
