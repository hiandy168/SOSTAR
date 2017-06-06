package com.renyu.sostar.bean;

import com.google.gson.annotations.SerializedName;
import com.renyu.commonlibrary.network.params.ResponseList;

import java.util.List;

/**
 * Created by renyu on 2017/6/6.
 */

public class SostarResponseList<T> implements ResponseList<T> {
    @SerializedName("data")
    List<T> data;
    @SerializedName("result")
    int result;
    @SerializedName("message")
    String message;

    public List<T> getData() {
        return data;
    }

    @Override
    public void setData(List<T> data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
