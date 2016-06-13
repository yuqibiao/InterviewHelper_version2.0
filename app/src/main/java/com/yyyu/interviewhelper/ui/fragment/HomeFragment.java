package com.yyyu.interviewhelper.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.bean.MenuDataJson;
import com.yyyu.interviewhelper.callback.OnFragmentCreatedListener;
import com.yyyu.interviewhelper.callback.OnMenuBoardLoadedListener;
import com.yyyu.interviewhelper.ui.activity.MainActivity;
import com.yyyu.interviewhelper.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：主页界面对应的fragment
 *
 * @author yyyu
 * @date 2016/5/18
 */
public class HomeFragment extends BaseFragment {

    private static final String TAG = "HomeFragment";


    private TabLayout tl_home;
    private ViewPager vp_home;
    private FragmentPagerAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void beforeInit() {

    }

    @Override
    protected void initView() {
        tl_home = getView(R.id.tl_home);
        vp_home = getView(R.id.vp_home);
    }

    @Override
    protected void initListener() {
        //---菜单数据请求成功的回调
        ((MainActivity)getActivity()).setOnMenuBoardLoadedListener(new OnMenuBoardLoadedListener() {
            @Override
            public void onMenuBoardLoaded(final List<MenuDataJson.ClazzJson.BoardJson> boardData) {
                final List<Fragment> tabs = new ArrayList<Fragment>();
                for (int i=0 ; i<boardData.size();i++){
                    tabs.add(HomeItemFragment.getInstance(boardData.get(i).boardId));
                }
                adapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {
                        return tabs.get(position);
                    }

                    @Override
                    public int getCount() {
                        return tabs.size();
                    }

                    //-------------- 一定要设置ID  不然会重复利用ffragment---------------
                    @Override
                    public long getItemId(int position) {
                        return boardData.get(position).boardId;
                    }

                    @Override
                    public CharSequence getPageTitle(int position) {
                        return boardData.get(position).boardName;
                    }
                };
                vp_home.setAdapter(adapter);
                tl_home.setupWithViewPager(vp_home);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.onFragmentCreatedListener.onFragmentCreatedListener();
    }

    private OnFragmentCreatedListener onFragmentCreatedListener;
    public void setOnFragmentCreatedListener(OnFragmentCreatedListener onFragmentCreatedListener){
        this.onFragmentCreatedListener = onFragmentCreatedListener;
    }
}
