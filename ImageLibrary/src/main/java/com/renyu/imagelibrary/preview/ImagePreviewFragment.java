package com.renyu.imagelibrary.preview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.renyu.commonlibrary.basefrag.BaseFragment;
import com.renyu.imagelibrary.R;
import com.renyu.imagelibrary.R2;

import butterknife.BindView;
import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Created by renyu on 2017/3/8.
 */

public class ImagePreviewFragment extends BaseFragment {

    @BindView(R2.id.photo_view)
    PhotoDraweeView photoDraweeView;

    public static ImagePreviewFragment newInstance(String url, int position) {
        ImagePreviewFragment fragment=new ImagePreviewFragment();
        Bundle bundle=new Bundle();
        bundle.putString("url", url);
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initParams() {
        String url=getArguments().getString("url");
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setUri(Uri.parse(url.indexOf("http")!=-1?url:"file://"+url));
        controller.setOldController(photoDraweeView.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null) {
                    return;
                }
                ((ImagePreviewActivity) getActivity()).addPicSize(getArguments().getInt("position"), imageInfo);
                photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        photoDraweeView.setController(controller.build());
        photoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                getActivity().finish();
            }
        });
        photoDraweeView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(getActivity()).setItems(new String[]{"保存"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                return true;
            }
        });
    }

    @Override
    public int initViews() {
        return R.layout.fragment_imagepreview;
    }

    @Override
    public void loadData() {

    }

    public void update(int width, int height) {
        photoDraweeView.update(width, height);
    }
}
