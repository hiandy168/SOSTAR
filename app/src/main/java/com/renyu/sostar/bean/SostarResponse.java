package com.renyu.sostar.bean;

import com.google.gson.annotations.SerializedName;
import com.renyu.commonlibrary.network.params.Response;

/**
 * Created by renyu on 2017/6/6.
 */

public class SostarResponse<T> implements Response<T> {
    @SerializedName("data")
    T data;
    @SerializedName("result")
    int result;
    @SerializedName("message")
    String message;

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public int getResult() {
        return result;
    }

    @Override
    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
