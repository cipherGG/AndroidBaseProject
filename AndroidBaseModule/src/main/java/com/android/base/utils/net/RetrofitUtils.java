package com.android.base.utils.net;

import com.android.base.R;
import com.android.base.utils.other.LogUtils;
import com.android.base.utils.str.StringUtils;
import com.android.base.utils.sys.ContextUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by JiangZhiGuo on 2016/10/13.
 * describe Retrofit管理工具类
 */
public class RetrofitUtils<T extends APIUtils> {

    private static RetrofitUtils instance;
    /* 初始化接口 */
    private static InitListener initListener;
    /* api集合（不同的header参数） */
    private final HashMap<String, T> callMap = new HashMap<>();

    public static RetrofitUtils get() {
        if (instance == null) {
            synchronized (RetrofitUtils.class) {
                if (instance == null) {
                    instance = new RetrofitUtils();
                }
            }
        }
        return instance;
    }

    public interface InitListener {
        String getUserToken();

        String getBaseURL();

        String getApiKey();
    }

    /**
     * 在App中初始化
     */
    public static void initApp(InitListener listener) {
        initListener = listener;
    }

    /**
     * 清除缓存的api集合
     */
    public void clearToken() {
        callMap.clear();
    }

    /* head参数（自定义） */
    public enum Head {
        empty, common, token
    }

    /* 返回数据构造器 */
    public enum Factory {
        empty, string, gson
    }

    /* 获取api */
    public T call(Head head, Factory factory) {
        String key = head.name() + factory.name();
        T call = callMap.get(key);
        if (call != null) return call;
        // head
        Interceptor hea;
        if (head == Head.common) { // 通用
            String apiKey = "";
            if (initListener != null) {
                apiKey = initListener.getApiKey();
            }
            hea = getHeader(apiKey, "");
        } else if (head == Head.token) { // token
            String apiKey = "";
            String userToken = "";
            if (initListener != null) {
                apiKey = initListener.getApiKey();
                userToken = initListener.getUserToken();
            }
            hea = getHeader(apiKey, userToken);
        } else { // null
            hea = null;
        }
        // factory
        Converter.Factory fac;
        if (factory == Factory.string) {
            fac = getStringFactory();
        } else if (factory == Factory.gson) {
            fac = getGsonFactory();
        } else {
            fac = null;
        }
        T newCall = getService(hea, fac);
        callMap.put(key, newCall);
        return newCall;
    }

    /* http请求回调 */
    public interface CallBack<M> {
        void onSuccess(M result);

        void onFailure(int httpCode, String errorMessage);
    }

    /**
     * 先调用getCall ,再调用此方法
     *
     * @param call     APIService接口调用
     * @param callBack 回调接口
     * @param <M>      返回实体类
     */
    public <M> void enqueue(Call<M> call, final CallBack<M> callBack) {
        call.enqueue(new Callback<M>() {
            @Override
            public void onResponse(Call<M> call, Response<M> response) {
                int code = response.code();
                M result = response.body();
                // 响应处理
                if (callBack == null) return;
                if (code == 200) { // 200成功
                    callBack.onSuccess(result);
                } else { // 响应非200
                    String errorString = "";
                    try {
                        ResponseBody error = response.errorBody();
                        errorString = error.string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        callBack.onFailure(code, errorString);
                    }
                }
            }

            @Override
            public void onFailure(Call<M> call, Throwable t) {
                Class<? extends Throwable> aClass = t.getClass();
                int errorMessage;
                if (aClass.equals(java.net.ConnectException.class)) { // 网络环境
                    errorMessage = R.string.http_error_connect;
                } else if (aClass.equals(java.net.SocketTimeoutException.class)) { // 超时错误
                    errorMessage = R.string.http_error_time;
                } else { // 其他网络错误
                    errorMessage = R.string.http_error_request;
                    LogUtils.e(t.toString());
                }
                if (callBack == null) return;
                callBack.onFailure(-1, ContextUtils.get().getString(errorMessage));
            }
        });
    }

    /* 构建头信息 */
    private Interceptor getHeader(String apiKey, String token) {
        HashMap<String, String> options = new HashMap<>();
        options.put("Content-Type", "application/json;charset=utf-8");
        options.put("Accept", "application/json");
        if (!StringUtils.isEmpty(apiKey)) {
            options.put("API_KEY", apiKey);
        }
        if (!StringUtils.isEmpty(token)) {
            options.put("Authorization", token);
        }
        return getHeader(options);
    }

    /* 构建头client */
    private Interceptor getHeader(final Map<String, String> options) {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                for (String key : options.keySet()) {
                    builder.addHeader(key, options.get(key));
                }
                Request request = builder.build();
                return chain.proceed(request);
            }
        };
    }

    /* 获取OKHttp的client */
    private OkHttpClient getClient(Interceptor header) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(getLogInterceptor());
        if (header != null) {
            builder.addInterceptor(header);
        }
        return builder.build();
    }

    /* 获取日志拦截器 */
    private Interceptor getLogInterceptor() {
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        String log = message.trim();
                        if (StringUtils.isEmpty(log)) return;
                        if (log.startsWith("{") || log.startsWith("[")) {
                            LogUtils.json(log);
                        } else {
                            LogUtils.d(log);
                        }
                    }
                });
        // BODY 请求/响应行 + 头 + 体
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }

    /* 获取Retrofit实例 */
    private Retrofit getRetrofit(Interceptor header, Converter.Factory factory) {
        Retrofit.Builder builder = new Retrofit.Builder();
        if (initListener != null) {
            builder.baseUrl(initListener.getBaseURL()); // host
        }
        if (factory != null) {
            builder.addConverterFactory(factory); // 解析构造器
        }
        builder.client(getClient(header)); // client
        return builder.build();
    }

    /* 数据解析构造器 */
    private GsonConverterFactory getGsonFactory() {
        return GsonConverterFactory.create();
    }

    private ScalarsConverterFactory getStringFactory() {
        return ScalarsConverterFactory.create();
    }

    /* 获取service 开始请求网络 */
    private T getService(Interceptor header, Converter.Factory factory) {
        Retrofit retrofit = getRetrofit(header, factory);
        return retrofit.create(getCls());
    }

    public RequestBody string2PartBody(String body) {
        return RequestBody.create(MediaType.parse("text/plain"), body);
    }

    public String file2PartKey(File file) {
        return "file\";filename=\"" + file.getName();
    }

    public RequestBody img2PartBody(File file) {
        return RequestBody.create(MediaType.parse("image/jpeg"), file);
    }

    /* 获取当前类 */
    @SuppressWarnings("unchecked")
    private Class<T> getCls() {
        Type type = this.getClass().getGenericSuperclass();
        return (Class<T>) (((ParameterizedType) (type)).getActualTypeArguments()[0]);
    }

}
