package com.yyyu.interviewhelper.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.yyyu.interviewhelper.MyApplication;

import java.io.File;

/**
 * 功能：图片选择封装工具类
 * 就是隐式调用android自带的activity
 *
 * @author yyyu
 * @date 2016/5/31
 */
public class PicChoiceUtils {

    public static final int PHOTO_REQUEST_GALLERY = 2;// intent回调相册标记
    public static final int PHOTO_REQUEST_CAMERA = 1; // 相机标志
    public static final int PHOTO_REQUEST_CUT = 3; // 剪切意图标志
    public static final String PHOTO_FILE_NAME = "temp_photo.jpg";

    public static void toCamera(Activity activity) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (SDcardUtils.hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(((MyApplication) activity.getApplication()).filePath,
                    PHOTO_FILE_NAME)));
        }
        activity.startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    public static void toGallery(Activity activity) {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /*
     * 图片的剪切（也是一个意图调用android自带的界面）
	 */
    public static void crop(Activity activity, Uri uri, int outputX, int outputY) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    public static void crop(Activity activity, Uri uri){
        crop(activity,uri ,
                DimensChange.dp2px(activity , 300) ,
                DimensChange.dp2px(activity , 400));
    }

}
