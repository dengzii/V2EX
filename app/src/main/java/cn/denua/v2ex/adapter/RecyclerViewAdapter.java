package cn.denua.v2ex.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.denua.v2ex.R;
import cn.denua.v2ex.model.Topic;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Topic> topics;

    public RecyclerViewAdapter(List<Topic> topics){
        this.topics = topics;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Topic topic = topics.get(position);
        holder.tvNickname.setText(topic.author.nickName);
        holder.tvContent.setText(topic.title);
        holder.tvRefreshTime.setText(topic.refreshTime);
        holder.tvNode.setText(topic.node.name);
        holder.tvReplay.setText(String.valueOf(topic.replay));
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView ivUserPic;
        TextView tvNickname;
        TextView tvContent;
        TextView tvReplay;
        TextView tvRefreshTime;
        TextView tvNode;

        MyViewHolder(View itemView) {
            super(itemView);

            ivUserPic = (ImageView) itemView.findViewById(R.id.iv_user_pic);
            tvNickname = (TextView)itemView.findViewById(R.id.tv_username);
            tvContent =  (TextView)itemView.findViewById(R.id.tv_content);
            tvReplay =  (TextView)itemView.findViewById(R.id.tv_replay);
            tvRefreshTime =  (TextView)itemView.findViewById(R.id.tv_latest_refresh);
            tvNode =  (TextView)itemView.findViewById(R.id.tv_node);
        }
    }
}
