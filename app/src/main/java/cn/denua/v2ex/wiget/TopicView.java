/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.wiget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.base.App;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.ui.NodeActivity;
import cn.denua.v2ex.ui.TopicActivity;

/*
 * Topic 话题列表的 item, 自定义 view
 *
 * @author denua
 * @date 2018/10/31 12
 * @date 2018/10/31 12
 */
public class TopicView extends FrameLayout {

    @BindView(R.id.iv_user_pic)
    ImageView ivUserPic;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_replay)
    TextView tvReply;
    @BindView(R.id.tv_latest_touched)
    TextView tvLastTouched;
    @BindView(R.id.tv_node)
    TextView tvNode;

    private Context context;
    private Topic topic;
    private int topicId;

    public TopicView(Context context ) {
        super(context);
        this.context = context;
        initView(context);
    }

    public TopicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        this.getRootView();
    }

    private void initView(Context context ){
        inflate(context, R.layout.view_topic, this);
        ButterKnife.bind(this);
        setOnClickListener(v -> goToTopicDetail());
    }

    public void  loadDataFromTopic(Topic topic){

        String lastTouched = TimeUtils.getFitTimeSpanByNow(topic.getLast_touched()*1000, 4);
        String userPicUrl = topic.getMember().getAvatar_large();
        lastTouched = lastTouched.startsWith("-")?lastTouched.substring(1):lastTouched;

        this.topic = topic;
        tvTitle.setText(topic.getTitle());
        tvTitle.setOnClickListener(v -> goToTopicDetail());
        tvUsername.setText(topic.getMember().getUsername());
        tvReply.setText(String.valueOf(topic.getReplies()));
        tvLastTouched.setText(lastTouched);
        tvNode.setText(topic.getNode().getName());
        Glide.with(this).load(userPicUrl).into(ivUserPic);

        tvNode.setOnClickListener(v->{
            goToNodeDetail();
        });
    }

    private void goToNodeDetail(){

        Intent intent = new Intent(context, NodeActivity.class);
        intent.putExtra("node", topic.getNode());
        context.startActivity(intent);
    }

    private void goToTopicDetail(){

        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra("topic",topic);
        context.startActivity(intent);
    }
}
