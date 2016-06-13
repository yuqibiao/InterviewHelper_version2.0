package com.yyyu.interviewhelper;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import cn.sharesdk.framework.ShareSDK;

/**
 * 功能：
 *
 * @author yyyu
 * @date 2016/5/17
 */
public class MyApplication extends Application{

    private static final String TAG = "MyApplication";

    public Context aContext;

    public String filePath;

    @Override
    public void onCreate() {
        super.onCreate();
        this.aContext = getApplicationContext();
        this.filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/interview/";
        File savePath = new File(filePath);
        if(savePath.exists()){
            savePath.mkdirs();
        }
        //shareSDK初始化
        ShareSDK.initSDK(this);
    }


}
