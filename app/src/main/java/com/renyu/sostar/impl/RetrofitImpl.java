package com.renyu.sostar.impl;

import com.renyu.commonlibrary.networkutils.params.Response;
import com.renyu.sostar.bean.ResetPasswordResponse;
import com.renyu.sostar.bean.SigninResponse;
import com.renyu.sostar.bean.SignupResponse;
import com.renyu.sostar.bean.VCodeResponse;

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
    Observable<Response<VCodeResponse>> getVcode(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/user/register")
    Observable<Response<SignupResponse>> signup(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/user/resetpass")
    Observable<Response<ResetPasswordResponse>> resetPwd(@Body RequestBody requestBody);
}
