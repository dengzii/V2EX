package cn.denua.v2ex.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.denua.v2ex.TabEnum;
import cn.denua.v2ex.api.TopicApi;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Reply;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.utils.HtmlUtil;
import cn.denua.v2ex.utils.RxUtil;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/*
 * 话题相关请求
 *
 * @author denua
 * @date 2018/10/25
 */
public class TopicService extends BaseService<List<Topic>> {

    private static TopicApi topicApi = RetrofitManager.create(TopicApi.class);
    private Gson mGson = new Gson();

    public TopicService(IResponsibleView v, ResponseListener<List<Topic>> topicListener){
        this.view = v;
        attachView(v);
        setResponseListener(topicListener);
    }

    public void getTopic(TabEnum type){

        switch (type){
            case HOT:
                getHot();
                break;
            case LATEST:
                getLatest();
                break;
            case CHANGES:
                getChanges();
                break;
            case ALL:
                break;
            case FOLLOWING:
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

    private void getChanges(){

        topicApi.getLatestTopic2()
                .compose(RxUtil.io2main())
                .subscribe(new RxObserver<String>(this) {
                    @Override
                    public void _onNext(String s) {
                        List<Topic> topics = HtmlUtil.getTopics(s);
                        returnSuccess(topics);
                    }
                });
    }

    public void getReply(Topic topic, int page){
        Topic topicCopy = (Topic) topic.clone();
        topicApi.getTopicDetail(topicCopy.getId(), page)
                .compose(RxUtil.io2main())
                .subscribe(new RxObserver<String>(this) {
                    @Override
                    public void _onNext(String s) {
                        if (page == 1){
                            HtmlUtil.attachRepliesAndDetail(topicCopy, s);
                        }else {
                            HtmlUtil.attachReplies(topicCopy, s);
                        }
                        returnSuccess(new ArrayList<Topic>(1){{add(topicCopy);}});
                    }
                });
    }

    public void getReplyFromApi(Topic topic, int page){

        Topic topicCopy = (Topic) topic.clone();
        topicApi.getReplies(topicCopy.getId(), page)
                .compose(RxUtil.io2main())
                .subscribe(new RxObserver<JsonArray>(this) {
                    @Override
                    public void _onNext(JsonArray jsonElements) {
                        List<Reply> replies = new ArrayList<>(jsonElements.size());
                        for (JsonElement element : jsonElements) {
                            Reply reply = new Gson().fromJson(element, Reply.class);
                            replies.add(reply);
                        }
                        topicCopy.setReplyList(replies);
                        returnSuccess(new ArrayList<Topic>(1){{ add(topicCopy); }});
                    }
                });
    }

    private RxObserver<JsonArray> jsonArrayToTopicsObserver =
            new RxObserver<JsonArray>(this) {
                @Override
                public void _onNext(JsonArray jsonElements) {

                    handleJsonArray(jsonElements);
                }
            };

    private void handleJsonArray(JsonArray jsonArray){

        List<Topic> topics = new ArrayList<>();
        Iterator<JsonElement> iterator = jsonArray.iterator();

        for (JsonElement element; iterator.hasNext(); ){
            element = iterator.next();
            topics.add(mGson.fromJson(element, Topic.class));
        }
        returnSuccess(topics);
    }
}
