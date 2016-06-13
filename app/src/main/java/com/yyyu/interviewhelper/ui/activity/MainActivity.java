package com.yyyu.interviewhelper.ui.activity;

import android.animation.ArgbEvaluator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.adapter.ClazzAdapter;
import com.yyyu.interviewhelper.animation.HomeBtmAnim;
import com.yyyu.interviewhelper.bean.FollowJson;
import com.yyyu.interviewhelper.bean.LoginJson;
import com.yyyu.interviewhelper.bean.MenuDataJson;
import com.yyyu.interviewhelper.callback.OnFragmentCreatedListener;
import com.yyyu.interviewhelper.callback.OnMenuBoardLoadedListener;
import com.yyyu.interviewhelper.callback.OnMenuHideListener;
import com.yyyu.interviewhelper.callback.OnRvItemClickListener;
import com.yyyu.interviewhelper.net.MyBitmapUtils;
import com.yyyu.interviewhelper.net.MyHttpUrl;
import com.yyyu.interviewhelper.ui.fragment.ExeCateFragment;
import com.yyyu.interviewhelper.ui.fragment.HomeFragment;
import com.yyyu.interviewhelper.ui.fragment.LoveFragment;
import com.yyyu.interviewhelper.ui.fragment.MsgFragment;
import com.yyyu.interviewhelper.ui.widget.ChangeColorView;
import com.yyyu.interviewhelper.ui.widget.HideMenuLayout;
import com.yyyu.interviewhelper.ui.widget.dialog.LoadingDialog;
import com.yyyu.interviewhelper.ui.widget.dialog.QRCodeDialog;
import com.yyyu.interviewhelper.utils.ActivityHolder;
import com.yyyu.interviewhelper.utils.FileUtils;
import com.yyyu.interviewhelper.utils.LogicUtils;
import com.yyyu.interviewhelper.utils.MyLog;
import com.yyyu.interviewhelper.net.MyHttpManager;
import com.yyyu.interviewhelper.utils.MySnackBar;
import com.yyyu.interviewhelper.utils.MyToast;
import com.yyyu.interviewhelper.utils.NetUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int TO_CAPTURE = 0;

    private List<ChangeColorView> tabIndicators = new ArrayList<>();
    private List<Integer> colors = new ArrayList<>();
    private ClazzAdapter clazzAdapter;
    private BroadcastReceiver loginCallBack;

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
    private LinearLayout ll_home;
    private HideMenuLayout hl_main;
    private LinearLayout nv_main;
    private TextView tv_username;
    private CoordinatorLayout cl_main;
    private RecyclerView rv_clazz;
    private TextView tv_intro;
    private CircleImageView civ_icon;
    private ImageView iv_qr;
    private LoadingDialog loadingDialog;
    private QRCodeDialog qrCodeDialog;
    private LoginJson.UserInfo userInfo;
    HomeFragment homeFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void beforeInit() {
        //---添加tab fragment界面
        final List<Fragment> tabs = new ArrayList<Fragment>();
        tabs.add(homeFragment=new HomeFragment());
        tabs.add(new ExeCateFragment());
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
        loadingDialog = new LoadingDialog(this);
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
        cl_main = getView(R.id.cl_main);
        tb_main = getView(R.id.tb_main);
        vp_main = getView(R.id.vp_main);
        ll_main_btm = getView(R.id.ll_main_btm);
        dl_main = getView(R.id.dl_main);
        ll_home = getView(R.id.ll_home);
        hl_main = getView(R.id.hl_main);
        nv_main = getView(R.id.nv_main);
        rv_clazz = getView(R.id.rv_clazz);
        //----header
        tv_username = (TextView) nv_main.findViewById(R.id.tv_username);
        tv_intro = (TextView) nv_main.findViewById(R.id.tv_intro);
        civ_icon = (CircleImageView) nv_main.findViewById(R.id.civ_icon);
        iv_qr = (ImageView) nv_main.findViewById(R.id.iv_QR);

        //---设置缓存页(一定要设置！！！！！)
        vp_main.setOffscreenPageLimit(3);
    }

    @Override
    protected void initListener() {
        addOnClickListener(R.id.ccv_main, R.id.ccv_exe, R.id.ccv_msg, R.id.ccv_love);
        iv_qr.setOnClickListener(this);
        tv_username.setOnClickListener(this);
        civ_icon.setOnClickListener(this);

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
                    int rightColor = colors.get(position + 1);
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, leftColor, rightColor);
                    tb_main.setBackgroundColor(evaluate);
                    ll_main_btm.setBackgroundColor(evaluate);
                    ll_home.setBackgroundColor(evaluate);
                }
            }

            @Override
            public void onPageSelected(int position) {
                //--设置选定颜色
                tb_main.setBackgroundColor(colors.get(position));
                ll_main_btm.setBackgroundColor(colors.get(position));
                ll_home.setBackgroundColor(colors.get(position));
                if (position != 0) {
                    dl_main.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    tb_main.setNavigationIcon(null);
                } else {//HOME页
                    dl_main.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    tb_main.setNavigationIcon(getResources().getDrawable(R.mipmap.icon_menu));
                }
                switch (position) {
                    case 0: {
                        tb_main.setTitle("主页");
                        break;
                    }
                    case 1: {
                        tb_main.setTitle("题目练习");
                        break;
                    }
                    case 2: {
                        tb_main.setTitle("我的消息");
                        break;
                    }
                    case 3: {
                        tb_main.setTitle("喜爱的文章");
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //------底部Menu菜单隐藏回调
        hl_main.setOnMenuHideListener(new OnMenuHideListener() {
            @Override
            public void showMenu() {
                HomeBtmAnim.switchMenu(ll_main_btm, false);
            }

            @Override
            public void hideMenu() {
                HomeBtmAnim.switchMenu(ll_main_btm, true);
            }
        });

    }

    @Override
    protected void initData() {
        loadingDialog.show();
        //---设置adapter
        vp_main.setAdapter(tabsAdapter);
        homeFragment.setOnFragmentCreatedListener(new OnFragmentCreatedListener() {
            @Override
            public void onFragmentCreatedListener() {
                //--得到缓存数据
                String cache = FileUtils.getDataFromCache(MainActivity.this, "get_menu_data");
                if (!NetUtils.isConnected(MainActivity.this) && !TextUtils.isEmpty(cache)) {
                    MenuDataJson menuDataJson = new Gson().fromJson(cache, MenuDataJson.class);
                    setData(menuDataJson);
                    MyLog.e("=======get_menu_data=========onMenuBoardLoadedListener" + onMenuBoardLoadedListener);
                } else {
                    getDataFromNet();
                }
            }
        });

    }


    /**
     * 从网络获取菜单数据
     */
    private void getDataFromNet() {
        MyHttpManager.getAsyn(MyHttpUrl.GET_MENU_DATA,
                new MyHttpManager.ResultCallback<MenuDataJson>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        loadingDialog.setTips("请检查网络");
                        MySnackBar.showLongDef(ll_home, "网络请求失败").setAction("点击重试", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //重新加载网络
                                initData();
                            }
                        });
                        String cache = FileUtils.getDataFromCache(MainActivity.this, "get_menu_data");
                        if (!TextUtils.isEmpty(cache)) {
                            MenuDataJson menuDataJson = new Gson().fromJson(cache, MenuDataJson.class);
                            setData(menuDataJson);
                        }
                        MyLog.e(TAG, "网络请求错误：" + e.getMessage());
                    }

                    @Override
                    public void onResponse(final MenuDataJson menuDataJson) {
                        //---缓存数据
                        if (menuDataJson.menuData!=null&&menuDataJson.menuData.size()>0){
                            FileUtils.toCacheData(MainActivity.this, "get_menu_data", new Gson().toJson(menuDataJson));
                        }
                        setData(menuDataJson);
                    }
                });
    }

    /**
     * 设置数据
     *
     * @param menuDataJson
     */
    private void setData(final MenuDataJson menuDataJson) {
        loadingDialog.dismiss();
        if (onMenuBoardLoadedListener != null &&menuDataJson.menuData!=null &&menuDataJson.menuData.size()>0) {
            onMenuBoardLoadedListener.onMenuBoardLoaded(menuDataJson.menuData.get(0).boards);//回调
        }
        //设置类别菜单数据
        rv_clazz.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        clazzAdapter = new ClazzAdapter(MainActivity.this, menuDataJson.menuData);
        rv_clazz.setAdapter(clazzAdapter);
        clazzAdapter.setOnRvItemClickListener(new OnRvItemClickListener() {
            @Override
            public void OnItemClick(int pos) {
                if (onMenuBoardLoadedListener != null) {
                    onMenuBoardLoadedListener.onMenuBoardLoaded(menuDataJson.menuData.get(pos).boards);
                }
                if (dl_main.isDrawerOpen(GravityCompat.START)) {
                    dl_main.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    protected void afterInit() {
        ll_main_btm.setBackgroundColor(colors.get(0));
        setSupportActionBar(tb_main);
        qrCodeDialog = new QRCodeDialog(this);
        setUserInfo();
        //---注册登录成功的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(LoginActivity.LOGIN_SUCCESS);
        filter.addAction(LogicUtils.LOGOUT_ACTION);
        filter.addAction(ModifyUserInfoActivity.MODIFY_USERINRO_ACTION);
        loginCallBack = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (LoginActivity.LOGIN_SUCCESS.equals(intent.getAction())) {
                    setUserInfo();
                } else if (LogicUtils.LOGOUT_ACTION.equals(intent.getAction())) {
                    setUserInfo();
                } else if (ModifyUserInfoActivity.MODIFY_USERINRO_ACTION.equals(intent.getAction())) {
                    setUserInfo();
                }
            }
        };
        registerReceiver(loginCallBack, filter);
    }

    private void setUserInfo() {
        LoginJson.UserInfo userInfo = LogicUtils.getUserInfo(MainActivity.this);
        if (userInfo == null) {
            tv_username.setText("点击登登录");
            return;
        }
        tv_username.setText(userInfo.username);
        if (TextUtils.isEmpty(userInfo.userIntro)) {
            tv_intro.setText("快去设置你的个性签名吧");
        } else {
            tv_intro.setText(userInfo.userIntro);
        }
        MyBitmapUtils.getInstance(this)
                .display(civ_icon, MyHttpUrl.IP_PORT + userInfo.userIcon);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        tb_main.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dl_main.isDrawerOpen(GravityCompat.START)) {
                    dl_main.closeDrawer(GravityCompat.START);
                } else {
                    dl_main.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(loginCallBack);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tb_main_search: {
                MyToast.showLong(this, "搜索");
                break;
            }
            case R.id.edit_article: {//编辑文章
                if (LogicUtils.isLogin(this)) {
                    EditArticleActivity.startAction(this);
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            }
            case R.id.add_fork: {
                userInfo = LogicUtils.getUserInfo(this);
                if (userInfo == null) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, CaptureActivity.class);
                    startActivityForResult(intent, TO_CAPTURE);
                }
                break;
            }
            case R.id.tb_logout: {
                LogicUtils.logout(this);
                break;
            }
            case R.id.tb_exit_app: {
                ActivityHolder.finishedAll();
                break;
            }
        }
        return true;
    }

    private OnMenuBoardLoadedListener onMenuBoardLoadedListener;

    /**
     * 设置Menu board数据加载完成的回调
     */
    public void setOnMenuBoardLoadedListener(OnMenuBoardLoadedListener onMenuBoardLoadedListener) {
        this.onMenuBoardLoadedListener = onMenuBoardLoadedListener;
    }

    /**
     * 重置其他的Tab
     */
    private void resetOtherTabs() {
        for (int i = 0; i < tabIndicators.size(); i++) {
            tabIndicators.get(i).setIconAlpha(0);
        }
    }

    /**
     * 设备配置发生改变的时候调用
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 返回键监听
     */
    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        if (dl_main.isDrawerOpen(GravityCompat.START)) {
            dl_main.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                MyToast.showShort(this, "再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                this.finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == TO_CAPTURE) {
            Bundle bundle = data.getExtras();
            String followUserId = bundle.getString("result");
            String userId = "" + userInfo.userId;
            MyHttpManager.getAsyn
                    (MyHttpUrl.ADD_FOLLOW + "?forWhat=addFollow&userId=" + userId + "&followUserId=" + followUserId,
                            new MyHttpManager.ResultCallback<FollowJson>() {
                                @Override
                                public void onError(Request request, Exception e) {
                                    MyToast.showLong(MainActivity.this, "关注失败，检查网络！！");
                                }

                                @Override
                                public void onResponse(FollowJson followJson) {
                                    MyToast.showLong(MainActivity.this, "" + followJson.msg);
                                }
                            });
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
            case R.id.tv_username: {
                Intent intent;
                if (LogicUtils.isLogin(this)) {
                    intent = new Intent(MainActivity.this, MomentsActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                break;
            }
            case R.id.iv_QR: {
                //--判断是否登录
                final LoginJson.UserInfo userInfo = LogicUtils.getUserInfo(this);
                if (userInfo == null) {
                    MyToast.showShort(this, "请先登录啊！！");
                    break;
                }
                MyBitmapUtils.getInstance(this).display(MyHttpUrl.IP_PORT + userInfo.userIcon,
                        new ImageLoader.ImageListener() {
                            @Override
                            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                Bitmap icon = response.getBitmap();
                                Bitmap qrBm = EncodingUtils.createQRCode
                                        ("" + userInfo.userId,
                                                getResources().getDimensionPixelOffset(R.dimen.qr_dialog_width),
                                                getResources().getDimensionPixelOffset(R.dimen.qr_dialog_height),
                                                icon);
                                qrCodeDialog.iv_qr_code.setImageBitmap(qrBm);
                                qrCodeDialog.show();
                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Bitmap qrBm = EncodingUtils.createQRCode
                                        ("" + userInfo.userId,
                                                getResources().getDimensionPixelOffset(R.dimen.qr_dialog_width),
                                                getResources().getDimensionPixelOffset(R.dimen.qr_dialog_height),
                                                null);
                                qrCodeDialog.iv_qr_code.setImageBitmap(qrBm);
                                qrCodeDialog.show();
                            }
                        });
                break;
            }
            case R.id.civ_icon: {
                Intent intent;
                if (LogicUtils.isLogin(this)) {
                    intent = new Intent(MainActivity.this, MomentsActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                break;
            }
        }
    }

}
