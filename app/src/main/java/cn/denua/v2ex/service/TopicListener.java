package cn.denua.v2ex.service;

import java.util.List;

import cn.denua.v2ex.interfaces.FailedListener;
import cn.denua.v2ex.model.Topic;

public interface TopicListener extends FailedListener {
    void onTopics(List<Topic> topics);
}
