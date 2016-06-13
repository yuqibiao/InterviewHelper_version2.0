package com.yyyu.interviewhelper.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.yyyu.interviewhelper.animation.FABExpandAnim;
import com.yyyu.interviewhelper.utils.DimensChange;

/**
 * 功能：自定义Floating菜单
 *
 * @author yyyu
 * @date 2016/5/23
 */
public class FloatingMenuLayout extends RelativeLayout {

    /**
     * 间距
     */
    private int space;

    public FloatingMenuLayout(Context context) {
        this(context, null);
    }

    public FloatingMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        space = DimensChange.dp2px(context, 80);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int cCount = getChildCount();
        int width = 0;
        int height = 0;
        for (int i = 0; i < cCount; i++) {
            View cView = getChildAt(i);
            int cHeight = (i + 1) * space;//最后一个View的移动距离
            width = Math.max(width, cView.getWidth());
            height = cHeight + cView.getHeight();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int cCount = getChildCount();
        int lHight = getMeasuredHeight();
        for (int i = 0; i < cCount; i++) {
            View cView = getChildAt(i);
            cView.setVisibility(INVISIBLE);
            cView.layout(0, lHight - cView.getWidth(), cView.getWidth(), lHight);
        }

    }


    private boolean isOpen = false;

    /**
     * 切换menu
     */
    public void switchMenu() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View cView = getChildAt(i);
            cView.setVisibility(VISIBLE);
            int cHeight = (i + 1) * space;
            if (!isOpen) {//打开
                FABExpandAnim.itemAnime(cView, cHeight);
                if (cCount == i + 1) {
                    isOpen = true;
                }
            } else {//关闭
                FABExpandAnim.itemAnime(cView, -cHeight);
                if (cCount == i + 1) {
                    isOpen = false;
                }
            }
        }
    }


}
