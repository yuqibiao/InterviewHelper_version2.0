package com.yyyu.interviewhelper.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.yyyu.interviewhelper.callback.OnMenuHideListener;
import com.yyyu.interviewhelper.utils.DimensChange;

/**
 * 功能：自定义隐藏底部菜单布局
 *
 * @author yyyu
 * @date 2016/5/22
 */
public class HideMenuLayout extends FrameLayout{

    private OnMenuHideListener onMenuHideListener;

    public HideMenuLayout(Context context) {
        this(context , null);
    }

    public HideMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        min_dis = DimensChange.dp2px(context,25);
    }

    private float down_y;

    private float move_y;

    private   float min_dis ;

    private boolean isHided = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case  MotionEvent.ACTION_DOWN:{
                down_y = ev.getRawY();
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                break;
            }
            case MotionEvent.ACTION_UP:{
                move_y = ev.getRawY() - down_y;
                if(move_y > min_dis && isHided){//显示
                    isHided = false;
                    onMenuHideListener.showMenu();
                }else if(move_y <-min_dis && !isHided){//隐藏
                    isHided = true;
                    onMenuHideListener.hideMenu();
                }
                break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setOnMenuHideListener(OnMenuHideListener onMenuHideListener){
        this.onMenuHideListener = onMenuHideListener;
    }

}
