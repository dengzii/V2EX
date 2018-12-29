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

import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.TabEnum;
import cn.denua.v2ex.adapter.TopicRecyclerViewAdapter;
import cn.denua.v2ex.base.BaseNetworkFragment;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.service.TopicService;

public class TopicFragment extends BaseNetworkFragment implements ResponseListener<List<Topic>> {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_topics)
    RecyclerView recyclerView;

    private TopicRecyclerViewAdapter adapter;
    private List<Topic> topics = new ArrayList<>();

    private TopicService topicService;
    private TabEnum mTabType;

    public static TopicFragment create(TabEnum contentType){

        TopicFragment topicFragment = new TopicFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("contentType", contentType);
        topicFragment.setArguments(bundle);

        return topicFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( getArguments() != null){
            this.mTabType = (TabEnum) getArguments().getSerializable("contentType");
        }
        topicService = new TopicService(this, this);
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
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(RecyclerView.DRAWING_CACHE_QUALITY_LOW);
        adapter = new TopicRecyclerViewAdapter(getContext(), topics);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);

        setSwipeRefreshTheme(swipeRefreshLayout);

        return savedView;
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    public void onRefresh() {

        topicService.getTopic(mTabType, 1);
    }

    @Override
    public void onComplete(List<Topic> result) {

        this.topics = result;
        adapter.addTopics(topics);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCompleteRequest() {
        super.onCompleteRequest();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailed(String msg) {
        ToastUtils.showShort(msg);
    }
}
