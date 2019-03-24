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
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.Config;
import cn.denua.v2ex.R;
import cn.denua.v2ex.Tab;
import cn.denua.v2ex.TabEnum;
import cn.denua.v2ex.adapter.PullRefreshReplyAdapter;
import cn.denua.v2ex.adapter.TopicRecyclerViewAdapter;
import cn.denua.v2ex.base.BaseNetworkFragment;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.service.ErrorEnum;
import cn.denua.v2ex.service.TopicService;

public class TopicFragment extends BaseNetworkFragment implements ResponseListener<List<Topic>> {

    static public String TAG = "TopicFragment";
    static
    private RecyclerView.RecycledViewPool sRecyclerViewPool = new RecyclerView.RecycledViewPool();

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_topics)
    RecyclerView recyclerView;

    private PullRefreshReplyAdapter mAdapter;
    private List<Topic> topics = new ArrayList<>();

    private TopicService topicService;
    private Tab mTabType;
    private boolean mIsNeedLogin = false;

    private int mCurrentPage = 1;

    private ResponseListener<List<Topic>> mResponseListener = new ResponseListener<List<Topic>>() {
        @Override
        public void onComplete(List<Topic> result) {
            topics.addAll(result);
            mAdapter.setStatus(PullRefreshReplyAdapter.FooterStatus.LOADING);
            mAdapter.notifyRangeInserted(topics.size() - result.size(), result.size());
        }
        @Override
        public boolean onFailed(String msg) {
            ToastUtils.showShort(msg);
            return true;
        }
    };

    public static TopicFragment create(Tab contentType){
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
            this.mTabType = (Tab) getArguments().getSerializable("contentType");
        }
        topicService = new TopicService(this, this);
        onRefresh();
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

//        recyclerView.setRecycledViewPool(sRecyclerViewPool);
        recyclerView.setFocusableInTouchMode(false);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(RecyclerView.DRAWING_CACHE_QUALITY_LOW);
        mAdapter = new PullRefreshReplyAdapter(getContext(),
                new TopicRecyclerViewAdapter(getContext(), topics));
        mAdapter.setBottomPadding(BarUtils.getNavBarHeight());

        recyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);

        setSwipeRefreshTheme(swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(true);
        if (mTabType.getType() == TabEnum.NODE){
            mAdapter.setOnPullUpListener(() -> {
                mCurrentPage ++;
                TopicService.getTopicByNode(this, mTabType.getTitle(),
                        mCurrentPage, mResponseListener);
            });
        }else{
            mAdapter.setStatus(PullRefreshReplyAdapter.FooterStatus.COMPLETE);
        }
        return savedView;
    }

    public void onRefresh() {
        if (mIsNeedLogin && !Config.getAccount().isLogin()){
            ToastUtils.showShort(R.string.not_login);
            return;
        }
        topicService.getTopic(mTabType, 1);
    }

    @Override
    public void onComplete(List<Topic> result) {

        topics.clear();
        topics.addAll(result);
        if (mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCompleteRequest() {
        super.onCompleteRequest();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public int getContextStatus() {
        return !isDetached()||isRemoving() ? VIEW_STATUS_DESTROYED : VIEW_STATUS_ACTIVATED;
    }

    @Override
    public boolean onFailed(String msg) {

        Toast.makeText(getContext(), msg + ", " + mTabType.getTitle(), Toast.LENGTH_SHORT).show();
        if(msg.equals(ErrorEnum.ERR_PAGE_NEED_LOGIN.getReadable())){
            mIsNeedLogin = true;
        }else{
            ToastUtils.showShort(msg);
        }
        return true;
    }
}
