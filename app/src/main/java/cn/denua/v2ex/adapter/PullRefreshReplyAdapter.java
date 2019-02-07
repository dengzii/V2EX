/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import cn.denua.v2ex.R;
import cn.denua.v2ex.model.Reply;

/*
 * pull refresh reply recycler view adapter
 *
 * @author denua
 * @date 2018/12/03 18
 */
public class PullRefreshReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ReplyRecyclerViewAdapter mAdapter;

    private ViewGroup mFooterViewGroup;
    private FooterStatus mStatus = FooterStatus.LOADING;
    private OnPullUpListener mOnPullUpListener;
    private TextView mTvStatus;
    private ProgressBar mPbLoading;

    public enum FooterStatus{
        LOADING, HIDDEN, COMPLETE
    }

    public PullRefreshReplyAdapter(Context context, List<Reply> replies){
        this(new ReplyRecyclerViewAdapter(context, replies));
    }

    public PullRefreshReplyAdapter(ReplyRecyclerViewAdapter adapter){
        this.mAdapter = adapter;

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        mFooterViewGroup = new FrameLayout(mAdapter.getContext());
        mFooterViewGroup.setLayoutParams(layoutParams);
        mPbLoading = new ProgressBar(mAdapter.getContext());
        mPbLoading.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 80));
        mFooterViewGroup.addView(mPbLoading);
        mFooterViewGroup.setVisibility(View.INVISIBLE);

        mTvStatus = new TextView(mAdapter.getContext());
        mTvStatus.setTextSize(15);
        mTvStatus.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mTvStatus.setGravity(Gravity.CENTER);
    }

    public void setStatus(FooterStatus mStatus) {
        this.mStatus = mStatus;
        switch (mStatus){
            case COMPLETE:
                mFooterViewGroup.removeAllViews();
                mTvStatus.setText(mAdapter.getContext().getString(R.string.no_more));
                mFooterViewGroup.addView(mTvStatus);
                break;
            case LOADING:
                mFooterViewGroup.removeAllViews();
                mFooterViewGroup.addView(mPbLoading);
                break;
            case HIDDEN:
                mFooterViewGroup.setVisibility(View.GONE);
                break;
        }
    }

    public void setOnPullUpListener(OnPullUpListener onPullUpListener) {
        this.mOnPullUpListener = onPullUpListener;
    }

    public void setBottomPadding(int height){
        mAdapter.setBottomPadding(height);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == mAdapter.FOOTER){
            return new FooterViewHolder(mFooterViewGroup);
        }else{
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    public void setHeaderView(LinearLayout mLlHeader) {

        mAdapter.setHeaderView(mLlHeader);
    }

    public void notifyRangeChanged(int start, int count){

        mAdapter.notifyItemRangeChanged(start, count);
    }

    public void notifyItem(int position){
        mAdapter.notifyItemChanged(position);
    }

    public void notifyAllDataChanged(){
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder && position > 10){
            switch (mStatus){
                case HIDDEN:
                    mFooterViewGroup.setVisibility(View.GONE);
                    break;
                case COMPLETE:
                    mFooterViewGroup.setVisibility(View.VISIBLE);
                    break;
                case LOADING:
                    mFooterViewGroup.setVisibility(View.VISIBLE);
                    mOnPullUpListener.onPullUp();
                    break;
            }
        }else{
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    public interface OnPullUpListener{
        void onPullUp();
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 1;
    }

    private static class FooterViewHolder extends RecyclerView.ViewHolder{

        ViewGroup viewGroup;
        FooterViewHolder(View itemView) {
            super(itemView);
            this.viewGroup = (ViewGroup) itemView;
        }
    }
}
