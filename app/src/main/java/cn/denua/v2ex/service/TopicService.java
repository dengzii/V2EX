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
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
            if (topics != null){
                returnSuccess(topics);
                reset();
            }
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

    public static void  postTopic(IResponsibleView iResponsibleView,
                          String title,
                          String content,
                          String node,
                          ResponseListener<Topic> listener){

        int syntax = 1;
        topicApi.getPostTopicPage(node)
                .compose(RxUtil.io2computation())
                .flatMap((Function<String, ObservableSource<String>>) s -> {
                    int once = HtmlUtil.getOnceFromPostTopicPage(s);
                    if (once < 1){
                       ErrorEnum.ERROR_PARSE_HTML.throwThis();
                    }
                    return topicApi.createTopic(node, title, content, once, syntax);
                })
                .map(s -> {
                    ErrorEnum.ERROR_CREATE_TOPIC_TOO_OFTEN.check(s);
                    ErrorEnum.ERROR_CREATE_TOPIC_NEED_VERIFY_EMAIL.check(s);
                    return HtmlUtil.getTopicAndReplies(s);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver2<>(iResponsibleView, listener));
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
                .compose(RxUtil.io2computation())
                .map(s -> {
                    ErrorEnum.ERR_PAGE_NEED_LOGIN0.check(s);
                    return HtmlUtil.getTopics(s);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver2<>(getView(), getResponseListener()));
    }

    public static void getTopicAndReply(IResponsibleView responsibleView,
                                        int topicId,
                                        int page,
                                        ResponseListener<Topic> listener){
        topicApi.getTopicDetail(topicId, page)
                .compose(RxUtil.io2main())
                .map(s -> {
                    ErrorEnum.ERR_PAGE_NEED_LOGIN.check(s);
                    try {
                        return HtmlUtil.getTopicAndReplies(s);
                    }catch (Exception e){
                        e.printStackTrace();
                        ErrorEnum.ERROR_PARSE_HTML.throwThis();
                        return null;
                    }
                })
                .subscribe(new RxObserver2<>(responsibleView, listener));
    }

    public static void postReply(IResponsibleView responsibleView,
                                 Topic topic,
                                 String content,
                                 ResponseListener<String> responseListener){
        topicApi.postReply(topic.getId(), topic.getOnce(), content)
        .compose(RxUtil.io2main())
        .subscribe(new RxObserver2<String>(responsibleView, responseListener) {
            @Override
            public void onNext(String s) {
                super.onNext(s);

            }
        });
    }

    public static void getReply(IResponsibleView responsibleView,
                                int topicId,
                                int page,
                                ResponseListener<List<Reply>> listener){

        topicApi.getTopicDetail(topicId, page)
                .subscribeOn(Schedulers.computation())
                .map(HtmlUtil::getReplies)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver2<>(responsibleView, listener));

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
                .observeOn(AndroidSchedulers.mainThread());
    }
}
