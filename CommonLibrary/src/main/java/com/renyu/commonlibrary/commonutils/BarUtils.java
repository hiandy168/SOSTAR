package com.renyu.commonlibrary.commonutils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by renyu on 2017/1/3.
 */

public class BarUtils {

    /**
     * 状态栏着色的沉浸式
     */
    public static void setColor(Activity activity, int color) {
        //设置contentview为fitsSystemWindows
        ViewGroup viewGroup= (ViewGroup) activity.findViewById(android.R.id.content);
        if (viewGroup.getChildAt(0)!=null) {
            viewGroup.getChildAt(0).setFitsSystemWindows(true);
        }
        Window window=activity.getWindow();
        WindowManager.LayoutParams params=window.getAttributes();
        if (Build.VERSION.SDK_INT==Build.VERSION_CODES.KITKAT) {
            //将状态栏设置成全透明
            int bits=WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if ((params.flags & bits)==0) {
                params.flags |= bits;
                //如果是取消全透明，params.flags &= ~bits;
                window.setAttributes(params);
            }

            //给statusbar着色
            View view=new View(activity);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, com.blankj.utilcode.utils.BarUtils.getStatusBarHeight(activity)));
            view.setBackgroundColor(calculateStatusColor(color, 0));
            viewGroup.addView(view);

            return;
        }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(calculateStatusColor(color, 0));
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                // 去掉系统状态栏下的windowContentOverlay
                View v = window.findViewById(android.R.id.content);
                if (v != null) {
                    v.setForeground(null);
                }
            }
        }
    }

    /**
     * 状态栏全透明沉浸式
     */
    public static void setTranslucent(Activity activity) {
        Window window=activity.getWindow();
        WindowManager.LayoutParams params=window.getAttributes();
        if (Build.VERSION.SDK_INT==Build.VERSION_CODES.KITKAT) {
            //将状态栏设置成全透明
            int bits=WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if ((params.flags & bits)==0) {
                params.flags |= bits;
                //如果是取消全透明，params.flags &= ~bits;
                window.setAttributes(params);
            }
        }
        else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 计算状态栏颜色
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private static int calculateStatusColor(int color, int alpha) {
        try {
            Class BarUtilsClass=Class.forName("com.blankj.utilcode.utils.BarUtils");
            Method calculateStatusColorMethod=BarUtilsClass.getDeclaredMethod("calculateStatusColor", int.class, int.class);
            calculateStatusColorMethod.setAccessible(true);
            return (int) calculateStatusColorMethod.invoke(null, color, alpha);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return Color.TRANSPARENT;
    }

    public static void adjustStatusBar(Activity activity, ViewGroup contentLayout, int color) {
        View view=new View(activity);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, com.blankj.utilcode.utils.BarUtils.getStatusBarHeight(activity)));
        if (color!=-1) {
            view.setBackgroundColor(calculateStatusColor(color, 0));
        }
        contentLayout.addView(view, 0);
    }

    /**
     * 获取底部导航栏高度
     * @param context
     * @return
     */
    public static int getNavBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }

        return navigationBarHeight;
    }

    private static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hasNavigationBar;
    }
}
