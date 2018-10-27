/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.junit.Test;

import cn.denua.v2ex.api.TopicApi;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.http.RxObserver;
import cn.denua.v2ex.model.Topic;

public class TopicServiceTest {

    @Test
    public void getHot() {

        RetrofitManager.init(null);

        RetrofitManager.create(TopicApi.class)
                .getHotTopic()
                .subscribe(new RxObserver<JsonArray>() {
                    @Override
                    public void _onNext(JsonArray jsonElements) {
                        jsonElements.forEach(jsonElement -> {
                            Topic topic = new Gson().fromJson(jsonElement, Topic.class);
                            System.out.println(topic.getTitle());
                            System.out.println(topic.getReplies());
                            System.out.println(topic.getContent());
                        });
                    }
                    @Override
                    public void _onError(String msg) {
                        System.err.println(msg);
                    }
                });
    }
}