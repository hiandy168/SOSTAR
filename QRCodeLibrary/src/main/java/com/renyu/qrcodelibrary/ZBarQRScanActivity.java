package com.renyu.qrcodelibrary;

import android.Manifest;
import android.os.Vibrator;
import android.util.Log;

import com.renyu.commonlibrary.baseact.BaseActivity;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * Created by renyu on 2017/1/19.
 */

public class ZBarQRScanActivity extends BaseActivity {

    @BindView(R2.id.zbar_scan_view)
    ZBarView zbar_scan_view;

    String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @Override
    public void initParams() {
        zbar_scan_view.setDelegate(new QRCodeView.Delegate() {
            @Override
            public void onScanQRCodeSuccess(String result) {
                Log.d("ZBarQRScanActivity", result);
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(200);
                zbar_scan_view.startSpot();
            }

            @Override
            public void onScanQRCodeOpenCameraError() {

            }
        });
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
        checkPermission(permissions, getResources().getString(R.string.permission_camera), new OnPermissionCheckedListener() {
            @Override
            public void checked(boolean flag) {

            }

            @Override
            public void grant() {
                zbar_scan_view.post(new Runnable() {
                    @Override
                    public void run() {
                        zbar_scan_view.startSpot();
                    }
                });
            }

            @Override
            public void denied() {

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
}
