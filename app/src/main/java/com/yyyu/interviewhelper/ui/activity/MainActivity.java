package com.yyyu.interviewhelper.ui.activity;

import android.animation.ArgbEvaluator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.ui.fragment.ExeFragment;
import com.yyyu.interviewhelper.ui.fragment.HomeFragment;
import com.yyyu.interviewhelper.ui.fragment.MsgFragment;
import com.yyyu.interviewhelper.ui.fragment.LoveFragment;
import com.yyyu.interviewhelper.ui.widget.ChangeColorView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private List<ChangeColorView> tabIndicators = new ArrayList<>();
    private List<Integer> colors = new ArrayList<>();

    private Toolbar tb_main;
    private ViewPager vp_main;
    private FragmentPagerAdapter tabsAdapter;
    private ChangeColorView ccv_main;
    private ChangeColorView ccv_exe;
    private ChangeColorView ccv_msg;
    private ChangeColorView ccv_love;
    private LinearLayout ll_main_btm;
    private ArgbEvaluator evaluator;
    private DrawerLayout dl_main;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void beforeInit() {
        final List<Fragment> tabs = new ArrayList<Fragment>();
        tabs.add(new HomeFragment());
        tabs.add(new ExeFragment());
        tabs.add(new MsgFragment());
        tabs.add(new LoveFragment());
        FragmentManager fm = getSupportFragmentManager();
        tabsAdapter = new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return tabs.get(position);
            }

            @Override
            public int getCount() {
                return tabs.size();
            }
        };
        //--初始化颜色
        colors.add(getResources().getColor(R.color.home_main_color));
        colors.add(getResources().getColor(R.color.exe_main_color));
        colors.add(getResources().getColor(R.color.msg_main_color));
        colors.add(getResources().getColor(R.color.love_main_color));
        evaluator = new ArgbEvaluator();
    }

    @Override
    protected void initView() {
        tb_main = getView(R.id.tb_main);
        vp_main = getView(R.id.vp_main);
        //--得到底部tab
        ccv_main = getView(R.id.ccv_main);
        ccv_exe = getView(R.id.ccv_exe);
        ccv_msg = getView(R.id.ccv_msg);
        ccv_love = getView(R.id.ccv_love);
        ccv_main.setIconAlpha(1.0f);
        tabIndicators.add(ccv_main);
        tabIndicators.add(ccv_exe);
        tabIndicators.add(ccv_msg);
        tabIndicators.add(ccv_love);
        ll_main_btm = getView(R.id.ll_main_btm);
        dl_main = getView(R.id.dl_main);

    }

    @Override
    protected void initListener() {
        addOnClickListener(R.id.ccv_main, R.id.ccv_exe, R.id.ccv_msg, R.id.ccv_love);
        vp_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    ChangeColorView left = tabIndicators.get(position);
                    ChangeColorView right = tabIndicators.get(position + 1);
                    left.setIconAlpha(1 - positionOffset);
                    right.setIconAlpha(positionOffset);
                    //---使toolbar颜色渐变
                    int leftColor = colors.get(position);
                    int rightColor = colors.get(position+1);
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, leftColor,rightColor);
                    tb_main.setBackgroundColor(evaluate);
                    ll_main_btm.setBackgroundColor(evaluate);
                }
            }

            @Override
            public void onPageSelected(int position) {
                //--设置toolbar的颜色
                tb_main.setBackgroundColor(colors.get(position));
                ll_main_btm.setBackgroundColor(colors.get(position));
                if (position!=0){
                    dl_main.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }else{
                    dl_main.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {
        vp_main.setAdapter(tabsAdapter);
    }

    @Override
    protected void afterInit() {
        super.afterInit();
        ll_main_btm.setBackgroundColor(colors.get(0));
        setSupportActionBar(tb_main);
    }

    /**
     * 重置其他的Tab
     */
    private void resetOtherTabs() {
        for (int i = 0; i < tabIndicators.size(); i++) {
            tabIndicators.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ccv_main: {
                resetOtherTabs();
                tabIndicators.get(0).setIconAlpha(1.0f);
                vp_main.setCurrentItem(0, false);
                break;
            }
            case R.id.ccv_exe: {
                resetOtherTabs();
                tabIndicators.get(1).setIconAlpha(1.0f);
                vp_main.setCurrentItem(1, false);
                break;
            }
            case R.id.ccv_msg: {
                resetOtherTabs();
                tabIndicators.get(2).setIconAlpha(1.0f);
                vp_main.setCurrentItem(2, false);
                break;
            }
            case R.id.ccv_love: {
                resetOtherTabs();
                tabIndicators.get(3).setIconAlpha(1.0f);
                vp_main.setCurrentItem(3, false);
                break;
            }
        }
    }

}
