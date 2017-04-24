package com.renyu.sostar.impl;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by renyu on 2017/4/24.
 */

public interface FileUploadImpl {

    @POST("http://106.15.46.105:9333/submit")
    Call<ResponseBody> upload(@Body RequestBody Body);
}
