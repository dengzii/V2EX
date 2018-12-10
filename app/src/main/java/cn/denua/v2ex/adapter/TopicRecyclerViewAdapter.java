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

import java.util.List;

import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.ui.TopicActivity;
import cn.denua.v2ex.widget.TopicView;
import retrofit2.http.HEAD;

public class TopicRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Topic> mTopics;
    private Context context;
    private boolean mIsSimpleView;
    private final int ITEM = 0;
    private final int HEADER = 1;
    private final int FOOT = 2;
    private int mItemCount = 0;

    private FrameLayout mHeaderFrameLayout;
    private FrameLayout mFooterFrameLayout;

    private FrameLayout.LayoutParams mWrapContentParams;

    public TopicRecyclerViewAdapter(Context context, List<Topic> topics){
        this.mIsSimpleView = false;
        this.mTopics = topics;
        this.context = context;
        this.mItemCount = topics.size();
        this.mWrapContentParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setIsSimpleView(boolean isSimpleView){
        this.mIsSimpleView = isSimpleView;
    }

    public void setHeaderView(View headerView){
        mHeaderFrameLayout = new FrameLayout(context);
        mHeaderFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 100, Gravity.CENTER_VERTICAL));
        mHeaderFrameLayout.addView(headerView);
        mItemCount += 1;
    }

    public void setFooterView(View footerView){

        mFooterFrameLayout = new FrameLayout(context);
        mFooterFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 150, Gravity.CENTER));
        mFooterFrameLayout.addView(footerView);
        mItemCount += 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == HEADER && mHeaderFrameLayout != null){
            return new OtherViewHolder(mHeaderFrameLayout);
        }
        if (viewType == FOOT && mFooterFrameLayout != null){
            return new OtherViewHolder(mFooterFrameLayout);
        }
        TopicView topicView = new TopicView(context, mIsSimpleView);
        topicView.setLayoutParams(mWrapContentParams);
        return new ItemViewHolder(topicView);
    }

    public void addTopics(List<Topic> topics) {
        this.mItemCount += topics.size();
        this.mTopics.addAll(topics);
    }

    @Override
    public int getItemViewType(int position) {

        if (position+1 == getItemCount() && mTopics.size() > 12){
            return FOOT;
        }else if (position == 0){
            return HEADER;
        }else{
            return ITEM;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof OtherViewHolder){
            return;
        }
        Topic topic = mTopics.get(position - ((mHeaderFrameLayout==null) ? 0 : 1));
        if (topic == null){
            return;
        }
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.topicView.loadDataFromTopic(topic);
        itemViewHolder.topicView.setOnClickListener(v-> TopicActivity.start(context, topic));
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{

        TopicView topicView;
        ItemViewHolder(View itemView) {
            super(itemView);
            this.topicView = (TopicView) itemView;
        }
    }

    static class OtherViewHolder extends RecyclerView.ViewHolder{

        FrameLayout frameLayout;
        OtherViewHolder(View itemView) {
            super(itemView);
            this.frameLayout = (FrameLayout) itemView;
        }
    }
}
