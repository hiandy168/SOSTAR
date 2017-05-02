package com.renyu.qrcodelibrary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.BarUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * Created by renyu on 2017/1/19.
 */

public class ZBarQRScanActivity extends BaseActivity {

    @BindView(R2.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R2.id.ib_nav_left)
    ImageButton ib_nav_left;
    @BindView(R2.id.zbar_scan_view)
    ZBarView zbar_scan_view;
    @BindView(R2.id.tv_zbar_scan_view_time)
    TextView tv_zbar_scan_view_time;
    @BindView(R2.id.tv_zbar_scan_view_tip)
    TextView tv_zbar_scan_view_tip;

    @Override
    public void initParams() {
        tv_nav_title.setTextColor(Color.WHITE);
        tv_nav_title.setText("扫码签到");
        ib_nav_left.setImageResource(R.mipmap.ic_arrow_write_left);
        ib_nav_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        BarUtils.adjustStatusBar(this, (ViewGroup) ((ViewGroup) zbar_scan_view.getParent()).getChildAt(2), -1);

        zbar_scan_view.setDelegate(new QRCodeView.Delegate() {
            @Override
            public void onScanQRCodeSuccess(String result) {
                Log.d("ZBarQRScanActivity", result);
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(200);
                zbar_scan_view.startSpot();

                Intent intent=new Intent();
                intent.putExtra("result", result);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onScanQRCodeOpenCameraError() {

            }
        });

        tv_zbar_scan_view_time.setText("订单编号  "+getIntent().getStringExtra("orderId")
                +"   "+"工作时间  "+getIntent().getStringExtra("startTime")+"-"+getIntent().getStringExtra("endTime"));
        refreshTime();
    }

    @Override
    public int initViews() {
        return R.layout.activity_zbarqrscan;
    }

    @Override
    public void loadData() {

    }

    @Override
    public int setStatusBarColor() {
        return 0;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 1;
    }

    @Override
    protected void onStart() {
        super.onStart();
        zbar_scan_view.startCamera();
//        zbar_scan_view.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        zbar_scan_view.showScanRect();
        zbar_scan_view.post(new Runnable() {
            @Override
            public void run() {
                zbar_scan_view.startSpot();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        zbar_scan_view.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        zbar_scan_view.onDestroy();
    }

    private void refreshTime() {
        String[] periodTime=getIntent().getStringExtra("periodTime").split(",");
        long currentTime1 =System.currentTimeMillis();
        for (String s : periodTime) {
            SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm");
            try {
                long startTime=format.parse(s+" "+getIntent().getStringExtra("startTime")).getTime();
                long endTime=format.parse(s+" "+getIntent().getStringExtra("endTime")).getTime();
                // 任务开始范围外
                if (currentTime1>endTime) {
                    continue;
                }
                // 任务正在执行，半小时内可以补签到
                else if (currentTime1>=startTime && currentTime1<=endTime) {
                    if (startTime+1000*60*30>currentTime1) {
                        tv_zbar_scan_view_tip.setText("提示：已经开工,半小时内补签到");
                    }
                    else {
                        tv_zbar_scan_view_tip.setText("提示：已经开工");
                    }
                }
                // 任务还未开始
                else if (currentTime1<startTime) {
                    int minute= (int) ((startTime- currentTime1)/(1000*60));
                    tv_zbar_scan_view_tip.setText("提示：距离开工还有 "+minute+" 分钟\n请尽快提醒你的雇员扫码签到开工");
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
