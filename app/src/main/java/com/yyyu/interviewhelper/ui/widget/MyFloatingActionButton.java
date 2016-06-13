package com.yyyu.interviewhelper.ui.widget;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

/**
 * 功能：自定义FloatingActionButton
 *          主要是为了和FloatingMenuLayout关联
 *
 * @author yyyu
 * @date 2016/5/28
 */
public class MyFloatingActionButton extends FloatingActionButton{

    /**
     * 关联的FloatingMenuLayout
     */
    private FloatingMenuLayout floatingMenu;
    /**
     * 关联的背景界面
     */
    private View mask;
    /**
     * 悬浮菜单是否展开
     */
    private boolean isOpen = false;


    public MyFloatingActionButton(Context context) {
        this(context ,null);
    }

    public MyFloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public MyFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public View getMask() {
        return mask;
    }

    public void setMask(View mask) {
        this.mask = mask;
    }


    public FloatingMenuLayout getFloatingMenu() {
        return floatingMenu;
    }

    public void setFloatingMenu(FloatingMenuLayout floatingMenu) {
        this.floatingMenu = floatingMenu;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }


}
