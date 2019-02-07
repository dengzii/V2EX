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
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import cn.denua.v2ex.model.Reply;
import cn.denua.v2ex.ui.TopicActivity;
import cn.denua.v2ex.widget.ReplyView;

/*
 * reply adapter
 *
 * @author denua
 * @date 2018/11/07 21
 */
public class ReplyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int HEADER = 1;
    final int FOOTER = 2;
    final int BOTTOM_PADDING = 4;
    private List<Reply> mReplies;
    private Context context;
    private FrameLayout.LayoutParams mLayoutParams;

    private ViewGroup mHeaderViewGroup;

    private View mBottomPaddingView;

    public ReplyRecyclerViewAdapter(Context context, List<Reply> replies){

        this.context = context;
        this.mReplies = replies;
        this.mLayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        mBottomPaddingView = new View(context);
        mBottomPaddingView.setLayoutParams(
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
    }

    public Context getContext() {
        return context;
    }

    public void setBottomPadding(int height){
        mBottomPaddingView.getLayoutParams().height = height;
    }

    public void setHeaderView(ViewGroup view){

        mHeaderViewGroup = view;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == HEADER){
            return new HeaderViewHolder(mHeaderViewGroup);
        }else if (viewType == BOTTOM_PADDING){
            return new PaddingViewHolder(mBottomPaddingView);
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
        if (position == getItemCount() - 1){
            return FOOTER;
        }
        if (position == getItemCount()){
            return BOTTOM_PADDING;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder){
            int p = ((mHeaderViewGroup!=null) ? position-1 : position);
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Reply reply = mReplies.get(p);
            reply.setFloor(position-1);
            itemViewHolder.replyView.setReply(reply);
        }
    }

    @Override
    public int getItemCount() {
        return mReplies.size() + (mHeaderViewGroup == null ? 0 : 1);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{

        ReplyView replyView;
        ItemViewHolder(View itemView) {
            super(itemView);
            this.replyView = (ReplyView) itemView;
        }

    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder{

        ViewGroup viewGroup;
        HeaderViewHolder(View itemView) {
            super(itemView);
            this.viewGroup = (ViewGroup) itemView;
        }
    }

    static class PaddingViewHolder extends RecyclerView.ViewHolder{

        View view;
        PaddingViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }
}
