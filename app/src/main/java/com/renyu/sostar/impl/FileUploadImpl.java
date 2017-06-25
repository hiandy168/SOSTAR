package com.renyu.sostar.impl;

import com.renyu.sostar.params.CommonParams;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by renyu on 2017/4/24.
 */

public interface FileUploadImpl {

    @Streaming
    @POST(CommonParams.UploadUrl+"submit")
    Call<ResponseBody> upload(@Body RequestBody Body);

    @Streaming
    @POST
    Call<ResponseBody> uploadDynamicUrl(@Url String url, @Body RequestBody Body);
}
