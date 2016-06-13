package com.yyyu.interviewhelper.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.adapter.HomeArticlesAdapter;
import com.yyyu.interviewhelper.bean.Article;
import com.yyyu.interviewhelper.bean.ArticleDataJson;
import com.yyyu.interviewhelper.callback.OnNetDataLoadedListener;
import com.yyyu.interviewhelper.callback.OnRvItemClickListener;
import com.yyyu.interviewhelper.net.MyHttpUrl;
import com.yyyu.interviewhelper.ui.activity.ArticleShowActivity;
import com.yyyu.interviewhelper.ui.widget.RefreshLayout;
import com.yyyu.interviewhelper.utils.FileUtils;
import com.yyyu.interviewhelper.utils.MyLog;
import com.yyyu.interviewhelper.net.MyHttpManager;
import com.yyyu.interviewhelper.utils.MySnackBar;
import com.yyyu.interviewhelper.utils.NetUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 功能：主页Viewpager中每一项的fragment界面
 *
 * @author yyyu
 * @date 2016/5/20
 */
public class HomeItemFragment extends BaseFragment implements View.OnTouchListener {

    private int pagerNow = 1;
    private int boardId;
    private HomeArticlesAdapter homeArticlesAdapter;
    List<Article> articleData;

    private RecyclerView rv_article;
    private RefreshLayout rf_home;

    public HomeItemFragment() {

    }

    /**
     * Fragment要这样传参！！！！
     */
    public static HomeItemFragment getInstance(int boardId) {
        HomeItemFragment homeItemFragment = new HomeItemFragment();
        homeItemFragment.boardId = boardId;
        return homeItemFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_fragment_home;
    }

    @Override
    protected void beforeInit() {

    }

    @Override
    protected void initView() {
        rv_article = getView(R.id.rv_article);
        rf_home = getView(R.id.rf_home);
        rf_home.setColorSchemeColors(//----设置下拉刷新控件的颜色
                getResources().getColor(R.color.home_main_color),
                getResources().getColor(R.color.my_pink),
                getResources().getColor(R.color.love_main_color));
    }

    @Override
    protected void initListener() {

        rf_home.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(REFRESH, new OnNetDataLoadedListener() {
                    @Override
                    public void onGetDataFinished() {
                        rf_home.setRefreshing(false);
                    }
                });
            }
        });
        rf_home.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                MyLog.e("加载更多=====================");
                getDataFromNet(LOAD_MORE, new OnNetDataLoadedListener() {
                    @Override
                    public void onGetDataFinished() {
                        rf_home.setLoadFinished();
                    }
                });
            }
        });

    }

    @Override
    protected void initData() {
        rv_article.setLayoutManager(new LinearLayoutManager(getActivity()));
        getDataFromNet(REFRESH, null);
    }


    /**
     * 刷新数据
     */
    private static final int REFRESH = 0;
    private static final int LOAD_MORE = 1;

    private void getDataFromNet(final int type, final OnNetDataLoadedListener onNetDataLoadedListener) {
        if (type == REFRESH) {
            pagerNow = 1;
            //--从缓存中取数据
            String cache = FileUtils.getDataFromCache(getActivity(),
                    "android_articleData_boardId_" + boardId + "pagerNow_" + pagerNow);
            if (!NetUtils.isConnected(getActivity()) && !TextUtils.isEmpty(cache)) {
                setRefreshData(new Gson().fromJson(cache, ArticleDataJson.class));
            }
        } else if (type == LOAD_MORE) {
            pagerNow++;
            String cache = FileUtils.getDataFromCache(getActivity(),
                    "android_articleData_boardId_" + boardId + "pagerNow_" + pagerNow);
            if (!NetUtils.isConnected(getActivity()) && !TextUtils.isEmpty(cache)) {
                setLoadMoreData(new Gson().fromJson(cache, ArticleDataJson.class));
            }
        }
        MyHttpManager.getAsyn(MyHttpUrl.GET_ARTICLES_BY_BOARD_ID +
                        "?pageNow=" + pagerNow + "&boardId=" + boardId,
                new MyHttpManager.ResultCallback<ArticleDataJson>() {
                    @Override
                    public void onError(Request request, Exception e){
                        rf_home.setLoadFinished();
                        rf_home.setRefreshing(false);
                        String cache = FileUtils.getDataFromCache(getActivity(),
                                "android_articleData_boardId_" + boardId + "pagerNow_" + pagerNow);
                        if (!TextUtils.isEmpty(cache)) {
                            ArticleDataJson articleDataJson = new Gson().fromJson(cache, ArticleDataJson.class);
                            if (type == REFRESH) {
                                setRefreshData(articleDataJson);
                            } else if (type == LOAD_MORE) {
                                setLoadMoreData(articleDataJson);
                            }
                        }

                    }

                    @Override
                    public void onResponse(ArticleDataJson articleDataJson) {
                        if (articleDataJson.articleByPage!=null&&articleDataJson.articleByPage.size()>0){
                            //---缓存数据
                            FileUtils.toCacheData(getActivity(),
                                    "android_articleData_boardId_" + boardId + "pagerNow_" + pagerNow,
                                    new Gson().toJson(articleDataJson));
                        }
                        if (onNetDataLoadedListener != null) {
                            onNetDataLoadedListener.onGetDataFinished();
                        }
                        if (type == REFRESH) {
                            setRefreshData(articleDataJson);
                        } else if (type == LOAD_MORE) {
                            setLoadMoreData(articleDataJson);
                        }
                        rf_home.setLoadFinished();
                        rf_home.setRefreshing(false);
                    }
                });
    }

    private void setLoadMoreData(ArticleDataJson articleDataJson) {
        if (homeArticlesAdapter == null && articleData == null) {
            throw new UnsupportedOperationException("为什么你要先load more 不科学！！！");
        } else {
            if (articleDataJson == null || articleDataJson.articleByPage == null) {
                MySnackBar.showShortDef(rv_article, "没有更多数据了");
            } else {
                List<Article> newArticles = articleDataJson.articleByPage;
                articleData.addAll(newArticles);
                homeArticlesAdapter.setArticleData(articleData);
                homeArticlesAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setRefreshData(ArticleDataJson articleDataJson) {
        articleData = articleDataJson.articleByPage;
        homeArticlesAdapter = new HomeArticlesAdapter(getActivity(), articleDataJson);
        rv_article.setAdapter(homeArticlesAdapter);
        //--Item点击事件
        homeArticlesAdapter.setOnRvItemClickListener(new OnRvItemClickListener() {
            @Override
            public void OnItemClick(int pos) {
                ArticleShowActivity.startAction(getActivity(),
                        articleData.get(pos).articleId,
                        articleData.get(pos).articleIcon,
                        articleData.get(pos).articleTitle,
                        articleData.get(pos).articleContent);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                break;
            }
        }
        return false;
    }


}
