package com.yyyu.interviewhelper.animation;

import android.view.animation.LinearInterpolator;

/**
 * 功能：反弹效果的差值器
 *
 * @author yyyu
 * @date 2016/5/30
 */
public class MyDialogInterpolatpr extends LinearInterpolator {

    private float factor;

    public MyDialogInterpolatpr() {
        this.factor = 0.15f;
    }

    @Override
    public float getInterpolation(float input) {
        return (float) (Math.pow(2, -10 * input) *
                Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
    }
}
