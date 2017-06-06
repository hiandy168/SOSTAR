package com.renyu.sostar.impl;

import com.renyu.commonlibrary.network.params.EmptyResponse;
import com.renyu.sostar.bean.EmployeeIndexResponse;
import com.renyu.sostar.bean.EmployerIndexResponse;
import com.renyu.sostar.bean.EmployerStaffListResponse;
import com.renyu.sostar.bean.FavListResponse;
import com.renyu.sostar.bean.FlowResponse;
import com.renyu.sostar.bean.MsgListResponse;
import com.renyu.sostar.bean.MyCenterEmployeeResponse;
import com.renyu.sostar.bean.MyCenterEmployerResponse;
import com.renyu.sostar.bean.MyOrderListResponse;
import com.renyu.sostar.bean.OrderResponse;
import com.renyu.sostar.bean.PayInfoResponse;
import com.renyu.sostar.bean.EmployerCashAvaliableResponse;
import com.renyu.sostar.bean.PushResponse;
import com.renyu.sostar.bean.RechargeResponse;
import com.renyu.sostar.bean.SigninResponse;
import com.renyu.sostar.bean.SostarResponse;
import com.renyu.sostar.bean.SostarResponseList;
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
    Observable<SostarResponse<SigninResponse>> signin(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/verification/get")
    Observable<SostarResponse<EmptyResponse>> getVcode(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/user/register")
    Observable<SostarResponse<SigninResponse>> signup(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/user/resetpass")
    Observable<SostarResponse<EmptyResponse>> resetPwd(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/center")
    Observable<SostarResponse<MyCenterEmployeeResponse>> getMyEmployeeCenter(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/user/setUserType")
    Observable<SostarResponse<EmptyResponse>> setUserState(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/setStaffInfo")
    Observable<SostarResponse<EmptyResponse>> setStaffInfo(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/setStaffAuthentica")
    Observable<SostarResponse<EmptyResponse>> setStaffAuthentica(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/center")
    Observable<SostarResponse<MyCenterEmployerResponse>> getMyEmployerCenter(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/setEmployerInfo")
    Observable<SostarResponse<EmptyResponse>> setEmployerInfo(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/setEmployerAuthentica")
    Observable<SostarResponse<EmptyResponse>> setEmployerAuthentica(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/setSuggest")
    Observable<SostarResponse<EmptyResponse>> setSuggest(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/all/msgList")
    Observable<SostarResponse<MsgListResponse>> msgList(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/all/deleteMsg")
    Observable<SostarResponse<EmptyResponse>> deleteMsg(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/all/readMsg")
    Observable<SostarResponse<EmptyResponse>> readMsg(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/all/deleteMsgList")
    Observable<SostarResponse<EmptyResponse>> deleteMsgList(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/sys/set")
    Observable<SostarResponse<EmptyResponse>> setNotificationState(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/sys/userType/set")
    Observable<SostarResponse<EmptyResponse>> setUserType(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/setEmployerOrder")
    Observable<SostarResponse<EmptyResponse>> releaseOrder(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/myOrderList")
    Observable<SostarResponse<MyOrderListResponse>> myStaffOrderList(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/myOrderList")
    Observable<SostarResponse<MyOrderListResponse>> myEmployerOrderList(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/homepage/staff")
    Observable<SostarResponse<EmployeeIndexResponse>> getEmployeeIndex(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/homepage/employer")
    Observable<SostarResponse<EmployerIndexResponse>> getEmployerIndex(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/user/updatePosition")
    Observable<SostarResponse<EmptyResponse>> updatePosition(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/orderRelationDetail")
    Observable<SostarResponse<OrderResponse>> staffOrderDetail(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/getEmployerOrderDetail")
    Observable<SostarResponse<OrderResponse>> employerOrderDetail(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/cancleMyOrder")
    Observable<SostarResponse<EmptyResponse>> cancleMyOrder(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/getEmployerStaffList")
    Observable<SostarResponseList<EmployerStaffListResponse>> getEmployerStaffList(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/cancelOrder")
    Observable<SostarResponse<EmptyResponse>> cancelEmployeeOrder(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/receiveOrder")
    Observable<SostarResponse<EmptyResponse>> receiveOrder(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/orderCenter")
    Observable<SostarResponse<MyOrderListResponse>> employeeOrderCenter(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/orderDetail")
    Observable<SostarResponse<OrderResponse>> employeeOrderDetail(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/startMyOrder")
    Observable<SostarResponse<EmptyResponse>> startMyOrder(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/confirmStaff")
    Observable<SostarResponse<EmptyResponse>> confirmStaff(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/startMyOrderSign")
    Observable<SostarResponse<StartMyOrderSignResponse>> startMyOrderSign(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/sign")
    Observable<SostarResponse<EmptyResponse>> staffSign(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/staff/applyOff")
    Observable<SostarResponse<EmptyResponse>> applyOff(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/fireStaff")
    Observable<SostarResponse<EmptyResponse>> fireStaff(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/comfirmResignation")
    Observable<SostarResponse<EmptyResponse>> comfirmResignation(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/refuseResignation")
    Observable<SostarResponse<EmptyResponse>> refuseResignation(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/setMsg")
    Observable<SostarResponse<EmptyResponse>> setMsg(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/evaluateStaff")
    Observable<SostarResponse<EmptyResponse>> evaluateStaff(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/doFav")
    Observable<SostarResponse<EmptyResponse>> doFav(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/searchFav")
    Observable<SostarResponseList<FavListResponse>> searchFav(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/outFav")
    Observable<SostarResponse<EmptyResponse>> outFav(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/setExtrawork")
    Observable<SostarResponse<EmptyResponse>> setExtrawork(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/homepage/staffDetail")
    Observable<SostarResponseList<MyOrderListResponse.DataBean>> searchOrder(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/cash/recharge")
    Observable<SostarResponse<RechargeResponse>> recharge(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/cash/charge")
    Observable<SostarResponse<EmptyResponse>> charge(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/cash/center")
    Observable<SostarResponse<EmployerCashAvaliableResponse>> rechargeInfo(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/cash/bind")
    Observable<SostarResponse<EmptyResponse>> bindCashInfo(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/cash/flow")
    Observable<SostarResponseList<FlowResponse>> flowList(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/getPayInfo")
    Observable<SostarResponse<PayInfoResponse>> getPayInfo(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/employer/setOrderPay")
    Observable<SostarResponse<EmptyResponse>> setOrderPay(@Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("sostar/api/sys/userType/getPushFlg")
    Observable<SostarResponse<PushResponse>> getPushInfo(@Body RequestBody requestBody);
}
