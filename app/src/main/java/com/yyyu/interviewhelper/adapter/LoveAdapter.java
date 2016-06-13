package com.yyyu.interviewhelper.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.animation.ArticleItemAnim;
import com.yyyu.interviewhelper.bean.LoveArticleJson;
import com.yyyu.interviewhelper.callback.OnRvItemClickListener;
import com.yyyu.interviewhelper.net.MyBitmapUtils;
import com.yyyu.interviewhelper.net.MyHttpUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器
 */
public class LoveAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Integer> mHeights = new ArrayList<>();
    private List<LoveArticleJson.LoveArticle> mLoveArticles;
    private OnRvItemClickListener onRvItemClickListener;

    public LoveAdapter(Context context, List<LoveArticleJson.LoveArticle> loveArticles) {
        this.mContext = context;
        this.mLoveArticles = loveArticles;
        for (int i = 0; i < 10; i++) {
            mHeights.add((int) ((mContext.getResources().getDimension(R.dimen.love_rv_item_height)) * (Math.random() + 1)));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item_love_article, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //item动画
        ArticleItemAnim.getInstance(mContext).startAnimator(holder.itemView, position);
        final ContentViewHolder mHolder = (ContentViewHolder) holder;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mHolder.iv_love_item_icon.getLayoutParams();
        lp.height = mHeights.get(position);
        mHolder.iv_love_item_icon.setLayoutParams(lp);
        mHolder.tv_love_item_title.setText(mLoveArticles.get(position).articleTitle);
        MyBitmapUtils.getInstance(mContext)
                .display(mHolder.iv_love_item_icon,
                        MyHttpUrl.IP_PORT + mLoveArticles.get(position).articleIcon,
                        new ImageLoader.ImageListener() {
                            @Override
                            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                if (response.getBitmap() != null) {
                                    mHolder.iv_love_item_icon.setImageBitmap(response.getBitmap());
                                    //获取颜色
                                    Palette.from(response.getBitmap()).generate(new Palette.PaletteAsyncListener() {
                                        @Override
                                        public void onGenerated(Palette palette) {
                                            Palette.Swatch vibrant = palette.getLightVibrantSwatch();//有活力
                                            if (vibrant != null) {
                                              //  int color1 = vibrant.getBodyTextColor();//内容颜色
                                                int color2 = vibrant.getTitleTextColor();//标题颜色
                                                int color3 = vibrant.getRgb();//rgb颜色
                                                mHolder.tv_love_item_title.setBackgroundColor(color3);
                                                mHolder.tv_love_item_title.setTextColor(color2);
                                            }
                                        }
                                    });
                                } else {
                                    mHolder.iv_love_item_icon.setImageResource(R.mipmap.img_df);
                                }
                            }
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                mHolder.iv_love_item_icon.setImageResource(R.mipmap.img_err);
                            }
                        });
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRvItemClickListener.OnItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLoveArticles.size();
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_love_item_icon;
        public TextView tv_love_item_title;

        public ContentViewHolder(View itemView) {
            super(itemView);
            iv_love_item_icon = (ImageView) itemView.findViewById(R.id.iv_love_item_icon);
            tv_love_item_title = (TextView) itemView.findViewById(R.id.tv_love_item_title);
        }
    }


    public void setOnRvItemClickListener(OnRvItemClickListener onRvItemClickListener ){
        this.onRvItemClickListener = onRvItemClickListener;
    }

    public void setLoveArticleDate(List<LoveArticleJson.LoveArticle> loveArticles){
        this.mLoveArticles = loveArticles;
    }

}