package com.renyu.sostar.activity.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.renyu.sostar.utils.PermissionsUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by renyu on 2016/12/27.
 */

public abstract class BaseActivity extends AppCompatActivity {

    //权限接口相关
    public OnPermissionCheckedListener listener;
    private List<String> permission;
    String deniedDesp;
    public interface OnPermissionCheckedListener {
        void checked(boolean flag);
        void grant();
        void denied();
    }
    boolean isCheckAgain;

    public abstract void initParams();
    public abstract int initViews();
    public abstract void loadData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(initViews());
        initParams();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isCheckAgain) {
            isCheckAgain=false;
            checkPermission();
        }
    }

    public void checkPermission(String[] permissions, String deniedDesp, OnPermissionCheckedListener listener) {
        this.permission = Arrays.asList(permissions);
        this.deniedDesp=deniedDesp;
        this.listener=listener;
        checkPermission();
    }

    private void checkPermission() {
        if (permission==null || permission.size()==0) {
            return;
        }
        String[] permissions=new String[permission.size()];
        for (int i = 0; i < permission.size(); i++) {
            permissions[i]=permission.get(i);
        }
        if (PermissionsUtils.lacksPermissions(this, permissions)) {
            if (PermissionsUtils.hasDelayAllPermissions(this, permissions)) {
                if (listener!=null) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(BaseActivity.this);
                    builder.setTitle("提示")
                            .setMessage(deniedDesp)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    startActivity(intent);

                                    isCheckAgain=true;
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (listener!=null) {
                                        listener.denied();
                                    }
                                }
                            }).setCancelable(false).show();
                }
            }
            else {
                PermissionsUtils.requestPermissions(this, permissions);
            }
        }
        else {
            if (listener!=null) {
                listener.grant();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGrant=true;
        for (int grantResult : grantResults) {
            if (grantResult== PackageManager.PERMISSION_DENIED) {
                isGrant=false;
                break;
            }
        }
        if (listener!=null) {
            listener.checked(isGrant);
            if (isGrant) {
                if (listener!=null) {
                    listener.grant();
                }
            }
            else {
                AlertDialog.Builder builder=new AlertDialog.Builder(BaseActivity.this);
                builder.setTitle("提示")
                        .setMessage(deniedDesp)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);

                                isCheckAgain=true;
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (listener!=null) {
                                    listener.denied();
                                }
                            }
                        }).show();
            }
        }
    }
}
