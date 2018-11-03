package cn.denua.v2ex.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseActivity;
import cn.denua.v2ex.model.Reply;
import cn.denua.v2ex.model.Topic;

public class TopicActivity extends BaseActivity {

    private Topic topic;
    private Reply[] replies;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_topic);

        this.topic = getIntent().getParcelableExtra("topic");
        setTitle(topic.getTitle());
    }
}
