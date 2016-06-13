package com.yyyu.interviewhelper.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.bean.MenuDataJson;
import com.yyyu.interviewhelper.callback.OnRvItemClickListener;

import java.util.List;

/**
 * 功能：类别菜单Adapter
 *
 * @author yyyu
 * @date 2016/5/25
 */
public class ClazzAdapter extends RecyclerView.Adapter<ClazzAdapter.ClazzHolder>{

    private Context ctx;
    private List<MenuDataJson.ClazzJson> clazzData;
    private OnRvItemClickListener onRvItemClickListener;
    public  int menuSelectedPos = 0;

    public ClazzAdapter(Context ctx , List<MenuDataJson.ClazzJson> clazzData){
        this.ctx = ctx;
        this.clazzData = clazzData;
    }

    @Override
    public ClazzHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(ctx).inflate(R.layout.rv_item_clazz , parent , false);
        return new ClazzHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClazzHolder holder, final int position) {
        if (position==menuSelectedPos){//设置背景为黑色
            holder.itemView.setBackgroundColor(Color.parseColor("#F0F0F0"));
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#FAFAFA"));
        }
        holder.tv_clazz.setText(clazzData.get(position).clazzName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (onRvItemClickListener !=null){
                   menuSelectedPos = position;
                   onRvItemClickListener.OnItemClick(position);
                   notifyDataSetChanged();
               }
            }
        });
    }

    @Override
    public int getItemCount() {
        return clazzData.size();
    }

    public void setOnRvItemClickListener(OnRvItemClickListener onRvItemClickListener){
        this.onRvItemClickListener = onRvItemClickListener;
    }

    public class ClazzHolder extends RecyclerView.ViewHolder{

        public TextView tv_clazz;
        public ImageButton ib_clazz_add;

        public ClazzHolder(View itemView) {
            super(itemView);
            tv_clazz = (TextView) itemView.findViewById(R.id.tv_clazz);
            ib_clazz_add = (ImageButton) itemView.findViewById(R.id.ib_clazz_add);
        }
    }
}
