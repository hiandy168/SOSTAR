package com.renyu.imagelibrary.crop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edmodo.cropper.CropImageView;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.imagelibrary.R;
import com.renyu.imagelibrary.R2;
import com.renyu.imagelibrary.params.CommonParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CropActivity extends BaseActivity {

	@BindView(R2.id.nav_layout)
	RelativeLayout nav_layout;
	@BindView(R2.id.nav_left_image)
	ImageView nav_left_image;
	@BindView(R2.id.nav_right_text)
	TextView nav_right_text;
	@BindView(R2.id.cropImg)
	CropImageView mCropImage=null;

	Bitmap bmp=null;
	Bitmap cropBmp=null;
	String sourcePath;

	@Override
	public void initParams() {
		sourcePath=getIntent().getExtras().getString("path");
		bmp=BitmapFactory.decodeFile(sourcePath);

		nav_layout.setBackgroundColor(Color.parseColor("#80000000"));
		nav_left_image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		nav_left_image.setVisibility(View.VISIBLE);
		nav_left_image.setImageResource(R.mipmap.icon_back_white);
		nav_right_text.setText("完成");
		nav_right_text.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				crop();
			}
		});
		nav_right_text.setTextColor(Color.WHITE);
		nav_right_text.setVisibility(View.VISIBLE);
		mCropImage.setFixedAspectRatio(true);
		mCropImage.setImageBitmap(bmp);
	}

	@Override
	public int initViews() {
		return R.layout.activity_crop;
	}

	@Override
	public void loadData() {

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
					String path=writeImage(cropBmp, CommonParams.IMAGECACHE+"/"+System.currentTimeMillis()+".png", 100);
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
		new File(sourcePath).delete();
	}
}
