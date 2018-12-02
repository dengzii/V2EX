package cn.denua.v2ex.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.adapter.ReplyRecyclerViewAdapter;
import cn.denua.v2ex.base.BaseNetworkActivity;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Reply;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.service.TopicService;
import cn.denua.v2ex.utils.HtmlUtil;
import cn.denua.v2ex.widget.TopicView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class TopicActivity extends BaseNetworkActivity implements ResponseListener<List<Topic>> {

    private WebView mWebView;
    private TopicView mTopicView;
    private TextView mTvError;
    private LinearLayout mLlHeader;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_reply)
    RecyclerView mRecyclerView;

    private Topic mTopic;
    private List<Reply> mReplies = new ArrayList<>();

    private ReplyRecyclerViewAdapter mRecyclerViewAdapter;

    public static void start(Context context, Topic topic){

        Intent intent = new Intent(context, TopicActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("topic", topic);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_topic);
        ButterKnife.bind(this);

        setTitle(R.string.topic);
        this.mTopic = getIntent().getParcelableExtra("topic");
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSwipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    protected void initView(){

        mRecyclerViewAdapter = new ReplyRecyclerViewAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        initHeaderView();

        mSwipeRefreshLayout.setOnRefreshListener(this::onRefresh);
    }

    private void initHeaderView(){

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLlHeader = new LinearLayout(this);
        mLlHeader.setGravity(Gravity.CENTER_HORIZONTAL);
        mLlHeader.setOrientation(LinearLayout.VERTICAL);
        mLlHeader.setLayoutParams(linearLayoutParams);
        
        mTopicView = new TopicView(this, false);
        mTopicView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
        mTopicView.loadDataFromTopic(mTopic);

        mWebView = new WebView(this);
        mWebView.setNetworkAvailable(true);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setNetworkAvailable(true);

        if (mTopic.getContent_rendered()!=null){
            mWebView.loadData(HtmlUtil.applyHtmlStyle(mTopic.getContent_rendered()),
                    "text/html", "utf-8");
        }
        mLlHeader.addView(mTopicView);
        mLlHeader.addView(mWebView);
        mRecyclerViewAdapter.setHeaderView(mLlHeader);
    }

    private void onRefresh(){

        new TopicService(this, this).getReply(mTopic, 1);
    }

    @Override
    public void onCompleteRequest() {
        super.onCompleteRequest();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onComplete(List<Topic> result) {

        this.mTopic = result.get(0);
        this.mReplies = result.get(0).getReplyList();
        setRecyclerViewData(mReplies, mTopic);
    }

    @Override
    public void onFailed(String msg) {

        mTvError = new TextView(this);
        mTvError.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 600));
        mTvError.setTextSize(25);
        mTvError.setTextColor(getColorAccent());
        mTvError.setGravity(Gravity.CENTER);
        mTvError.setText(msg);

        mLlHeader.addView(mTvError);
    }

    private void setRecyclerViewData(List<Reply> replies, Topic topic){

        if (mTopic.getContent_rendered() == null) {
            mWebView.loadData(HtmlUtil.applyHtmlStyle(topic.getContent_rendered()),
                    "text/html", "utf-8");
            mTopicView.setLastTouched(topic.getAgo());
        }
        mTopicView.loadDataFromTopic(mTopic);

        mRecyclerViewAdapter.setReplies(replies);
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public int getContextStatus() {

        return IResponsibleView.VIEW_STATUS_ACTIVATED;
    }


}
