/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import cn.denua.v2ex.api.TopicApi;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.http.RxObserver;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Reply;
import cn.denua.v2ex.model.Topic;

public class TopicServiceTest {

    @Before
    public void init(){

        RetrofitManager.init(null);
    }

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

    @Test
    public void getReply(){

        Topic topic = new Topic();
        topic.setId(505186);

        new TopicService<>(new IResponsibleView() {
            @Override
            public void onStartRequest() { }

            @Override
            public void onProcessData(int progress) { }

            @Override
            public void onCompleteRequest() { }

            @Override
            public int getContextStatus() {
                return IResponsibleView.VIEW_STATUS_ACTIVATED;
            }
        }, new ResponseListener<List<Topic>>() {
            @Override
            public void onComplete(List<Topic> result) {
                List<Reply> replies = result.get(0).getReplyList();
                for (Reply reply:replies){
                    System.out.println(reply.getMember().getUsername());
                    System.out.println(reply.getContent());
                    System.out.println(reply.getId());
                }
            }
            @Override
            public void onFailed(String msg) {
                System.err.println(msg);
            }
        })
        .getReply(topic, 1);
    }
}