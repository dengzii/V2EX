package cn.denua.v2ex.service;

import java.util.List;

import cn.denua.v2ex.model.Topic;

public interface TopicListener extends ErrorLinstener{
    void onTopics(List<Topic> topics);
}
