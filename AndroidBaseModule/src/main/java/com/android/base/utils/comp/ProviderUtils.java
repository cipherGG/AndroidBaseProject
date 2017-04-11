package com.android.base.utils.comp;

import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gg on 2017/3/13.
 * 内容提供者
 */
public class ProviderUtils {

    /**
     * @param uri        查询的uri
     * @param projection map里的key
     * @param orderBy    排序
     * @return 查询到的数据
     */
    public static List<Map<String, String>> getProviderColumn(Uri uri, String[] projection,
                                                              String selection,
                                                              String[] selectionArgs,
                                                              String orderBy) {
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = ContextUtils.get().getContentResolver()
                .query(uri, projection, selection, selectionArgs, orderBy);
        if (null == cursor) return list;
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < projection.length; i++) {
                map.put(projection[i], cursor.getString(i));
            }
            list.add(map);
        }
        cursor.close();
        return list;
    }

    public static String getProviderColumnTop(Uri uri, String[] projection, String selection,
                                              String[] selectionArgs, String orderBy) {
        Cursor cursor = ContextUtils.get().getContentResolver()
                .query(uri, projection, selection, selectionArgs, orderBy);

        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(projection[0]);
            if (index > -1) return cursor.getString(index);
            cursor.close();
        }
        return null;
    }

}
