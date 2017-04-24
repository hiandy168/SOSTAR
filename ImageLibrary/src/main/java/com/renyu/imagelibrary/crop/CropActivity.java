package com.renyu.imagelibrary.crop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.edmodo.cropper.CropImageView;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.params.InitParams;
import com.renyu.imagelibrary.R;
import com.renyu.imagelibrary.R2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import id.zelory.compressor.Compressor;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CropActivity extends BaseActivity {

	@BindView(R2.id.nav_layout)
	RelativeLayout nav_layout;
	@BindView(R2.id.ib_nav_left)
	ImageButton ib_nav_left;
	@BindView(R2.id.tv_nav_right)
	TextView tv_nav_right;
	@BindView(R2.id.cropImg)
	CropImageView mCropImage=null;

	Bitmap bmp=null;
	Bitmap cropBmp=null;

	@Override
	public void initParams() {
		FileUtils.createOrExistsDir(InitParams.IMAGE_PATH);

		nav_layout.setBackgroundColor(Color.parseColor("#80000000"));
		ib_nav_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ib_nav_left.setVisibility(View.VISIBLE);
		ib_nav_left.setImageResource(R.mipmap.icon_back_white);
		tv_nav_right.setText("完成");
		tv_nav_right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				crop();
			}
		});
		tv_nav_right.setTextColor(Color.WHITE);
		tv_nav_right.setVisibility(View.VISIBLE);
		mCropImage.setFixedAspectRatio(true);
		mCropImage.setAspectRatio(1000, 1000);
	}

	@Override
	public int initViews() {
		return R.layout.activity_crop;
	}

	@Override
	public void loadData() {
		Observable.just(getIntent().getExtras().getString("path")).map(new Function<String, Bitmap>() {
			@Override
			public Bitmap apply(String s) throws Exception {
				return new Compressor.Builder(CropActivity.this)
						.setCompressFormat(Bitmap.CompressFormat.JPEG)
						.build()
						.compressToBitmap(new File(s));
			}
		}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Bitmap>() {
			@Override
			public void accept(Bitmap bmp) throws Exception {
				CropActivity.this.bmp=bmp;
				mCropImage.setImageBitmap(bmp);
			}
		});
	}

	@Override
	public int setStatusBarColor() {
		return Color.BLACK;
	}

	@Override
	public int setStatusBarTranslucent() {
		return 0;
	}

	public void crop() {
		Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			public void subscribe(ObservableEmitter<String> e) throws Exception {
				try {
					cropBmp=mCropImage.getCroppedImage();
					String path=writeImage(cropBmp, InitParams.IMAGE_PATH+"/"+System.currentTimeMillis()+".jpg", 100);
					e.onNext(path);
				} catch (Exception e1) {
					e.onError(new Exception(getResources().getString(R.string.image_small_error)));
				}
			}
		}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<String>() {
			@Override
			public void onNext(String value) {
				Intent intent=getIntent();
				Bundle bundle=new Bundle();
				bundle.putString("path", value);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}

			@Override
			public void onError(Throwable e) {
				Toast.makeText(CropActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete() {

			}
		});
	}
	
	public String writeImage(Bitmap bitmap,String destPath,int quality) {
		File file=new File(destPath);
		try {
			if(file.exists()) {
				file.delete();
			}
			if (file.createNewFile()) {
				FileOutputStream out = new FileOutputStream(destPath);
				if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
					out.flush();
					out.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getPath();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(bmp!=null&&!bmp.isRecycled()) {
			bmp.recycle();
			bmp=null;
		}
		if(cropBmp!=null&&!cropBmp.isRecycled()) {
			cropBmp.recycle();
			cropBmp=null;
		}
	}
}
