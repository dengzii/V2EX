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

import java.util.ArrayList;
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
    private List<Reply> mReplies;
    private Context context;
    private FrameLayout.LayoutParams mLayoutParams;

    private ViewGroup mHeaderViewGroup;
    private ViewGroup mFooterViewGroup;
    private int mItemCount = 0;

    public ReplyRecyclerViewAdapter(Context context){

        this.context = context;
        this.mReplies = new ArrayList<>();
        this.mLayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    public void setHeaderView(ViewGroup view){

        mItemCount += 1;
        mHeaderViewGroup = view;
    }

    public void setFooterView(ViewGroup view){

        mItemCount += 1;
        mFooterViewGroup = view;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == HEADER){
            return new OtherViewHolder(mHeaderViewGroup);
        }else if (viewType == FOOTER){
            return new OtherViewHolder(mFooterViewGroup);
        }
        ReplyView replyView = new ReplyView(parent.getContext());
        replyView.setLayoutParams(mLayoutParams);
        return new ItemViewHolder(replyView);
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0 && mHeaderViewGroup != null){
            return HEADER;
        }
        if (position == getItemCount() && mFooterViewGroup != null){
            return FOOTER;
        }
        return ITEM;
    }

    public void setReplies(List<Reply> replies){

        mItemCount += replies.size();
        mReplies = replies;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder){
            int p = ((mHeaderViewGroup!=null) ? position-1 : position);
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.replyView.setReply(mReplies.get(p));
        }
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{

        ReplyView replyView;

        public ItemViewHolder(View itemView) {
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
