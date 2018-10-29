package cn.denua.v2ex.adapter;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;

import java.util.List;

import cn.denua.v2ex.R;
import cn.denua.v2ex.model.Topic;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Topic> topics;
    private Context context;

    public RecyclerViewAdapter(Context context, List<Topic> topics){
        this.topics = topics;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);

        return new MyViewHolder(view);
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Topic topic = topics.get(position);

        holder.tvNickname.setText(topic.getMember().getUsername());
        holder.tvContent.setText(topic.getTitle());

        String lastTouched = TimeUtils.getFitTimeSpanByNow(topic.getLast_touched()*1000, 4);
        String userPicUrl = topic.getMember().getAvatar_large();
        lastTouched = lastTouched.startsWith("-")?lastTouched.substring(1):lastTouched;

        holder.tvRefreshTime.setText(lastTouched);
        holder.tvNode.setText(topic.getNode().getName());
        holder.tvReplay.setText(String.valueOf(topic.getReplies()));

        Glide.with(context).load(userPicUrl).into(holder.ivUserPic);
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
