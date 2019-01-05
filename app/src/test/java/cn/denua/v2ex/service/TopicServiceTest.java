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

        Observable.create((ObservableOnSubscribe<String>) emitter -> {
                            System.out.println(Thread.currentThread().getName());
                            emitter.onNext("1");
                            emitter.onNext("2");
                            emitter.onNext("a");
                            emitter.onComplete();
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .flatMap((Function<String, ObservableSource<Integer>>) s -> {
                    System.out.println(Thread.currentThread().getName());
                    return Observable.just(Integer.valueOf(s)*2);
                })
                .observeOn(Schedulers.newThread())
                .doOnError(throwable -> {
                    System.out.println(Thread.currentThread().getName());
                    throwable.printStackTrace();
                })
                .subscribe(integer -> {
                    System.out.println(Thread.currentThread().getName());
                    System.out.println(integer);
                })
                .dispose();
    }

    @Test
    public void getReplyTest(){

        TopicService.getReply(this.iResponsibleView, 523986, 1, new ResponseListener<List<Reply>>() {
            @Override
            public void onComplete(List<Reply> result) {
                for (Reply reply : result){
                    System.out.println(reply.toString());
                }
            }
            @Override
            public void onFailed(String msg) {
                System.out.println(msg);
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

    @Test
    public void getReply(){

        Topic topic = new Topic();
        topic.setId(505378);

        new TopicService(iResponsibleView, new ResponseListener<List<Topic>>() {
            @Override
            public void onComplete(List<Topic> result) {
                Topic topic1 = result.get(0);
                List<Reply> replies = topic1.getReplyList();

                Log.d("CsrfToken", topic1.getCsrfToken());
                Log.d("Node:", topic1.getNode().getName() + topic1.getNode().getTitle());
                Log.d("Thanks:", String.valueOf(topic1.getThanks()));
                Log.d("Clicks:", String.valueOf(topic1.getClicks()));

                for (Tag tag:topic1.getTags()){
                    Log.d("Tag", tag.getName());
                }

                if (replies == null){
                    Log.e(null, "No replies exists.");
                    return;
                }
                for (Reply reply:replies){
                    Log.d(null,  reply.getMember().getAvatar_normal());
                    Log.d(null, reply.getMember().getUsername() + reply.getAgo() + reply.getId() + reply.getVia());
                    Log.d(null, reply.getContent());
                    Log.d(null, "================================================================");
                }
            }
            @Override
            public void onFailed(String msg) {
                Log.d(null, msg);
            }
        })
        .getReply(topic, 1);
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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