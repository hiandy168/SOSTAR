package com.renyu.commonlibrary.commonutils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

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
        }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(calculateStatusColor(color, 0));
        }
    }

    /**
     * 状态栏着色的沉浸式
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

    public static void adjustStatusBar(Activity activity, View nav_top_layout) {
        int statusBarHeight= com.blankj.utilcode.utils.BarUtils.getStatusBarHeight(activity);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) nav_top_layout.getLayoutParams();
        params.height=statusBarHeight;
        nav_top_layout.setLayoutParams(params);
    }
}
