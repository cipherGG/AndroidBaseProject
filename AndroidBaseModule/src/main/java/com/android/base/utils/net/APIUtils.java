package com.android.base.utils.net;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by JiangZhiGuo on 2016/10/14.
 * describe Retrofit接口
 */
public interface APIUtils {

    /* BaseURL最好以/结尾 */
    String HOST = "http://192.168.0.1/"; // 测试
    //    String HOST = "http://192.168.0.1/"; // 正式
    String BASE_URL = HOST + "api/v1/zh-CN/";
    String IMG_FORE_URL = ""; // 图片前缀
//
//    @Streaming // 下载大文件(请求需要放在子线程中)
//    @Multipart // 上传文件
//    @GET("demo/{path}")
//    Call<List<User>> demo(@Url String url, @Path("path") String path, // {path}
//                          @Header("key") String key, @HeaderMap Map<String, String> headers,
//                          @Query("limit") String limit, @QueryMap Map<String, String> options,
//                          @Part("name") String value, @PartMap Map<String, RequestBody> params,
//                          @Body User user, @Body String requestBody);

    @Streaming
    @GET
    Call<ResponseBody> downloadLargeFile(@Url String url);

//    @GET("checkUpdate/{version}")
//    Call<Version> checkUpdate(@Path("version") int version);

}
