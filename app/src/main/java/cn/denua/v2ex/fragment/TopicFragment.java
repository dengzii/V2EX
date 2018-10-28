package cn.denua.v2ex.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class TopicFragment extends BaseFragment implements TopicListener {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rv_topics)
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    List<Topic> topics = new ArrayList<>();

    public static TopicFragment newInstance(String contentType){

        TopicFragment topicFragment = new TopicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("contentType", contentType);
        topicFragment.setArguments(bundle);

        return topicFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( getArguments() != null){
            this.setContentType(getArguments().getString("contentType"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (this.savedView != null)
            return savedView;

        savedView = inflater.inflate(R.layout.frag_topic, container,false);

        ButterKnife.bind(this, savedView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(getContext(), topics);
        recyclerView.setAdapter(adapter);

        return savedView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeRefreshLayout.setRefreshing(true);
        TopicService.getTopic(getContentType(), this);
    }

    @Override
    public void onTopics(List<Topic> topics) {

        swipeRefreshLayout.setRefreshing(false);
        this.topics = topics;
        adapter.setTopics(topics);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
