package com.jiangzg.project.utils;

import com.android.base.utils.net.APIUtils;
import com.jiangzg.project.domain.Version;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by gg on 2017/4/9.
 * describe Retrofit接口
 */
public interface API extends APIUtils {

    /* BaseURL最好以/结尾 */
    String HOST = "http://192.168.0.1/"; // 测试
    //    String HOST = "http://192.168.0.1/"; // 正式
    String BASE_URL = HOST + "api/v1/zh-CN/";
    String IMG_FORE_URL = ""; // 图片前缀

    @GET("checkUpdate/{version}")
    Call<Version> checkUpdate(@Path("version") int version);

}
