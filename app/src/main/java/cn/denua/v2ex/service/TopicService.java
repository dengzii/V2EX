package cn.denua.v2ex.service;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    private static TopicApi topicApi;
    private static TopicService topicService = new TopicService();

    private TopicService(){
        topicApi = RetrofitManager.create(TopicApi.class);
    }

    public static void getTopic(String type, TopicListener topicListener){

        switch (type){
            case "热 门":
                topicService.getHot(new TopicObserver().callBack(topicListener));
                break;
            case "最 新":
                topicService.getLatest(new TopicObserver().callBack(topicListener));
                break;
            case "关 注":
                break;
            case "全 部":
                break;
            case "None":
                topicListener.onError("Not set content type yet!");
                break;
            default:
                break;

        }
    }

    private void getHot(TopicObserver topicObserver){

        topicApi.getHotTopic()
                .compose(RxUtil.io2main())
                .subscribe(topicObserver);
    }

    private void getLatest(TopicObserver topicObserver){

        topicApi.getLatestTopic()
                .compose(RxUtil.io2main())
                .subscribe(topicObserver);
    }


    static class TopicObserver extends RxObserver<JsonArray>{

        TopicListener topicListener;

        TopicObserver callBack(TopicListener topicListener){
            this.topicListener = topicListener;
            return this;
        }

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
    }
}
