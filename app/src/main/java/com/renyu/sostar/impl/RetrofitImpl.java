package com.renyu.sostar.impl;

import com.renyu.commonlibrary.networkutils.params.EmptyResponse;
import com.renyu.commonlibrary.networkutils.params.Response;
import com.renyu.sostar.bean.MyCenterResponse;
import com.renyu.sostar.bean.SigninResponse;
import com.renyu.sostar.bean.SignupResponse;

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

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/verification/get")
    Observable<Response<EmptyResponse>> getVcode(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/user/register")
    Observable<Response<SignupResponse>> signup(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/user/resetpass")
    Observable<Response<EmptyResponse>> resetPwd(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/center")
    Observable<Response<MyCenterResponse>> getMyCenter(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/user/setUserType")
    Observable<Response<EmptyResponse>> setUserState(@Body RequestBody requestBody);

}
