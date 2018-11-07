/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import cn.denua.v2ex.model.Reply;
import cn.denua.v2ex.wiget.ReplyView;

/*
 * reply adapter
 *
 * @author denua
 * @date 2018/11/07 21
 */
public class ReplyRecyclerViewAdapter extends RecyclerView.Adapter<ReplyRecyclerViewAdapter.MyViewHolder> {

    private List<Reply> replies;
    private Context context;

    public ReplyRecyclerViewAdapter(Context context, List<Reply> replies){

        this.context = context;
        this.replies = replies;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ReplyView replyView = new ReplyView(parent.getContext());
        replyView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        return new MyViewHolder(replyView);
    }

    public void setReplies(List<Reply> replies){
        this.replies = replies;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.replyView.setReply(replies.get(position));
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ReplyView replyView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.replyView = (ReplyView) itemView;
        }
    }
}
