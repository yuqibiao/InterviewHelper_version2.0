package com.yyyu.interviewhelper.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import com.yyyu.interviewhelper.utils.MyLog;

/**
 * 功能：Home页底部Tab菜单的Animation
 *
 * @author yyyu
 * @date 2016/5/20
 */
public class HomeBtmAnim {

    public static final int DURATION= 500;

    public static boolean isOnce = true;

    public static float init_y;

    public static  void switchMenu(View target , boolean toHide){
        if(isOnce){
            init_y = target.getY();
            isOnce = false;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(DURATION);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.cancel();
        if(toHide ){//隐藏
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(target , "y" , init_y ,init_y+ target.getMeasuredHeight());
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(target , "alpha" , 1f ,0.8f);
            animatorSet.playTogether(animator1 ,animator2);
            animatorSet.start();
        }else{//显示
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(target , "y" , init_y+target.getMeasuredHeight(), init_y);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(target , "alpha" , 0.5f ,8f);
            animatorSet.playTogether(animator1 ,animator2);
            animatorSet.start();
        }
    }

    /**
     * 弃用
     */
    public static AnimationSet  hideAnim(){

        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,1f
        );
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        translateAnimation.setDuration(DURATION);
        animationSet.addAnimation(translateAnimation);
        animationSet.setFillAfter(true);
        return animationSet;
    }

    /**
     * 弃用
     */
    public static AnimationSet showAnim(){
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,1f,
                Animation.RELATIVE_TO_SELF,0f
        );
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        translateAnimation.setDuration(DURATION);
        animationSet.addAnimation(translateAnimation);
        animationSet.setFillAfter(true);
        return animationSet;
    }

}
