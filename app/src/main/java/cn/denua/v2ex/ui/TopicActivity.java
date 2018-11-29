package cn.denua.v2ex.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class TopicActivity extends BaseNetworkActivity implements ResponseListener<List<Topic>> {

    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.topicView)
    TopicView mTopicView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.scrollView)
    NestedScrollView mScrollView;

    @BindView(R.id.rv_reply)
    RecyclerView mRecycleView;
    @BindView(R.id.tv_error)
    TextView mTvError;

    private Topic mTopic;
    private List<Reply> replies = new ArrayList<>();

    private ReplyRecyclerViewAdapter mRecyclerViewAdapter;

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

    @SuppressLint("ClickableViewAccessibility")
    protected void initView(){

        mRecyclerViewAdapter = new ReplyRecyclerViewAdapter(this, replies);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(mRecyclerViewAdapter);
        mRecycleView.setNestedScrollingEnabled(false);

        mTopicView.loadDataFromTopic(mTopic);
        if (mTopic.getContent_rendered()!=null){
            mWebView.loadData(HtmlUtil.applyHtmlStyle(mTopic.getContent_rendered()),
                    "text/html", "utf-8");
        }
        mSwipeRefreshLayout.setOnRefreshListener(this::onRefresh);

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setNetworkAvailable(true);
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

        if (mTopic.getContent_rendered()==null){
            mWebView.loadData(HtmlUtil.applyHtmlStyle(result.get(0).getContent_rendered()),
                    "text/html", "utf-8");
            mTopicView.setLastTouched(result.get(0).getAgo());
        }

        this.mTopic = result.get(0);
        this.replies = mTopic.getReplyList();

        mRecyclerViewAdapter.setReplies(this.replies);
        mRecyclerViewAdapter.notifyDataSetChanged();
        if (mWebView.getVisibility() != View.VISIBLE){
            mWebView.setVisibility(View.VISIBLE);
            mRecycleView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getContextStatus() {

        return IResponsibleView.VIEW_STATUS_ACTIVATED;
    }

    @Override
    public void onFailed(String msg) {

        mTvError.setText(msg);
        mWebView.setVisibility(View.GONE);
        mTvError.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
