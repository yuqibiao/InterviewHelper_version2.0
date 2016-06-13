package com.yyyu.interviewhelper.ui.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.adapter.LoveAdapter;
import com.yyyu.interviewhelper.animation.ArticleItemAnim;
import com.yyyu.interviewhelper.bean.LoginJson;
import com.yyyu.interviewhelper.bean.LoveArticleJson;
import com.yyyu.interviewhelper.callback.OnRvItemClickListener;
import com.yyyu.interviewhelper.callback.OnScrollHideListener;
import com.yyyu.interviewhelper.net.MyHttpManager;
import com.yyyu.interviewhelper.net.MyHttpUrl;
import com.yyyu.interviewhelper.ui.activity.ArticleShowActivity;
import com.yyyu.interviewhelper.ui.activity.LoginActivity;
import com.yyyu.interviewhelper.ui.activity.MainActivity;
import com.yyyu.interviewhelper.ui.widget.RefreshLayout;
import com.yyyu.interviewhelper.utils.LogicUtils;
import com.yyyu.interviewhelper.utils.MyLog;
import com.yyyu.interviewhelper.utils.MySnackBar;
import com.yyyu.interviewhelper.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：喜爱界面对应的fragment
 *
 * @author yyyu
 * @date 2016/5/18
 */
public class LoveFragment extends BaseFragment {

    private int pageNow = 1;
    private LoveAdapter loveAdapter;
    private List<LoveArticleJson.LoveArticle> loveArticles = new ArrayList<>();
    private static final int REFRESH = 0;
    private static final int LOAD_MORE = 1;

    private RecyclerView rv_love;
    private RefreshLayout rl_love;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_love;
    }

    @Override
    protected void initView() {
        rv_love = getView(R.id.rv_love);
        rl_love = getView(R.id.rl_love);
        //---set
        rl_love.setColorSchemeColors(//----设置下拉刷新控件的颜色
                getResources().getColor(R.color.home_main_color),
                getResources().getColor(R.color.my_pink ),
                getResources().getColor(R.color.love_main_color));
    }

    @Override
    protected void initListener() {
        rl_love.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(REFRESH);
                LoginJson.UserInfo userInfo = LogicUtils.getUserInfo(getActivity());
                if (userInfo == null) {
                    MySnackBar.showLongDef(rv_love, "你还没有登录啊！！")
                            .setAction("点击登录", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    getActivity().startActivity(intent);
                                }
                            });
                    return;
                }
            }
        });
        rl_love.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                getDataFromNet(LOAD_MORE);
            }
        });
    }


    @Override
    protected void initData() {
        getDataFromNet(REFRESH);
    }

    private void getDataFromNet(final int type) {
        LoginJson.UserInfo userInfo = LogicUtils.getUserInfo(getActivity());
        if (userInfo==null){
            return;
        }
        if (type == REFRESH) {
            pageNow = 1;
        } else if (type == LOAD_MORE) {
            pageNow++;
        }
        MyHttpManager.getAsyn(MyHttpUrl.GET_LOVE_ARTICLES + "?userId=" + userInfo.userId + "&pageNow=" + pageNow,
                new MyHttpManager.ResultCallback<LoveArticleJson>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        MyLog.e("加载喜爱文章数据出错：" + e.getMessage());
                    }
                    @Override
                    public void onResponse(LoveArticleJson loveArticleJson) {
                        if (type == REFRESH) {
                            loveArticles = loveArticleJson.loves;
                            loveAdapter = new LoveAdapter(getActivity(), loveArticles);
                            loveAdapter.setOnRvItemClickListener(new OnRvItemClickListener() {
                                @Override
                                public void OnItemClick(int pos) {
                                    ArticleShowActivity.startAction(getActivity(),
                                            loveArticles.get(pos).articleId,
                                            loveArticles.get(pos).articleIcon,
                                            loveArticles.get(pos).articleTitle,
                                            loveArticles.get(pos).articleContent);
                                }
                            });
                            rv_love.setAdapter(loveAdapter);
                            rv_love.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                            rl_love.setRefreshing(false);
                        } else if (type == LOAD_MORE) {
                            if (loveArticleJson.loves==null||loveArticleJson.loves.size()==0){
                                MyToast.showShort(getActivity() , "没有更多数据了！！");
                                rl_love.setLoadFinished();
                            }else {
                                loveArticles.addAll(loveArticleJson.loves);
                                loveAdapter.setLoveArticleDate(loveArticles);
                                loveAdapter.notifyDataSetChanged();
                                rl_love.setLoadFinished();
                            }
                        }

                    }
                });
    }


}
