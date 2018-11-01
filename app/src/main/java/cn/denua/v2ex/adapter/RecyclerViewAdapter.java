package cn.denua.v2ex.adapter;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.wiget.TopicView;

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

        View view = new TopicView(context);
        view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        return new MyViewHolder(view);
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.topicView.loadDataFromTopic(topics.get(position));
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TopicView topicView;

        MyViewHolder(View itemView) {
            super(itemView);
            this.topicView = (TopicView) itemView;
        }
    }
}
