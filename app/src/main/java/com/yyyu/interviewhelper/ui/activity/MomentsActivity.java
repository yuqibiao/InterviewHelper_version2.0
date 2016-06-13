package com.yyyu.interviewhelper.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.bean.LoginJson;
import com.yyyu.interviewhelper.net.MyBitmapUtils;
import com.yyyu.interviewhelper.net.MyHttpUrl;
import com.yyyu.interviewhelper.ui.fragment.MomentsFragment;
import com.yyyu.interviewhelper.ui.fragment.UserinfoFragment;
import com.yyyu.interviewhelper.utils.LogicUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 功能：用户关注动态Activity
 *
 * @author yyyu
 * @date 2016/5/30
 */
public class MomentsActivity extends BaseActivity {

    private FloatingActionButton fab_moments;
    private CircleImageView civ_moments_icon;
    private ImageView iv_moments_bg;
    private TabLayout tl_moments;
    private ViewPager vp_moments;
    private FragmentPagerAdapter adapter;
    private ImageView iv_moments_back;
    private CollapsingToolbarLayout ctb_moments;
    private BroadcastReceiver loginCallBack;

    @Override
    protected void beforeInit() {
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MomentsFragment());
        fragments.add(new UserinfoFragment());
        final List<String> tiltes = new ArrayList<>();
        tiltes.add("最新动态");
        tiltes.add("个人信息");
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tiltes.get(position);
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_moments;
    }

    @Override
    protected void initView() {
        ctb_moments = getView(R.id.ctb_moments);
        iv_moments_back = getView(R.id.iv_moments_back);
        iv_moments_bg = getView(R.id.iv_moments_bg);
        civ_moments_icon = getView(R.id.civ_moments_icon);
        fab_moments = getView(R.id.fab_moments);
        tl_moments = getView(R.id.tl_moments);
        vp_moments = getView(R.id.vp_moments);
        //---set
        vp_moments.setAdapter(adapter);
        tl_moments.setupWithViewPager(vp_moments);
        setUserInfo();
    }

    @Override
    protected void initListener() {
        fab_moments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MomentsActivity.this, ModifyUserInfoActivity.class);
                startActivity(intent);
            }
        });
        iv_moments_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MomentsActivity.this.finish();
            }
        });
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ModifyUserInfoActivity.MODIFY_USERINRO_ACTION);
        loginCallBack = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ModifyUserInfoActivity.MODIFY_USERINRO_ACTION.equals(intent.getAction())) {
                    setUserInfo();
                }
            }
        };
        registerReceiver(loginCallBack, filter);
    }

    private void setUserInfo() {
        LoginJson.UserInfo userInfo = LogicUtils.getUserInfo(this);
        ctb_moments.setTitle(userInfo.username);
        MyBitmapUtils.getInstance(this)
                .display(civ_moments_icon, MyHttpUrl.IP_PORT + userInfo.userIcon);
        MyBitmapUtils.getInstance(this)
                .display(iv_moments_bg, MyHttpUrl.IP_PORT + userInfo.userInfoBg, R.mipmap.icon);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(loginCallBack);
    }
}
