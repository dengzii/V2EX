package cn.denua.v2ex.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;

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
import cn.denua.v2ex.wiget.TopicView;

public class TopicActivity extends BaseNetworkActivity implements ResponseListener<List<Topic>> {

    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.topicView)
    TopicView mTopicView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rv_reply)
    RecyclerView mRecycleView;

    private Topic topic;
    private List<Reply> replies = new ArrayList<>();

    private ReplyRecyclerViewAdapter mRecyclerViewAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_topic);
        ButterKnife.bind(this);

        setTitle(R.string.topic);
        this.topic = getIntent().getParcelableExtra("topic");
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void onRefresh(){

        new TopicService<>(this, this).getReply(topic, 1);
    }

    private void initView(){

        mRecyclerViewAdapter = new ReplyRecyclerViewAdapter(this, replies);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(mRecyclerViewAdapter);

        mTopicView.loadDataFromTopic(topic);
        mWebView.loadData(topic.getContent_rendered(), "text/html", "utf-8");
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);
    }

    @Override
    public void onCompleteRequest() {
        super.onCompleteRequest();
        ToastUtils.showShort("Request complete");
    }

    @Override
    public void onComplete(List<Topic> result) {

        this.topic = result.get(0);
        this.replies = topic.getReplyList();
        swipeRefreshLayout.setRefreshing(false);
        mRecyclerViewAdapter.setReplies(this.replies);
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public int getContextStatus() {
        return IResponsibleView.VIEW_STATUS_ACTIVATED;
    }

    @Override
    public void onFailed(String msg) {

        swipeRefreshLayout.setRefreshing(false);
    }
}
