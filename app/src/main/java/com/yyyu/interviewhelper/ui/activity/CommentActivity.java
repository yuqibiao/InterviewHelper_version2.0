package com.yyyu.interviewhelper.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.squareup.okhttp.Request;
import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.adapter.CommentAdapter;
import com.yyyu.interviewhelper.bean.CommJson;
import com.yyyu.interviewhelper.bean.LoginJson;
import com.yyyu.interviewhelper.net.MyHttpManager;
import com.yyyu.interviewhelper.net.MyHttpUrl;
import com.yyyu.interviewhelper.ui.widget.RefreshLayout;
import com.yyyu.interviewhelper.utils.LogicUtils;
import com.yyyu.interviewhelper.utils.MyLog;
import com.yyyu.interviewhelper.utils.MySnackBar;
import com.yyyu.interviewhelper.utils.MyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能：文章评论界面
 *
 * @author yyyu
 * @date 2016/6/5
 */
public class CommentActivity extends BaseActivity {

    private Toolbar tb_comment;
    private EditText et_comm;
    private ImageButton ib_send_comment;
    private RecyclerView rv_comment;
    private RefreshLayout rl_comment;
    private int articleId;
    private int pageNow = 1;
    private List<CommJson.Comm> comms = new ArrayList<>();
    private static final  int REFREH=0 , LOAD_MORE=1;
    private CommentAdapter commentAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_comment;
    }

    @Override
    protected void beforeInit() {
        articleId = getIntent().getIntExtra("articleId", -1);
        if (articleId == -1) {
            throw new UnsupportedOperationException("传articleId和userId啊！！");
        }
    }

    @Override
    protected void initView() {
        tb_comment = getView(R.id.tb_comment);
        et_comm = getView(R.id.et_comm);
        ib_send_comment = getView(R.id.ib_send_comment);
        rv_comment = getView(R.id.rv_comment);
        rl_comment = getView(R.id.rl_comment);
        //---set
        rl_comment.setColorSchemeColors(//----设置下拉刷新控件的颜色
                getResources().getColor(R.color.home_main_color),
                getResources().getColor(R.color.my_pink ),
                getResources().getColor(R.color.love_main_color));
    }

    @Override
    protected void initListener() {
        tb_comment.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentActivity.this.finish();
            }
        });

        rl_comment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCommFormNet(REFREH);
            }
        });
        rl_comment.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                getCommFormNet(LOAD_MORE);
                MyLog.e("load more==pageNow="+pageNow);
            }
        });

        ib_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginJson.UserInfo userInfo = LogicUtils.getUserInfo(CommentActivity.this);
                if (userInfo == null) {
                    MyToast.showShort(CommentActivity.this, "你还没有登录啊 ！！");
                    return;
                }
                String commContent = et_comm.getText().toString();
                if (TextUtils.isEmpty(commContent)) {
                    MyToast.showShort(CommentActivity.this, "评论不能为空啊 ！！");
                    return;
                }
                //---发送评论
                Map<String, String> params = new HashMap<>();
                params.put("forWhat", "addComment");
                params.put("articleId", "" + articleId);
                params.put("userId", "" + userInfo.userId);
                params.put("commConet", commContent);
                MyHttpManager.postAsyn(MyHttpUrl.SEND_COMM, new MyHttpManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        MyLog.e("==评论失败！！" + e.getMessage());
                        MySnackBar.showLongDef(rl_comment,"评论失败！！请检查你的网络！！");
                    }

                    @Override
                    public void onResponse(String response) {
                        MySnackBar.showLongDef(rv_comment , "评论成功！！！");
                    }
                }, params);
            }
        });
    }

    @Override
    protected void initData() {
        getCommFormNet(REFREH);
    }

    private void getCommFormNet(final int type) {
        if (type==REFREH){
            pageNow=1;
        }else if (type==LOAD_MORE) {
            pageNow++;
        }
        String url = MyHttpUrl.GET_COMM + "?articleId=" + articleId + "&pageNow=" + pageNow;
        MyLog.e("url--------"+url);
        MyHttpManager.getAsyn(url , new MyHttpManager.ResultCallback<CommJson>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        MyLog.e("获取评论数据失败！！！"+e.getMessage());
                    }
                    @Override
                    public void onResponse(CommJson commJson) {
                        if (type==REFREH){
                            comms = commJson.comms;
                            commentAdapter = new CommentAdapter(CommentActivity.this , comms);
                            rv_comment.setAdapter(commentAdapter);
                            rv_comment.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
                            rl_comment.setRefreshing(false);
                        }else if (type==LOAD_MORE){
                            if (commJson.comms==null || commJson.comms.size()==0){
                                MyToast.showShort(CommentActivity.this , "没有更多数据了！！");
                                rl_comment.setLoadFinished();
                            }else{
                                comms.addAll(commJson.comms);
                                commentAdapter.setData(comms);
                                commentAdapter.notifyDataSetChanged();
                                rl_comment.setLoadFinished();
                            }
                        }
                    }
                });
    }

    public static void startAction(Activity activity, int articleId) {
        Intent intent = new Intent(activity, CommentActivity.class);
        intent.putExtra("articleId", articleId);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
