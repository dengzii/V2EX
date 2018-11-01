package cn.denua.v2ex.service;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.denua.v2ex.api.TopicApi;
import cn.denua.v2ex.base.BaseService;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.http.RxObserver;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.utils.RxUtil;
import io.reactivex.disposables.Disposable;

/*
 *
 * @author denua
 * @date 2018/10/25
 */
public class TopicService<V extends IResponsibleView> extends BaseService<V, List<Topic>> {

    private static TopicApi topicApi;
    private static TopicService topicService = new TopicService<>();

    private TopicService(){
        topicApi = RetrofitManager.create(TopicApi.class);
    }

    public static TopicService getInstance(){
        return topicService;
    }

    public void getTopic(String type, V v, ResponseListener<List<Topic>> topicListener){

        this.attachView(v);
        topicService.setResponseListener(topicListener);
        switch (type){
            case "热 门":
                topicService.getHot();
                break;
            case "最 新":
                topicService.getLatest();
                break;
            case "关 注":
                break;
            case "全 部":
                break;
            case "None":

                break;
            default:
                break;

        }
    }

    private void getHot(){

        topicApi.getHotTopic()
                .compose(RxUtil.io2main())
                .subscribe(jsonArrayToTopicsObserver);
    }

    private void getLatest(){

        topicApi.getLatestTopic()
                .compose(RxUtil.io2main())
                .subscribe(jsonArrayToTopicsObserver);
    }

    private RxObserver<JsonArray> jsonArrayToTopicsObserver = new RxObserver<JsonArray>() {

        @Override
        public void onSubscribe(Disposable d) {
            super.onSubscribe(d);
            onStartRequest();
        }

        @Override
        public void _onNext(JsonArray jsonElements) {
            handleJsonArray(jsonElements);
        }
        @Override
        public void _onError(String msg) {
            returnFailed(msg);
        }
    };

    private void handleJsonArray(JsonArray jsonArray){

        List<Topic> topics = new ArrayList<>();
        Iterator<JsonElement> iterator = jsonArray.iterator();
        for (JsonElement element; iterator.hasNext(); ){
            element = iterator.next();
            topics.add(new Gson().fromJson(element, Topic.class));
        }
        returnSuccess(topics);
    }
}
