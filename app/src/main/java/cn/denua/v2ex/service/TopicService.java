package cn.denua.v2ex.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import cn.denua.v2ex.api.TopicApi;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.http.RxObserver;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.utils.RxUtil;

/*
 *
 * @author denua
 * @date 2018/10/25
 */
public class TopicService {

    private TopicApi topicApi;

    public TopicService(){

        topicApi = RetrofitManager.create(TopicApi.class);
    }

    public void getHot(TopicListener topicListener){

        topicApi.getHotTopic()
                .compose(RxUtil.io2main())
                .subscribe(new RxObserver<JsonArray>() {
                    @Override
                    public void _onNext(JsonArray jsonElements) {

                        List<Topic> topics = new ArrayList<>();
                        Iterator<JsonElement> iterator = jsonElements.iterator();
                        for (JsonElement element; iterator.hasNext(); ){
                            element = iterator.next();
                            topics.add(new Gson().fromJson(element, Topic.class));
                        }
                        topicListener.onTopics(topics);
                    }
                    @Override
                    public void _onError(String msg) {
                        topicListener.onError(msg);
                    }
                });
    }

}
