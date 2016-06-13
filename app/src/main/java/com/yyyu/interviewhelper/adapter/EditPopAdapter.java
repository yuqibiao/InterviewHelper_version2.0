package com.yyyu.interviewhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.bean.MenuDataJson;
import com.yyyu.interviewhelper.callback.OnRvItemClickListener;

import java.util.List;

/**
 * 功能:文章编辑界面Pop窗口对应的Adapter
 *
 * @author yyyu
 * @date 2016/6/1
 */
public class EditPopAdapter<T> extends RecyclerView.Adapter<EditPopAdapter.MyHolder> {

    private OnRvItemClickListener onRvItemClickListener;
    private Context context;
    private List<T> data;

    public EditPopAdapter(Context context, List<T> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public EditPopAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.rv_pop_item, parent, false));
    }

    @Override
    public void onBindViewHolder(EditPopAdapter.MyHolder holder, final int position) {
        Object bean = data.get(position);
        if (bean instanceof MenuDataJson.ClazzJson) {
            holder.tv_pop_item.setText(((MenuDataJson.ClazzJson) bean).clazzName);
        } else if (bean instanceof MenuDataJson.ClazzJson.BoardJson) {
            holder.tv_pop_item.setText(((MenuDataJson.ClazzJson.BoardJson) bean).boardName);
        }else if(bean instanceof String ){
            holder.tv_pop_item.setText(""+bean);
        }
        holder.tv_pop_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRvItemClickListener.OnItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_pop_item;

        public MyHolder(View itemView) {
            super(itemView);
            tv_pop_item = (TextView) itemView.findViewById(R.id.tv_pop_item);
        }
    }

    public void  setOnRvClickListener(OnRvItemClickListener onRvItemClickListener){
        this.onRvItemClickListener = onRvItemClickListener;
    }

}
