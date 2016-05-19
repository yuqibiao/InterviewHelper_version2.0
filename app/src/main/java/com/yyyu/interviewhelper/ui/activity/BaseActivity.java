package com.yyyu.interviewhelper.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.yyyu.interviewhelper.utils.ActivityHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能：Activity的基类
 *
 * @author yyyu
 * @date 2016/5/18
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "BaseActivity";

    /**
     *View的容器
     */
    private Map<Integer ,View> viewContainer = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: 当前显示的Activity是========"+this.getClass().getName());
        ActivityHolder.addActivity(this);
        setContentView(getLayoutId());
        beforeInit();
        initView();
        initListener();
        initData();
        afterInit();
    }
    /**
     * 得到layout的资源Id
     * @return
     */
    public abstract  int getLayoutId();
    /**
     * 初始化之前
     */
    protected void beforeInit() {
    }
    /**
     * 初始化View
     */
    protected abstract void initView();
    /**
     * 注册监听
     */
    protected abstract void initListener();
    /**
     * 初始化数据
     */
    protected void initData() {
    }
    /**
     * 初始化之后
     */
    protected void afterInit() {
    }
    /**
     * findViewById
     */
    protected <T extends View>T getView(int viewId){
        if(viewContainer.containsKey(viewId)){
            return (T) viewContainer.get(viewId);
        }
        return (T) findViewById(viewId);
    }
    /**
     * 注册点击事件监听
     */
    protected void addOnClickListener(int...viewids){
        for (int viewId:viewids) {
            getView(viewId).setOnClickListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewContainer.clear();
        ActivityHolder.removeActivity(this);
    }
}
