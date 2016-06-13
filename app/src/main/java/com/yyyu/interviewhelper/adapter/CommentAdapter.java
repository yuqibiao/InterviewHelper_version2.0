package com.yyyu.interviewhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.bean.CommJson;
import com.yyyu.interviewhelper.net.MyBitmapUtils;
import com.yyyu.interviewhelper.net.MyHttpUrl;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 功能：评论Adapter
 * @author yyyu
 * @date 2016/6/5
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommHolder>{

    private Context mContext;
    private List<CommJson.Comm> comms;

    public CommentAdapter(Context context , List<CommJson.Comm> comms){
        this.mContext = context;
        this.comms = comms;
    }

    @Override
    public CommHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.rv_item_commnet  , parent , false);
        return new CommHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommHolder holder, int position) {
        holder.tv_user_name.setText(comms.get(position).username);
        holder.tv_comm_time.setText(comms.get(position).commTime);
        holder.tv_comm_context.setText(comms.get(position).commContent);
        MyBitmapUtils.getInstance(mContext).
                display(holder.comm_user_icon ,
                        MyHttpUrl.IP_PORT+comms.get(position).userIcon);
    }

    @Override
    public int getItemCount() {
        return comms.size();
    }

    public void setData(List<CommJson.Comm> comms){
        this.comms =comms;
    }

    public class CommHolder extends RecyclerView.ViewHolder{
        private TextView tv_user_name , tv_comm_time , tv_comm_context;
        private CircleImageView comm_user_icon ;
        public CommHolder(View itemView) {
            super(itemView);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
            tv_comm_time = (TextView) itemView.findViewById(R.id.tv_comm_time);
            tv_comm_context = (TextView) itemView.findViewById(R.id.tv_comm_context);
            comm_user_icon = (CircleImageView) itemView.findViewById(R.id.comm_user_icon);
        }
    }

}
