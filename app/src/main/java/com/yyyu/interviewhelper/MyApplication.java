package com.yyyu.interviewhelper;

import android.app.Application;
import android.util.Log;

/**
 * 功能：
 *
 * @author yyyu
 * @date 2016/5/17
 */
public class MyApplication extends Application{

    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ----------------Application");
    }
}
