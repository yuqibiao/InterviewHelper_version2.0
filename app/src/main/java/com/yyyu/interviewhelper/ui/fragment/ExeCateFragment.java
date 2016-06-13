package com.yyyu.interviewhelper.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.adapter.ExeCateAdapter;
import com.yyyu.interviewhelper.bean.ExeCateJson;
import com.yyyu.interviewhelper.callback.OnRvItemClickListener;
import com.yyyu.interviewhelper.net.MyHttpManager;
import com.yyyu.interviewhelper.net.MyHttpUrl;
import com.yyyu.interviewhelper.ui.activity.ExeActivity;
import com.yyyu.interviewhelper.utils.FileUtils;
import com.yyyu.interviewhelper.utils.MyLog;
import com.yyyu.interviewhelper.utils.MyToast;
import com.yyyu.interviewhelper.utils.NetUtils;

/**
 * 功能：练习界面对应的fragment
 *
 * @author yyyu
 * @date 2016/5/18
 */
public class ExeCateFragment extends BaseFragment{

    private RecyclerView rv_category;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_exe;
    }

    @Override
    protected void initView() {
        rv_category = getView(R.id.rv_category);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        String cache = FileUtils.getDataFromCache(getActivity() ,"android_get_exe_cate" );
        if (!NetUtils.isConnected(getActivity()) && !TextUtils.isEmpty(cache)){
            setData(new Gson().fromJson(cache , ExeCateJson.class));
        }else{
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        MyHttpManager.getAsyn(MyHttpUrl.GET_EXE_CATE, new MyHttpManager.ResultCallback<ExeCateJson>() {
            @Override
            public void onError(Request request, Exception e) {
                String cache = FileUtils.getDataFromCache(getActivity() ,"android_get_exe_cate" );
                if (!TextUtils.isEmpty(cache)){
                    setData(new Gson().fromJson(cache , ExeCateJson.class));
                }
            }
            @Override
            public void onResponse(final ExeCateJson exeCateJson) {
                if(exeCateJson.success){
                    //--缓存数据
                    FileUtils.toCacheData(getActivity() , "android_get_exe_cate" , new Gson().toJson(exeCateJson));
                    setData(exeCateJson);
                }
            }
        });
    }

    private void setData(final ExeCateJson exeCateJson) {
        ExeCateAdapter adapter;
        rv_category.setAdapter(adapter = new ExeCateAdapter(getActivity() , exeCateJson.exeCates));
        rv_category.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL) );
        adapter.setOnRvItemClickListener(new OnRvItemClickListener() {
            @Override
            public void OnItemClick(int pos) {
                //---答题界面
                ExeActivity.startAction(getActivity() , exeCateJson.exeCates.get(pos).cateId);
            }
        });
    }
}
