package com.jiangzg.project.utils;

import com.android.base.utils.str.ConstantUtils;
import com.android.base.utils.str.GsonUtils;
import com.android.base.utils.str.StringUtils;
import com.android.base.utils.view.ToastUtils;
import com.jiangzg.project.MyApp;
import com.jiangzg.project.R;
import com.jiangzg.project.domain.HttpError;
import com.jiangzg.project.service.UpdateService;

import java.util.HashMap;

/**
 * Created by gg on 2017/2/28.
 * 符合本项目的工具类
 */
public class Utils {
    public static final long IMG_SIZE = ConstantUtils.KB * 200; // 图片最大尺寸

    public static final int REQUEST_CAMERA = 191;  // 相机
    public static final int REQUEST_PICTURE = 192;  // 图库
    public static final int REQUEST_CROP = 193;  // 裁剪
    public static final int REQUEST_SCAN = 194;  // 扫描

    public static final int EVENT_COMMON = 1;

    public static boolean noLogin() {
        String userToken = SPUtils.getUser().getUserToken();
        return StringUtils.isEmpty(userToken);
    }

    public static void httpFailure(int httpCode, String errorMessage) {
        switch (httpCode) {
            case -1: // 请求异常(弹出异常信息)
                ToastUtils.get().show(errorMessage);
                break;
            case 401: // 用户验证失败
                ToastUtils.get().show(R.string.http_response_error_401);
//                LoginActivity.goActivity(MyApp.get());
                break;
            case 403: // APIUtils AliKey 不正确 或者没给
                ToastUtils.get().show(R.string.http_response_error_403);
                break;
            case 404: // 404
                ToastUtils.get().show(R.string.http_response_error_404);
                break;
            case 409: // 用户版本过低, 应该禁止用户登录，并提示用户升级
                ToastUtils.get().show(R.string.http_response_error_409);
                SPUtils.clearUser();
                UpdateService.goService(MyApp.get());
                break;
            case 410: // 用户被禁用,请求数据的时候得到该 ErrorCode, 应该退出应用
                ToastUtils.get().show(R.string.http_response_error_410);
//                StackUtils.closeActivities();
                break;
            case 417: // 逻辑错误，必须返回错误信息
                HttpError httpError = GsonUtils.get().fromJson(errorMessage, HttpError.class);
                int errorCode = -1;
                if (httpError != null) {
                    ToastUtils.get().show(httpError.getMessage());
                    errorCode = httpError.getErrorCode();
                }
                switch (errorCode) {
                    case 1001: // 1001: 用户被锁定
//                        StackUtils.closeTopActivity();
                        break;
                }
                break;
            case 500: // 500
                ToastUtils.get().show(R.string.http_response_error_500);
                break;
            default: // 其他错误
                ToastUtils.get().show(R.string.http_response_error);
                break;
        }
    }

    public static String getImageUrl(String url) {
        String foreUrl = "";
        String imgUrl;
        if (url.startsWith("http")) {
            imgUrl = url;
        } else {
            imgUrl = foreUrl + url;
        }
        return imgUrl;
    }

    public static HashMap<String, String> getHead() {
        HashMap<String, String> options = new HashMap<>();
        options.put("Content-Type", "application/json;charset=utf-8");
        options.put("Accept", "application/json");
        return options;
    }

    public static HashMap<String, String> getHeadFull() {
        HashMap<String, String> options = new HashMap<>();
        options.put("Content-Type", "application/json;charset=utf-8");
        options.put("Accept", "application/json");
        options.put("API_KEY", "");
        String userToken = SPUtils.getUser().getUserToken();
        options.put("Authorization", userToken);
        return options;
    }

}
