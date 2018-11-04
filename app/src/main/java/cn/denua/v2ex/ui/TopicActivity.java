package cn.denua.v2ex.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseActivity;
import cn.denua.v2ex.model.Reply;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.wiget.TopicView;

public class TopicActivity extends BaseActivity {

//    @BindView(R.id.iv_user_pic)
//    ImageView mUserPic;
//    @BindView(R.id.tv_topic_title)
//    TextView mTitle;
//    @BindView(R.id.tv_latest_touched)
//    TextView mLastTouched;
//    @BindView(R.id.tv_username)
//    TextView mUsername;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.topicView)
    TopicView mTopicView;

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

    private void initView(){

        mTopicView.loadDataFromTopic(topic);
//        Glide.with(this).load(topic.getMember().getAvatar_large()).into(mUserPic);
//        mTitle.setText(topic.getTitle());
//        mLastTouched.setText(com.blankj.utilcode.util.TimeUtils.getFitTimeSpanByNow(topic.getLast_touched(), 4));
//        mUsername.setText(topic.getMember().getUsername());
        mWebView.loadData(topic.getContent_rendered(), "text/html", "utf-8");
    }
}
