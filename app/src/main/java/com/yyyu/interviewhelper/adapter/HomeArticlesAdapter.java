package com.yyyu.interviewhelper.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.animation.ArticleItemAnim;
import com.yyyu.interviewhelper.bean.Article;
import com.yyyu.interviewhelper.bean.ArticleDataJson;
import com.yyyu.interviewhelper.callback.OnRvItemClickListener;
import com.yyyu.interviewhelper.net.MyHttpUrl;
import com.yyyu.interviewhelper.net.MyBitmapUtils;
import com.yyyu.interviewhelper.ui.activity.ArticleShowActivity;
import com.yyyu.interviewhelper.ui.widget.ViewpagerLooper;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：文章RecyclerView对应的Adapter
 *
 * @author yyyu
 * @date 2016/5/26
 */
public class HomeArticlesAdapter extends RecyclerView.Adapter {

    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_CONTENT = 1;
    private int headerSize = 1;

    private OnRvItemClickListener onRvItemClickListener;
    private ArticleDataJson articleDataJson;
    private Context ctx;
    List<Article> articleData = new ArrayList<>();

    public HomeArticlesAdapter(Context ctx, ArticleDataJson articleDataJson) {
        this.ctx = ctx;
        this.articleDataJson = articleDataJson;
        if (articleDataJson == null || articleDataJson.topArticle == null || articleDataJson.topArticle.size() == 0) {
            headerSize = 0;
        }
        articleData = articleDataJson.articleByPage;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < headerSize) {//---------Header
            return ITEM_TYPE_HEADER;
        } else {//-------Content
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            View itemView = LayoutInflater.from(ctx).inflate(R.layout.rv_item_home_article_header, parent, false);
            HeaderViewHolder holder = new HeaderViewHolder(itemView);
            return holder;
        } else if (viewType == ITEM_TYPE_CONTENT) {
            View itemView = LayoutInflater.from(ctx).inflate(R.layout.rv_item_home_article, parent, false);
            ContentViewHolder holder = new ContentViewHolder(itemView);
            return holder;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //item动画
        ArticleItemAnim.getInstance(ctx).startAnimator(holder.itemView, position);
        if (holder instanceof HeaderViewHolder) {
            final HeaderViewHolder hHolder = (HeaderViewHolder) holder;
            hHolder.vpl_home.setViewPager(hHolder.vp_home_head);
            hHolder.vpl_home.addIndicator(articleDataJson.topArticle.size());
            hHolder.tv_home_header_title.setText(articleDataJson.topArticle.get(0).articleTitle);
            hHolder.vp_home_head.setAdapter(new CommonPagerAdapter<Article>(ctx,
                    articleDataJson.topArticle, R.layout.vp_item_home_header) {
                @Override
                public void setItemData(final Article bean) {
                    ImageView iv_header_icon = getView(R.id.iv_header_icon);
                    MyBitmapUtils.getInstance(ctx).display(iv_header_icon, MyHttpUrl.IP_PORT + bean.articleIcon);
                    iv_header_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArticleShowActivity.startAction(ctx, bean.articleId, bean.articleIcon,
                                    bean.articleTitle, bean.articleContent);
                        }
                    });
                }
            });
            hHolder.vp_home_head.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    hHolder.tv_home_header_title.setText(articleDataJson.topArticle.get(position).articleTitle);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else if (holder instanceof ContentViewHolder) {
            int contentPos = position - headerSize;
            ContentViewHolder cHolder = (ContentViewHolder) holder;
            //Item点击事件
            cHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRvItemClickListener != null) {
                        onRvItemClickListener.OnItemClick(position - headerSize);
                    }
                }
            });
            Article article = articleDataJson.articleByPage.get(contentPos);
            cHolder.tv_article_item_title.setText(article.articleTitle);
            MyBitmapUtils.getInstance(ctx).display(cHolder.iv_article_item_icon, MyHttpUrl.IP_PORT + article.articleIcon);
        }
    }

    @Override
    public int getItemCount() {
        return articleData.size() + headerSize;
    }

    /**
     * 设置Item点击事件
     */
    public void setOnRvItemClickListener(OnRvItemClickListener onRvItemClickListener) {
        this.onRvItemClickListener = onRvItemClickListener;
    }

    /**
     * 设置数据源的方法
     */
    public void setArticleData(List<Article> articleData) {
        this.articleData = articleData;
    }


    public class ContentViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_article_item_icon;
        public TextView tv_article_item_title;

        public ContentViewHolder(View itemView) {
            super(itemView);
            iv_article_item_icon = (ImageView) itemView.findViewById(R.id.iv_article_item_icon);
            tv_article_item_title = (TextView) itemView.findViewById(R.id.tv_article_item_title);
        }

    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public ViewPager vp_home_head;
        public ViewpagerLooper vpl_home;
        public TextView tv_home_header_title;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            vp_home_head = (ViewPager) itemView.findViewById(R.id.vp_home_head);
            vpl_home = (ViewpagerLooper) itemView.findViewById(R.id.vpl_home);
            tv_home_header_title = (TextView) itemView.findViewById(R.id.tv_home_header_title);

        }
    }

}
