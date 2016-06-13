package com.yyyu.interviewhelper.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.yyyu.interviewhelper.R;

/**
 * 功能：文章Item的动画
 *
 * @author yyyu
 * @date 2016/5/22
 */
public class ArticleItemAnim {

    private  int lastPos = -1;

    private Context ctx;

    private static ArticleItemAnim instance;

    private ArticleItemAnim(Context ctx){
        this.ctx = ctx;
    }

    public static synchronized ArticleItemAnim getInstance(Context ctx){
        while (instance ==null){
            instance = new ArticleItemAnim(ctx);
        }
        return instance;
    }

    public void startAnimator(View view, int position) {
        if (position > lastPos) {
            view.startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.item_bottom_in));
        }
        lastPos = position;
    }

}
