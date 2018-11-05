package cn.denua.v2ex.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.webkit.WebView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseActivity;
import cn.denua.v2ex.base.BaseNetworkActivity;
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

    private Topic topic;
    private Reply[] replies;

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

        new TopicService<>(this, this).getReply(topic.getId(), 1);
    }

    private void initView(){

        mTopicView.loadDataFromTopic(topic);
        mWebView.loadData(topic.getContent_rendered(), "text/html", "utf-8");
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);
    }

    @Override
    public void onComplete(List<Topic> result) {

    }

    @Override
    public void onFailed(String msg) {

    }
}
