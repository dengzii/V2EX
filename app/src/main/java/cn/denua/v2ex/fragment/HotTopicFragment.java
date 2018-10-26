package cn.denua.v2ex.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.adapter.RecyclerViewAdapter;
import cn.denua.v2ex.base.BaseFragment;
import cn.denua.v2ex.model.Member;
import cn.denua.v2ex.model.Node;
import cn.denua.v2ex.model.Topic;

public class HotTopicFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_topic, container,false);

        ButterKnife.bind(this, view);

        RecyclerView recyclerView = view.findViewById(R.id.rv_topics);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Topic topic = new Topic();
        Node node = new Node();
        node.name = "V2EX";
        Member member = new Member();
        member.nickName = "nickname";

        topic.title = "Hello World, whats you name ?Hello World, whats you name ?Hello World, whats you name ?Hello World, whats you name ?Hello World, whats you name ?";
        topic.author = member;
        topic.refreshTime = "1 小时前";
        topic.replay = 100;
        topic.node = node;
        List<Topic> topics = new ArrayList<>();

        topics.add(topic);
        topics.add(topic);
        topics.add(topic);
        topics.add(topic);
        topics.add(topic);
        topics.add(topic);
        topics.add(topic);
        topics.add(topic);
        topics.add(topic);
        topics.add(topic);
        topics.add(topic);topics.add(topic);topics.add(topic);topics.add(topic);topics.add(topic);topics.add(topic);
        topics.add(topic);topics.add(topic);topics.add(topic);topics.add(topic);topics.add(topic);topics.add(topic);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(topics);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onRefresh() {
        new Thread(()->{
            try {
                Thread.sleep(3000);
                swipeRefreshLayout.setRefreshing(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
