package com.yyyu.interviewhelper.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.animation.DepthPageTransformer;
import com.yyyu.interviewhelper.bean.ExeJson;
import com.yyyu.interviewhelper.net.MyHttpManager;
import com.yyyu.interviewhelper.net.MyHttpUrl;
import com.yyyu.interviewhelper.ui.widget.MyListView;
import com.yyyu.interviewhelper.utils.CutViewUtils;
import com.yyyu.interviewhelper.utils.ExeUtils;
import com.yyyu.interviewhelper.utils.FileUtils;
import com.yyyu.interviewhelper.utils.MyLog;
import com.yyyu.interviewhelper.utils.MySnackBar;
import com.yyyu.interviewhelper.utils.MyToast;
import com.yyyu.interviewhelper.utils.NetUtils;
import com.yyyu.interviewhelper.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 功能：题目练习界面
 *
 * @author yyyu
 * @date 2016/6/7
 */
public class ExeActivity extends BaseActivity {

    private int cateId;

    private RadioButton rb_left;
    private RadioButton rb_right;
    protected Toolbar tb_main;
    private ViewPager vp_show_exe;
    private ImageButton ib_exe_share;

    private MyPagerAdapter myPagerAdapter;
    private View mCurrentView;//ViewPager当前的itemView

    /**
     * 每页选项状态的保存
     */
    HashMap<Integer, HashMap<Integer, String>> vp_pager_con = new HashMap<>();

    /**
     * 答题后解析状态的保存
     */
    List<Integer> answered_con = new ArrayList<>();

    /**
     * 答题后结果状态的保存
     */
    HashMap<Integer, String> result_com = new HashMap<>();

    @Override
    protected void beforeInit() {
        cateId = getIntent().getIntExtra("cateId", -1);
        MyToast.showShort(this, "cateId：" + cateId);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_exe;
    }

    @Override
    protected void initView() {
        tb_main = getView(R.id.tb_main);
        rb_left = getView(R.id.rb_left);
        rb_right = getView(R.id.rb_right);
        vp_show_exe = getView(R.id.vp_show_exe);
        ib_exe_share = getView(R.id.ib_exe_share);
        //---set
        rb_left.setText("答题模式");
        rb_right.setText("学习模式");
        vp_show_exe.setPageTransformer(true, new DepthPageTransformer());
        vp_show_exe.setOffscreenPageLimit(3);
    }

