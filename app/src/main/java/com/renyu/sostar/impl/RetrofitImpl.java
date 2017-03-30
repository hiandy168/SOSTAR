package com.renyu.sostar.impl;

import com.renyu.commonlibrary.network.params.EmptyResponse;
import com.renyu.commonlibrary.network.params.Response;
import com.renyu.commonlibrary.network.params.ResponseList;
import com.renyu.sostar.bean.EmployeeIndexResponse;
import com.renyu.sostar.bean.EmployerCashAvaliableResponse;
import com.renyu.sostar.bean.EmployerIndexResponse;
import com.renyu.sostar.bean.EmployerStaffListResponse;
import com.renyu.sostar.bean.FavListResponse;
import com.renyu.sostar.bean.MsgListResponse;
import com.renyu.sostar.bean.MyCenterEmployeeResponse;
import com.renyu.sostar.bean.MyCenterEmployerResponse;
import com.renyu.sostar.bean.MyOrderListResponse;
import com.renyu.sostar.bean.OrderResponse;
import com.renyu.sostar.bean.SigninResponse;
import com.renyu.sostar.bean.StartMyOrderSignResponse;

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

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/getEmployerCashAvaiable")
    Observable<Response<EmployerCashAvaliableResponse>> getEmployerCashAvaiable(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/setEmployerOrder")
    Observable<Response<EmptyResponse>> releaseOrder(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/myOrderList")
    Observable<Response<MyOrderListResponse>> myStaffOrderList(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/myOrderList")
    Observable<Response<MyOrderListResponse>> myEmployerOrderList(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/homepage/staff")
    Observable<Response<EmployeeIndexResponse>> getEmployeeIndex(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/homepage/employer")
    Observable<Response<EmployerIndexResponse>> getEmployerIndex(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/user/updatePosition")
    Observable<Response<EmptyResponse>> updatePosition(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/orderRelationDetail")
    Observable<Response<OrderResponse>> staffOrderDetail(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/getEmployerOrderDetail")
    Observable<Response<OrderResponse>> employeeOrderDetail(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/cancleMyOrder")
    Observable<Response<EmptyResponse>> cancleMyOrder(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/getEmployerStaffList")
    Observable<ResponseList<EmployerStaffListResponse>> getEmployerStaffList(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/cancelOrder")
    Observable<Response<EmptyResponse>> cancelEmployeeOrder(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/receiveOrder")
    Observable<Response<EmptyResponse>> receiveOrder(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/orderCenter")
    Observable<Response<MyOrderListResponse>> employeeOrderCenter(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/startMyOrder")
    Observable<Response<EmptyResponse>> startMyOrder(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/confirmStaff")
    Observable<Response<EmptyResponse>> confirmStaff(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/startMyOrderSign")
    Observable<Response<StartMyOrderSignResponse>> startMyOrderSign(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/sign")
    Observable<Response<EmptyResponse>> staffSign(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/applyOff")
    Observable<Response<EmptyResponse>> applyOff(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/fireStaff")
    Observable<Response<EmptyResponse>> fireStaff(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/comfirmResignation")
    Observable<Response<EmptyResponse>> comfirmResignation(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/setMsg")
    Observable<Response<EmptyResponse>> setMsg(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/evaluateStaff")
    Observable<Response<EmptyResponse>> evaluateStaff(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/doFav")
    Observable<Response<EmptyResponse>> doFav(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/searchFav")
    Observable<ResponseList<FavListResponse>> searchFav(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/outFav")
    Observable<Response<EmptyResponse>> outFav(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/setExtrawork")
    Observable<Response<EmptyResponse>> setExtrawork(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/homepage/staffDetail")
    Observable<ResponseList<MyOrderListResponse.DataBean>> searchOrder(@Body RequestBody requestBody);
}
