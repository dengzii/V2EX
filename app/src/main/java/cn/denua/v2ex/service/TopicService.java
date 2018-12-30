package cn.denua.v2ex.service;

import android.text.Html;

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
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
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

    private RxObserver<List<Topic>> mObserver = new RxObserver<List<Topic>>(this) {
        @Override
        public void _onNext(List<Topic> topics) {
            returnSuccess(topics);
        }
    };

    public TopicService(IResponsibleView v, ResponseListener<List<Topic>> topicListener){
        this.view = v;
        attachView(v);
        setResponseListener(topicListener);
    }

    public void getTopic(TabEnum type, int page){

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
            case CUSTOM:
                break;
            case NODE:
                getTopicsByNode(type.getTitle(), page);
                break;
            case ALL:
                break;
            case FOLLOWING:
                break;
            default:
                break;
        }
    }

    public void postTopic(String title, String content, String node){

        int syntax = 1;
        topicApi.getPostTopicPage(node)
                .compose(RxUtil.io2io())
                .flatMap((Function<String, ObservableSource<String>>) s -> {
                    int once = HtmlUtil.getOnceFromPostTopicPage(s);
                    if (once < 1){
                        returnFailed(ErrorEnum.ERROR_PARSE_HTML);
                        return null;
                    }
                    return topicApi.createTopic(node, title, content, once, syntax);
                })
                .map((Function<String, List<Topic>>) s -> {
                    Topic topic = new Topic();
                    HtmlUtil.attachRepliesAndDetail(topic, s);
                    return new ArrayList<Topic>(1){{add(topic);}};
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    private void getHot(){

        topicApi.getHotTopic().compose(jsonArrayResolver()).subscribe(mObserver);
    }

    private void getLatest() {

        topicApi.getLatestTopic().compose(jsonArrayResolver()).subscribe(mObserver);
    }

    private void getChanges(){

        topicApi.getLatestTopic2()
                .compose(RxUtil.io2computation())
                .map(s -> {
                    if (s.matches(ErrorEnum.ERR_PAGE_NEED_LOGIN.getPattern())){
                        cancel();
                        returnFailed(ErrorEnum.ERR_PAGE_NEED_LOGIN);
                    }
                    return HtmlUtil.getTopics(s);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    private void getTopicsByNode(String node, int page){

        topicApi.getTopicsByNode(node, page)
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
                .compose(RxUtil.io2computation())
                .map((Function<String, List<Topic>>) s -> {
                    if (page == 1){
                        HtmlUtil.attachRepliesAndDetail(topicCopy, s);
                    }else {
                        HtmlUtil.attachReplies(topicCopy, s);
                    }
                    return new ArrayList<Topic>(){{add(topicCopy);}};
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
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

    private ObservableTransformer<JsonArray, List<Topic>> jsonArrayResolver(){

        return upstream -> upstream
                .compose(RxUtil.io2computation())
                .map(jsonElements -> {
                        List<Topic> topics = new ArrayList<>();
                        Iterator<JsonElement> iterator = jsonElements.iterator();

                        for (JsonElement element; iterator.hasNext(); ){
                            element = iterator.next();
                            topics.add(mGson.fromJson(element, Topic.class));
                        }
                        return topics;
                })
                .subscribeOn(AndroidSchedulers.mainThread());
    }
}
