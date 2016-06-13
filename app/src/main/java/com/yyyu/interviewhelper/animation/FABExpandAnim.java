package com.yyyu.interviewhelper.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.yyyu.interviewhelper.utils.MyLog;

/**
 * 功能：FloatingActionBar展开效果
 *
 * @author yyyu
 * @date 2016/5/22
 */
public class FABExpandAnim {

    /**
     * 展开
     */
    public static void openExpand(View view) {
        AnimatorSet animSet = new AnimatorSet();
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "rotation", 0f, -150f, -135f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "rotationX", 0f, 360f);
        animSet.setDuration(500);
        animSet.playTogether(anim1, anim2);
        animSet.start();
    }

    /**
     * 关闭
     */
    public static void closeExpand(View view) {
        AnimatorSet animSet = new AnimatorSet();
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "rotation", -135f, 20f, 0f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "rotationX", 0f, 360f);
        animSet.setDuration(500);
        animSet.playTogether(anim1, anim2);
        animSet.start();
    }


    /**
     * up
     */
    public static boolean isBtmOnce = true;
    public static float btmInitY;
    public static void itemAnime(final View view, final int dis) {
        if(isBtmOnce &&dis>0){
            isBtmOnce = false;
         btmInitY = view.getY();
        }
        AnimatorSet animSet = new AnimatorSet();
        ObjectAnimator anim1;
        ObjectAnimator anim2;
        ObjectAnimator anim3;
        if (dis > 0) {
            anim1 = ObjectAnimator.ofFloat(view, "y", btmInitY, btmInitY - dis);
            anim2 = ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 1f);
            anim3 = ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 1f);
        } else {
            anim1 = ObjectAnimator.ofFloat(view, "y", view.getY(), btmInitY);
            anim2 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.0f);
            anim3 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.0f);
        }
        animSet.setDuration(500);
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animSet.playTogether(anim1, anim2, anim3);
        animSet.start();
    }

}
