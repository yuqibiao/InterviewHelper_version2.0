package com.yyyu.interviewhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.bean.ExeCateJson;
import com.yyyu.interviewhelper.callback.OnRvItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：题目分类的Adapter
 *
 * @author yyyu
 * @date 2016/6/7
 */
public class ExeCateAdapter extends RecyclerView.Adapter<ExeCateAdapter.ExeCateHolder>{

    private Context context;
    private List<ExeCateJson.Exe> exes;
    private OnRvItemClickListener onRvItemClickListener;
    List<Integer> mHeights = new ArrayList<>();

    public ExeCateAdapter(Context context , List<ExeCateJson.Exe> exes){
        this.context = context;
        this.exes = exes;
        for (int i=0 ; i<exes.size() ; i++){
            mHeights.add( (int) ( (context.getResources().getDimension(R.dimen.exe_rv_item_height) ) *(Math.random()+1) ));
        }
    }


    @Override
    public ExeCateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_category_item , parent , false);
        return new ExeCateHolder(view);
    }

    @Override
    public void onBindViewHolder(ExeCateHolder holder, final int position) {
        ViewGroup.LayoutParams lp = holder.tv_category.getLayoutParams();
        lp.height = mHeights.get(position);
        holder.tv_category.setLayoutParams(lp);
        holder.tv_category.setText(exes.get(position).cateName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRvItemClickListener.OnItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exes.size();
    }

    public void setOnRvItemClickListener(OnRvItemClickListener onRvItemClickListener){
        this.onRvItemClickListener = onRvItemClickListener;
    }

    public class ExeCateHolder extends RecyclerView.ViewHolder{

        private TextView tv_category;

        public ExeCateHolder(View itemView) {
            super(itemView);
            tv_category = (TextView) itemView.findViewById(R.id.tv_category);
        }
    }

}
