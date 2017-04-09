package com.android.base.utils.net;

import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gg on 2017/4/3.
 */

public class HttpUtils {

    public static final int GET = 1;
    public static final int POST = 2;

    public static void httpGet(String path, HashMap<String, Object> params, CallBack1 callBack1) {
        InputStream inputStream = null;
        try {
            StringBuilder buffer = new StringBuilder();
            //GET里记得加上？
            buffer.append("?");
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    buffer.append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue().toString(), "utf-8"))
                            .append("&");
                }
                //删去最后一个符号&
                buffer.deleteCharAt(buffer.length() - 1);
            }
            //注意这里的路径是get的方式
            URL url = new URL(path + buffer.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                inputStream = connection.getInputStream();
            }
            //接口回调
            callBack1.action(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void httpPost(String path, HashMap<String, Object> params, CallBack1 callBack1) {
        InputStream inputStream = null;
        try {
            StringBuilder buffer = new StringBuilder();
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    buffer.append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue().toString(), "utf-8"))
                            .append("&");
                }
                buffer.deleteCharAt(buffer.length() - 1);
            }
            //获取上传信息的字节大小及长度
            byte[] bytes = buffer.toString().getBytes();
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(3000);
            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
            OutputStream outputStream = connection.getOutputStream();
            //把请求信息写入到输出流当中
            outputStream.write(bytes);
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                inputStream = connection.getInputStream();
            }
            //接口回调
            callBack1.action(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String httpString(String path, HashMap<String, Object> params, int way) {

        if (way == GET) {

        } else if (way == POST) {

        }

        return "";
    }

    public static String httpGetString(String path, HashMap<String, Object> params) {
        final Bundle bundle = new Bundle();
        httpGet(path, params, new CallBack1() {
            @Override
            public void action(InputStream inputStream) {
                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, len);
                    }
                    String data = new String(outputStream.toByteArray());

                    bundle.putString("data", data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return bundle.getString("data");
    }

    public static String httpPostString(String path, HashMap<String, Object> params) {
        final Bundle bundle = new Bundle();
        httpPost(path, params, new CallBack1() {
            @Override
            public void action(InputStream inputStream) {
                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, len);
                    }
                    String data = new String(outputStream.toByteArray());

                    bundle.putString("data", data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return bundle.getString("data");
    }

    public interface CallBack1 {
        void action(InputStream inputStream);
    }

}
