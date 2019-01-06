/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.adapter;

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

    public enum FooterStatus{
        LOADING, HIDDEN, COMPLETE
    }

    public PullRefreshReplyAdapter(ReplyRecyclerViewAdapter adapter){
        this.mAdapter = adapter;

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        mFooterViewGroup = new FrameLayout(mAdapter.getContext());
        mFooterViewGroup.setLayoutParams(layoutParams);
        ProgressBar progressBar = new ProgressBar(mAdapter.getContext());
        progressBar.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 80));
        mFooterViewGroup.addView(progressBar);
        mFooterViewGroup.setVisibility(View.INVISIBLE);
    }

    public void setStatus(FooterStatus mStatus) {
        this.mStatus = mStatus;
        if (mStatus == FooterStatus.COMPLETE){
            mFooterViewGroup.removeAllViews();
            TextView textView = new TextView(mAdapter.getContext());
            textView.setText(mAdapter.getContext().getText(R.string.no_more));
            textView.setTextSize(15);
            textView.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            textView.setGravity(Gravity.CENTER);
            mFooterViewGroup.addView(textView);
        }
    }

    public void setOnPullUpListener(OnPullUpListener onPullUpListener) {
        this.mOnPullUpListener = onPullUpListener;
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

    public void notifyItem(int position){
        mAdapter.notifyItemChanged(position);
    }

    public void notifyAllData(){
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

    public void addReplies(List<Reply> replies){
        mAdapter.addReplies(replies);
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
