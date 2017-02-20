package com.renyu.sostar.impl;

import com.renyu.commonlibrary.networkutils.params.Response;
import com.renyu.sostar.bean.SigninResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by renyu on 2017/2/20.
 */

public interface RetrofitImpl {
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/user/login")
    Observable<Response<SigninResponse>> signin(@Body RequestBody requestBody);
}
