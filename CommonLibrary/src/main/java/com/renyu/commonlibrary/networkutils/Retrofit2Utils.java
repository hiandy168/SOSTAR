package com.renyu.commonlibrary.networkutils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by renyu on 2017/2/3.
 */

public class Retrofit2Utils {

    private static volatile Retrofit2Utils retrofit2Utils;

    private OkHttpClient.Builder okBuilder;
    private static Retrofit retrofit;

    private Retrofit2Utils(String baseUrl) {
        okBuilder=new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS);
        //https默认信任全部证书
        HttpsUtils.SSLParams sslParams=HttpsUtils.getSslSocketFactory(null, null, null);
        okBuilder.hostnameVerifier((hostname, session) -> true).sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        retrofit=new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okBuilder.build()).baseUrl(baseUrl).build();
    }

    public static Retrofit getInstance(String baseUrl) {
        if (retrofit2Utils==null) {
            synchronized (Retrofit2Utils.class) {
                if (retrofit2Utils==null) {
                    retrofit2Utils=new Retrofit2Utils(baseUrl);
                }
            }
        }
        return retrofit;
    }

    /**
     * post方式提交json数据
     * @param json
     * @return
     */
    public static RequestBody postJsonPrepare(String json) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        return RequestBody.create(JSON, json);
    }
}
