/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.service;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import cn.denua.v2ex.Tab;
import cn.denua.v2ex.TabEnum;
import cn.denua.v2ex.api.TopicApi;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Member;
import cn.denua.v2ex.model.Node;
import cn.denua.v2ex.model.Reply;
import cn.denua.v2ex.model.Tag;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.utils.HtmlUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TopicServiceTest {

    @Before
    public void init(){

        RetrofitManager.init(null);
    }
    @After
    public void sleep(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){

        TabEnum tabEnum = TabEnum.NODE;
        tabEnum.setTitle("sandbox");
        new TopicService(iResponsibleView, new ResponseListener<List<Topic>>() {
            @Override
            public void onComplete(List<Topic> result) {
                System.out.println(result);
            }
            @Override
            public boolean onFailed(String msg) {
                System.err.println(msg);
                return true;
            }
        }).getTopic(new Tab(TabEnum.NODE,"v2ex","v2ex"),1);
    }

    @Test
    public void getTopicWithReplyTest(){

        TopicService.getTopicAndReply(this.iResponsibleView, 524071,1, new ResponseListener<Topic>() {
            @Override
            public boolean onFailed(String msg) {
                System.err.println(msg);
                return true;
            }
            @Override
            public void onComplete(Topic result) {
                System.out.println(result);
            }
        });

    }

    @Test
    public void getReplyTest(){

        TopicService.getReply(this.iResponsibleView, 524330, 1, new ResponseListener<List<Reply>>() {
            @Override
            public void onComplete(List<Reply> result) {
                for (Reply reply : result){
                    System.out.println(reply.toString());
                }
            }
            @Override
            public boolean onFailed(String msg) {
                System.out.println(msg);
                return true;
            }
        });
    }
    @Test
    public void getTopicDetailTest(){

        RetrofitManager.create(TopicApi.class)
                .getTopicDetail(523986,1)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("TopicServiceTest.onSubscribe");
                    }
                    @Override
                    public void onNext(String s) {
                        List<Reply> res = HtmlUtil.getReplies(s);
                        for (Reply reply:res){
                            System.out.println(res);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onComplete() {
                        System.out.println("TopicServiceTest.onComplete");
                    }
                });
    }
    @Test
    public void cloneTest(){

        Member member = new Member("Tom");
        Node node = new Node();
        node.setName("Node");

        Topic topic = new Topic();
        topic.setTitle("Topic1");
        topic.setMember(member);
        topic.setNode(node);

        Topic topic1 = (Topic) topic.clone();

        System.out.println(topic1.getMember().getUsername());
        System.out.println(topic.getMember().getUsername());
        System.out.println(topic.getNode().getName());
        System.out.println(topic1.getNode().getName());

        member.setUsername("Jerry");
        node.setName("NODE1");
        System.out.println(topic1.getMember().getUsername());
        System.out.println(topic.getMember().getUsername());
        System.out.println(topic.getNode().getName());
        System.out.println(topic1.getNode().getName());
    }

    @Test
    public void getHot() {

        RetrofitManager.create(TopicApi.class)
                .getHotTopic()
                .subscribe(new RxObserver<JsonArray>() {
                    @Override
                    public void _onNext(JsonArray jsonElements) {
                        jsonElements.forEach(jsonElement -> {
                            Topic topic = new Gson().fromJson(jsonElement, Topic.class);
                            System.out.println(topic.getTitle());
                            System.out.print(topic.getReplies());
                            System.out.print(topic.getReplies());
                            System.out.print(topic.getContent());
                        });
                    }
                    @Override
                    public void _onError(String msg) {
                        System.err.println(msg);
                    }
                });
    }

    private IResponsibleView iResponsibleView = new IResponsibleView() {
        @Override
        public void onStartRequest() {
            System.out.println("TopicServiceTest.onStartRequest");
        }

        @Override
        public void onProcessData(int progress) {

        }

        @Override
        public void onCompleteRequest() {
            System.out.println("TopicServiceTest.onCompleteRequest");
        }

        @Override
        public void onFailMsg(String msg) {

        }

        @Override
        public int getContextStatus() {
            return VIEW_STATUS_ACTIVATED;
        }

        @Override
        public Context getContext() {
            return null;
        }
    };
    static class Log{
        static void d(String t, String s){
            System.out.println((t == null? "" : (t+"\t")) +s);
        }
        static void e(String t, String s){
            System.err.println(s);
        }
    }
}