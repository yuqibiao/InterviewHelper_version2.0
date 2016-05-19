package com.yyyu.interviewhelper.ui.fragment;

import android.support.v4.view.ViewPager;

import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.adapter.CommonPagerAapter;
import com.yyyu.interviewhelper.ui.widget.ViewpagerLooper;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：主页界面对应的fragment
 *
 * @author yyyu
 * @date 2016/5/18
 */
public class HomeFragment extends BaseFragment{

    private ViewPager vp_home_head;
    private ViewpagerLooper vpl_home;
    private List<String> testData;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        vp_home_head = getView(R.id.vp_home_head);
        vpl_home = getView(R.id.vpl_home);
        vpl_home.setViewPager(vp_home_head);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        testData = new ArrayList<>();
        testData.add("11111111");
        testData.add("2");
        testData.add("3");
        vpl_home.addIndicator(testData.size());
    }

    @Override
    protected void afterInit() {
        vp_home_head.setAdapter(new CommonPagerAapter<String>(context ,testData , R.layout.vp_item_home) {
            @Override
            public void setItemData(String bean) {

            }
        });
    }
}
