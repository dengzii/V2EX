package cn.denua.v2ex.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.denua.v2ex.api.TopicApi;
import cn.denua.v2ex.base.BaseService;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.http.RxObserver;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Reply;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.utils.HtmlUtil;
import cn.denua.v2ex.utils.RxUtil;
import io.reactivex.disposables.Disposable;

/*
 * 话题相关请求
 *
 * @author denua
 * @date 2018/10/25
 */
public class TopicService<V extends IResponsibleView> extends BaseService<V, List<Topic>> {

    private static TopicApi topicApi = RetrofitManager.create(TopicApi.class);

    public TopicService(V v, ResponseListener<List<Topic>> topicListener){
        this.view = v;
        attachView(v);
        setResponseListener(topicListener);
    }

    public void getTopic(String type){

        switch (type){
            case "热 门":
                getHot();
                break;
            case "最 新":
                getLatest();
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

    public void getReply(Topic topic, int page){

        Topic topicCopy = (Topic) topic.clone();
        topicApi.getTopicDetail(topicCopy.getId(), page)
                .compose(RxUtil.io2main())
                .subscribe(new RxObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        onStartRequest();
                    }
                    @Override
                    public void _onNext(String s) {
                        if (page == 1){
                            HtmlUtil.attachRepliesAndDetail(topicCopy, s);
                        }else {
                            HtmlUtil.attachReplies(topicCopy, s);
                        }
                        returnSuccess(new ArrayList<Topic>(){{add(topicCopy);}});
                    }
                    @Override
                    public void _onError(String msg) {
                        returnFailed(msg);
                    }
                });
    }

    public void getReplyFromApi(Topic topic, int page){

        Topic topicCopy = (Topic) topic.clone();
        topicApi.getReplies(topicCopy.getId(), page)
                .compose(RxUtil.io2main())
                .subscribe(new RxObserver<JsonArray>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        onStartRequest();
                    }
                    @Override
                    public void _onNext(JsonArray jsonElements) {
                        List<Reply> replies = new ArrayList<>(jsonElements.size());
                        for (JsonElement element : jsonElements) {
                            Reply reply = new Gson().fromJson(element, Reply.class);
                            replies.add(reply);
                        }
                        topicCopy.setReplyList(replies);
                        returnSuccess(new ArrayList<Topic>(1){{
                            add(topicCopy);
                        }});
                    }
                    @Override
                    public void _onError(String msg) {
                        returnFailed(msg);
                    }
                });
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

        @Override
        public void onComplete() {
            super.onComplete();
            onCompleteRequest();
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
