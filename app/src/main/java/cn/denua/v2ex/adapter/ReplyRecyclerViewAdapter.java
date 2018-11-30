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
import android.widget.LinearLayout;

import java.util.List;
import java.util.logging.Logger;

import cn.denua.v2ex.model.Reply;
import cn.denua.v2ex.widget.ReplyView;

/*
 * reply adapter
 *
 * @author denua
 * @date 2018/11/07 21
 */
public class ReplyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int HEADER = 1;
    private final int ITEM = 0;
    private final int FOOTER = 2;
    private List<Reply> replies;
    private Context context;
    private FrameLayout.LayoutParams mLayoutParams;

    private ViewGroup mHeaderViewGroup;

    public ReplyRecyclerViewAdapter(Context context, List<Reply> replies){

        this.context = context;
        this.replies = replies;
        this.mLayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    public void setHeaderView(ViewGroup view){

        this.mHeaderViewGroup = view;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == HEADER){
            return new OtherViewHolder(mHeaderViewGroup);
        }
        ReplyView replyView = new ReplyView(parent.getContext());
        replyView.setLayoutParams(mLayoutParams);
        return new MyViewHolder(replyView);
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0 && mHeaderViewGroup != null){
            return HEADER;
        }else{
            return ITEM;
        }
    }

    public void setReplies(List<Reply> replies){
        this.replies = replies;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder){
            ((MyViewHolder)holder).replyView.setReply(replies.get(position));
        }
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

    static class OtherViewHolder extends RecyclerView.ViewHolder{

        ViewGroup viewGroup;

        public OtherViewHolder(View itemView) {
            super(itemView);
            this.viewGroup = (ViewGroup) itemView;
        }
    }
}
