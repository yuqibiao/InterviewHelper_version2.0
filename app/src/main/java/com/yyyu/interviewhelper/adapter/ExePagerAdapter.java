package com.yyyu.interviewhelper.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.yyyu.interviewhelper.bean.ExeCateJson;

import java.util.List;

/**
 * 功能：题目显示ViewPager的Adapter
 *
 * @author yyyu
 * @date 2016/6/7
 */
public class ExePagerAdapter extends PagerAdapter{

    private Context context;
    private List<ExeCateJson.Exe> exes;

    public ExePagerAdapter(Context context , List<ExeCateJson.Exe> exes){
        this.context = context;
        this.exes = exes;
    }


    @Override
    public int getCount() {
        return exes.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
