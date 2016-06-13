package com.yyyu.interviewhelper.callback;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yyyu.interviewhelper.animation.HomeBtmAnim;
import com.yyyu.interviewhelper.utils.MyLog;

/**
 * 功能：RecyclerView的滚动监听
 *
 * @author yyyu
 * @date 2016/5/21
 */
public class OnScrollHideListener extends RecyclerView.OnScrollListener{

    View targetView;

    boolean isHided = false;

    public  OnScrollHideListener(View targetView){
        this.targetView = targetView;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
       if(dy>0 && !isHided){
           isHided = true;
           targetView.clearAnimation();
           targetView.setAnimation(HomeBtmAnim.hideAnim());
       }else if(dy<0 && isHided){
           isHided = false;
           targetView.clearAnimation();
           targetView.setAnimation(HomeBtmAnim.showAnim());
       }
    }
}
