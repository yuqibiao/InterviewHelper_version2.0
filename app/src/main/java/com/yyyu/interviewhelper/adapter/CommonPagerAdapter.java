package com.yyyu.interviewhelper.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 说明：ViewPager Adapter 的基类
 *
 * @author yyyu
 * @date 2016/2/29
 */
public abstract class CommonPagerAdapter<T> extends PagerAdapter {

    private Context ctx;
    private List<T> dataSource;
    private int layoutId;
    private View rootView;

    public CommonPagerAdapter(Context ctx, List<T> dataSource, int layoutId){
        this.ctx = ctx;
        this.dataSource = dataSource;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        rootView = LayoutInflater.from(ctx).inflate(layoutId , container , false);
        setItemData(dataSource.get(position));
        container.addView(rootView);
        return rootView;
    }

    /**
     * 设置ViewPager Item中的数据
     */
    public abstract void setItemData(T bean);

    /**
     * 得到Item中控件
     */
    public <T extends View> T getView(int widgetId){
        return (T) rootView.findViewById(widgetId);
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
