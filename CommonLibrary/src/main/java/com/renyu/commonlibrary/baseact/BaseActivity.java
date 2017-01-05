package com.renyu.commonlibrary.baseact;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.commonutils.PermissionsUtils;
import com.renyu.commonlibrary.networkutils.OKHttpHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by renyu on 2016/12/27.
 */

public abstract class BaseActivity extends AppCompatActivity {

    // 权限接口相关
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
    public abstract int setStatusBarColor();
    public abstract int setStatusBarTranslucent();

    // 网络请求
    public OKHttpHelper httpHelper = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (initViews()!=0) {
            setContentView(initViews());
            ButterKnife.bind(this);
        }

        //设置沉浸式，二选一
        if (setStatusBarColor()!=0) {
            BarUtils.setColor(this, setStatusBarColor());
        }
        if (setStatusBarTranslucent()!=0) {
            BarUtils.setTranslucent(this);
        }

        httpHelper = new OKHttpHelper();

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
                            .setPositiveButton("确定", (dialog, which) -> {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);

                                isCheckAgain=true;
                            })
                            .setNegativeButton("取消", (dialog, which) -> {
                                if (listener!=null) {
                                    listener.denied();
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
                        .setPositiveButton("确定", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);

                            isCheckAgain=true;
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            if (listener!=null) {
                                listener.denied();
                            }
                        }).show();
            }
        }
    }

    public void setDark(Activity activity) {
        String brand= Build.BRAND;
        if (brand.indexOf("Xiaomi")!=-1) {
            setStatusBarDarkMode(true, activity);
        }
        else if (brand.indexOf("Meizu")!=-1) {
            setStatusBarDarkIcon(activity.getWindow(), true);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            int ui = getWindow().getDecorView().getSystemUiVisibility();
            ui |=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;  //黑色
//            ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;  //白色
            decor.setSystemUiVisibility(ui);
        }
    }

    /**
     * 小米修改状态栏字体颜色
     * @param darkmode
     * @param activity
     */
    private void setStatusBarDarkMode(boolean darkmode, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 魅族修改状态栏字体颜色
     * @param window
     * @param dark
     * @return
     */
    private static boolean setStatusBarDarkIcon(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }
}