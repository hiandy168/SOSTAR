package com.renyu.sostar.impl;

import com.renyu.commonlibrary.networkutils.params.EmptyResponse;
import com.renyu.commonlibrary.networkutils.params.Response;
import com.renyu.sostar.bean.MsgListResponse;
import com.renyu.sostar.bean.MyCenterEmployeeResponse;
import com.renyu.sostar.bean.MyCenterEmployerResponse;
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

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/verification/get")
    Observable<Response<EmptyResponse>> getVcode(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/user/register")
    Observable<Response<SigninResponse>> signup(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/user/resetpass")
    Observable<Response<EmptyResponse>> resetPwd(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/center")
    Observable<Response<MyCenterEmployeeResponse>> getMyEmployeeCenter(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/user/setUserType")
    Observable<Response<EmptyResponse>> setUserState(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/setStaffInfo")
    Observable<Response<EmptyResponse>> setStaffInfo(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/setStaffAuthentica")
    Observable<Response<EmptyResponse>> setStaffAuthentica(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/center")
    Observable<Response<MyCenterEmployerResponse>> getMyEmployerCenter(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/setEmployerInfo")
    Observable<Response<EmptyResponse>> setEmployerInfo(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/setEmployerAuthentica")
    Observable<Response<EmptyResponse>> setEmployerAuthentica(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/setSuggest")
    Observable<Response<EmptyResponse>> setSuggest(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/all/msgList")
    Observable<Response<MsgListResponse>> msgList(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/all/deleteMsg")
    Observable<Response<EmptyResponse>> deleteMsg(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/all/readMsg")
    Observable<Response<EmptyResponse>> readMsg(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/all/deleteMsgList")
    Observable<Response<EmptyResponse>> deleteMsgList(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/sys/set")
    Observable<Response<EmptyResponse>> setNotificationState(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/sys/userType/set")
    Observable<Response<EmptyResponse>> setUserType(@Body RequestBody requestBody);

}