    @Override
    protected void initListener() {
        addOnClickListener(R.id.rb_left , R.id.rb_right, R.id.ib_exe_share , R.id.ib_exe_save);
        tb_main.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExeActivity.this.finish();
            }
        });
    }

    public static void startAction(Activity activity, int cateId) {
        Intent intent = new Intent(activity, ExeActivity.class);
        intent.putExtra("cateId", cateId);
        activity.startActivity(intent);
    }

    @Override
    protected void initData() {
        String cache = FileUtils.getDataFromCache(this , "android_get_exes_by_cateId_"+cateId);
        if(!NetUtils.isConnected(this) && !TextUtils.isEmpty(cache)){
            setData(new Gson().fromJson(cache , ExeJson.class));
        }else{
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        MyHttpManager.getAsyn(MyHttpUrl.GET_EXES_BY_CATE_ID + "?cateId=" + cateId,
                new MyHttpManager.ResultCallback<ExeJson>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        String cache = FileUtils.getDataFromCache(ExeActivity.this , "android_get_exes_by_cateId_"+cateId);
                        if(!TextUtils.isEmpty(cache)){
                            setData(new Gson().fromJson(cache , ExeJson.class));
                        }
                    }
                    @Override
                    public void onResponse(ExeJson exeJson) {
                        if(exeJson.success){
                            //缓存数据
                            FileUtils.toCacheData(ExeActivity.this ,"android_get_exes_by_cateId_"+cateId , new Gson().toJson(exeJson));
                            setData(exeJson);
                        }
                    }
                });
    }

    private void setData(ExeJson exeJson) {
        myPagerAdapter = new MyPagerAdapter(exeJson.exes);
        vp_show_exe.setAdapter(myPagerAdapter);
    }


    /**
     * 题目显示ViewPager的适配器
     */
    public class MyPagerAdapter extends PagerAdapter {

        private List<ExeJson.Exe> exes;

        public MyPagerAdapter(List<ExeJson.Exe> exes) {
            this.exes = exes;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentView = (View) object;
        }

        @Override
        public int getCount() {
            return exes.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View view = LayoutInflater.from(ExeActivity.this).inflate(R.layout.vp_answer_item, container, false);
            TextView tv_ans_title = (TextView) view.findViewById(R.id.tv_ans_title);
            TextView tv_code = (TextView) view.findViewById(R.id.tv_code);
            final MyListView lv_answer = (MyListView) view.findViewById(R.id.lv_answer);
            final Button btn_mul_sure = (Button) view.findViewById(R.id.btn_mul_sure);
            final LinearLayout ll_analysize = (LinearLayout) view.findViewById(R.id.ll_analysize);
            final TextView tv_result = (TextView) view.findViewById(R.id.tv_result);
            TextView tv_analysize = (TextView) view.findViewById(R.id.tv_analysize);

            final ExeJson.Exe bean = exes.get(position);
            int imgId = R.mipmap.ic_launcher;
            if (bean.typeId == 1) {
                imgId = R.mipmap.exe_danxuanti_day;
            } else if (bean.typeId == 2) {
                imgId = R.mipmap.exe_duoxuanti_day;
            } else if (bean.typeId == 3) {
                imgId = R.mipmap.exe_panduanti_day;
            }
            String[] titleAreas = bean.exeTitle.split("</code>");//分割得到代码块
            tv_ans_title.setText(getSpannerString(ExeUtils.replaceBlank(titleAreas[0]), imgId));
            if (titleAreas.length > 1) {
                tv_code.setVisibility(View.VISIBLE);
                tv_code.setText(titleAreas[1]);
            }
            tv_analysize.setText(bean.exeAnalyze);

            String[] options = bean.exeOption.split("(A|B|C|D|E|F)+、");
            final List<String> lv_data = new ArrayList<String>();
            for (int i = 0; i < options.length; i++) {//--去掉空的
                if (!TextUtils.isEmpty(options[i])) {
                    lv_data.add(options[i]);
                }
            }

            //---是否显示解析(用于重用Pager时)
            if (answered_con.contains(position)) {
                ll_analysize.setVisibility(View.VISIBLE);
            }

            if (result_com.containsKey(position)) {
                tv_result.setText(result_com.get(position));
            }

            final MyLvAdapter myLvAdapter = new MyLvAdapter(lv_data, position);
            lv_answer.setAdapter(myLvAdapter);
            lv_answer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    if (vp_pager_con.containsKey(vp_show_exe.getCurrentItem())) {
                        return;
                    }//--保证只被选一次
                    answered_con.add(position);
                    HashMap<Integer, String> lv_Item_con = new HashMap<>();
                    int exeType = bean.typeId;
                    if (exeType == 1 || exeType == 3) {  //--------------如果是单选题直接判断答案是否正确
                        boolean isRight = ExeUtils.checkSimpleSlection(pos, bean.exeAnswer);
                        if (isRight) {//判断正确
                            lv_Item_con.put(pos, "right");
                            vp_pager_con.put(vp_show_exe.getCurrentItem(), lv_Item_con);
                            myLvAdapter.notifyDataSetChanged();
                            tv_result.setText("结果：恭喜你！回答正确");
                            result_com.put(position, "结果：恭喜你！回答正确");
                            /*---------  插入错误记录--------*/
                        } else {//错误
                            lv_Item_con.put(pos, "error");
                            lv_Item_con.put(ExeUtils.str2Num(bean.exeAnswer), "right");
                            vp_pager_con.put(vp_show_exe.getCurrentItem(), lv_Item_con);
                            myLvAdapter.notifyDataSetChanged();
                            tv_result.setText("结果：很遗憾！回答错误");
                            result_com.put(position, "结果：很遗憾！回答错误");
                             /*---------  插入正确误记录--------*/
                        }
                        ll_analysize.setVisibility(View.VISIBLE);
                    } else {//-----多选题
                        lv_Item_con.put(position, "checked");
                        myLvAdapter.notifyDataSetChanged();
                    }
                }
            });

            if (bean.typeId == 2) {//---为多选时
                btn_mul_sure.setVisibility(View.VISIBLE);
                btn_mul_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        answered_con.add(position);
                        //---判断多选是否正确
                        List<Integer> choice = myLvAdapter.posContainer;
                        Collections.sort(choice, new Comparator<Integer>() {
                            @Override
                            public int compare(Integer lhs, Integer rhs) {
                                if (lhs > rhs) {
                                    return 1;
                                } else if (lhs == rhs) {
                                    return 0;
                                } else {
                                    return -1;
                                }
                            }
                        });
                        if (choice.size() < 2) {
                           MyToast.showShort(ExeActivity.this , "至少选择两项");
                        } else {
                            btn_mul_sure.setVisibility(View.GONE);
                            String choiceStr = "";
                            for (Integer pos : choice) {
                                choiceStr += ("" + ExeUtils.Num2Char(pos));
                            }
                            boolean isRight = ExeUtils.checkMultipleSelection(choiceStr, bean.exeAnswer);
                            HashMap<Integer, String> lv_Item_con = new HashMap<>();
                            if (isRight) {//判断正确
                                for (Integer pos : choice) {
                                    lv_Item_con.put(pos, "right");
                                    vp_pager_con.put(vp_show_exe.getCurrentItem(), lv_Item_con);
                                }
                                myLvAdapter.notifyDataSetChanged();
                                tv_result.setText("结果：恭喜你！回答正确");
                                result_com.put(position, "结果：恭喜你！回答正确");
                               /*--------插入正确记录----------*/
                            } else {//错误
                                for (Integer pos : ExeUtils.str2NumList(bean.exeAnswer)) {
                                    lv_Item_con.put(pos, "right");
                                    vp_pager_con.put(vp_show_exe.getCurrentItem(), lv_Item_con);
                                }
                                myLvAdapter.notifyDataSetChanged();
                                tv_result.setText("结果：很遗憾！回答错误");
                                result_com.put(position, "结果：很遗憾！回答错误");
                                /*--------插入错误记录----------*/
                            }
                            ll_analysize.setVisibility(View.VISIBLE);

                        }
                    }
                });
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    /**
     * 题目选项ListView的适配器
     */
    public class MyLvAdapter extends BaseAdapter {

        private List<String> mData;

        private int vp_position;

        public List<Integer> posContainer = new ArrayList<>();

        public MyLvAdapter(List data, int vp_position) {
            this.mData = data;
            this.vp_position = vp_position;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(ExeActivity.this).inflate(R.layout.lv_answer_item, parent, false);
            TextView tv_ans_option = (TextView) view.findViewById(R.id.tv_ans_option);
            CheckBox cb_option = (CheckBox) view.findViewById(R.id.cb_option);
            tv_ans_option.setText(mData.get(position).trim());

            switch (position) {
                case 0: {
                    cb_option.setButtonDrawable(getResources().getDrawable(R.drawable.option_a_selector));
                    break;
                }
                case 1: {
                    cb_option.setButtonDrawable(getResources().getDrawable(R.drawable.option_b_selector));
                    break;
                }
                case 2: {
                    cb_option.setButtonDrawable(getResources().getDrawable(R.drawable.option_c_selector));
                    break;
                }
                case 3: {
                    cb_option.setButtonDrawable(getResources().getDrawable(R.drawable.option_d_selector));
                    break;
                }
                case 4: {
                    cb_option.setButtonDrawable(getResources().getDrawable(R.drawable.option_e_selector));
                    break;
                }
                case 5: {
                    cb_option.setButtonDrawable(getResources().getDrawable(R.drawable.option_f_selector));
                    break;
                }
                case 6: {
                    cb_option.setButtonDrawable(getResources().getDrawable(R.drawable.option_g_selector));
                    break;
                }
            }
            HashMap temp = vp_pager_con.get(vp_position);
            if (temp != null && temp.containsKey(position)) {
                cb_option.invalidate();
                if (temp.get(position).equals("right")) {
                    cb_option.setChecked(true);
                    cb_option.setButtonDrawable(getResources().getDrawable(R.mipmap.option_right));
                } else if (temp.get(position).equals("error")) {
                    cb_option.setButtonDrawable(getResources().getDrawable(R.mipmap.option_error));
                } else if (temp.get(position).equals("checked")) {
                    cb_option.setChecked(true);
                }
            }
            //--点击checkedbox
            cb_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (posContainer.contains(new Integer(position))) {
                        posContainer.remove(new Integer(position));
                    } else {
                        posContainer.add(new Integer(position));
                    }
                }
            });

            return view;
        }
    }

    /**
     * 图文混排
     */
    private SpannableString getSpannerString(String str, int imgId) {
        int end = 2;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 2; i++) {
            sb.append("　");
        }
        SpannableString ss = new SpannableString(sb + str);
        Drawable d = getResources().getDrawable(imgId);//找到你要加入的图片
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, 0, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//设置图片的摆放位置
        return ss;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_exe_share:{
                ScrollView dv = (ScrollView) mCurrentView.findViewById(R.id.msv_show_exe);
                final Bitmap bitmap = CutViewUtils.getBitmapByView(dv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileUtils.saveBitmap(ExeActivity.this , bitmap, "exe_share_temp.jpg");
                        OnekeyShare oks = new OnekeyShare();
                        //关闭sso授权
                        oks.disableSSOWhenAuthorize();
                        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                        oks.setTitle("yyyu");
                        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                        oks.setTitleUrl("http://sharesdk.cn");
                        // text是分享文本，所有平台都需要这个字段
                        oks.setText("你知道这题怎么做吗？");
                        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                        oks.setImagePath(FileUtils.savePath+"exe_share_temp.jpg");
                        // url仅在微信（包括好友和朋友圈）中使用
                        oks.setUrl("http://sharesdk.cn");
                        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                        oks.setComment("我是测试评论文本");
                        // site是分享此内容的网站名称，仅在QQ空间使用
                        oks.setSite(getString(R.string.app_name));
                        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                        oks.setSiteUrl("http://sharesdk.cn");
                        // 启动分享GUI
                        oks.show(ExeActivity.this);
                    }
                }).start();
                break;
            }
            case R.id.ib_exe_save:{
                final String imgName = TimeUtils.getSysTime()+".jpg";
                ScrollView dv = (ScrollView) mCurrentView.findViewById(R.id.msv_show_exe);
                FileUtils.saveBitmap(this , CutViewUtils.getBitmapByView(dv) ,  imgName);
                MediaScannerConnection.scanFile(this, new String[]{FileUtils.savePath+imgName}, null, null);
                MySnackBar.showLongDef(dv , "文章已保存为图片，去相册查看");
                break;
            }
        }
    }


}
