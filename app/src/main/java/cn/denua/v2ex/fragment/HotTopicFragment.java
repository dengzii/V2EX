package cn.denua.v2ex.fragment;

import android.content.Context;
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
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.service.TopicListener;
import cn.denua.v2ex.service.TopicService;

public class HotTopicFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, TopicListener {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    TopicService topicService = new TopicService();

    @BindView(R.id.rv_topics)
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    List<Topic> topics = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_topic, container,false);

        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        topicService.getHot(this);

        adapter = new RecyclerViewAdapter(getContext(), topics);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onTopics(List<Topic> topics) {
        swipeRefreshLayout.setRefreshing(false);
        adapter.setTopics(topics);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String msg) {

    }
}
