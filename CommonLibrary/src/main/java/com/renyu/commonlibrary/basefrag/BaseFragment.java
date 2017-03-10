package com.renyu.commonlibrary.basefrag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.renyu.commonlibrary.networkutils.OKHttpHelper;
import com.renyu.commonlibrary.networkutils.Retrofit2Utils;

import butterknife.ButterKnife;
import retrofit2.Retrofit;

/**
 * Created by renyu on 15/12/3.
 */
public abstract class BaseFragment extends Fragment {

    public abstract void initParams();
    public abstract int initViews();
    public abstract void loadData();

    View view=null;

    public OKHttpHelper httpHelper;
    public Retrofit retrofit=null;
    public Retrofit retrofit_uploadimage=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view==null) {
            view=LayoutInflater.from(getActivity()).inflate(initViews(), container, false);
            ButterKnife.bind(this, view);

            httpHelper=new OKHttpHelper();
            Retrofit2Utils.getInstance("http://114.215.18.160:8080/", "http://114.215.18.160:9333/");
            retrofit = Retrofit2Utils.getBaseRetrofit();
            retrofit_uploadimage = Retrofit2Utils.getImageUploadRetrofit();

            initParams();
            loadData();
        }
        ViewGroup parent= (ViewGroup) view.getParent();
        if (parent!=null) {
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
