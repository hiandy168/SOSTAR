package com.renyu.sostar.activity;

import android.Manifest;

import com.blankj.utilcode.utils.FileUtils;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.base.BaseActivity;
import com.renyu.sostar.params.CommonParams;

/**
 * Created by renyu on 2016/12/27.
 */

public class SplashActivity extends BaseActivity {

    @Override
    public void initParams() {
        String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        checkPermission(permissions, "请授予SD卡读写权限", new OnPermissionCheckedListener() {
            @Override
            public void checked(boolean flag) {

            }

            @Override
            public void grant() {
                // 初始化文件夹
                FileUtils.createOrExistsDir(CommonParams.IMAGE_PATH);
                FileUtils.createOrExistsDir(CommonParams.HOTFIX_PATH);
                FileUtils.createOrExistsDir(CommonParams.FILE_PATH);
            }

            @Override
            public void denied() {

            }
        });
    }

    @Override
    public int initViews() {
        return R.layout.activity_splash;
    }

    @Override
    public void loadData() {

    }
}
