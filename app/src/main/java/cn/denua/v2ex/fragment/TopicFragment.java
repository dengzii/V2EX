package cn.denua.v2ex.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.Config;
import cn.denua.v2ex.R;
import cn.denua.v2ex.Tab;
import cn.denua.v2ex.TabEnum;
import cn.denua.v2ex.adapter.TopicRecyclerViewAdapter;
import cn.denua.v2ex.base.BaseActivity;
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

    private TopicRecyclerViewAdapter adapter;
    private List<Topic> topics = new ArrayList<>();

    private TopicService topicService;
    private Tab mTabType;
    private boolean mIsNeedLogin = false;

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

        recyclerView.setRecycledViewPool(sRecyclerViewPool);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(RecyclerView.DRAWING_CACHE_QUALITY_LOW);
        adapter = new TopicRecyclerViewAdapter(getContext(), topics);
        adapter.setBottomPadding(BarUtils.getNavBarHeight());
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);

        setSwipeRefreshTheme(swipeRefreshLayout);

        swipeRefreshLayout.setRefreshing(true);
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
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCompleteRequest() {
        super.onCompleteRequest();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onFailed(String msg) {

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        if(msg.equals(ErrorEnum.ERR_PAGE_NEED_LOGIN.getReadable())){
            mIsNeedLogin = true;
            TextView mTvError = new TextView(getContext());
            mTvError.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 600));
            mTvError.setTextSize(25);
            mTvError.setTextColor(R.attr.attr_color_accent);
            mTvError.setGravity(Gravity.CENTER);
            mTvError.setText(msg);
            adapter.setHeaderView(mTvError);
            adapter.notifyDataSetChanged();
        }else{
            ToastUtils.showShort(msg);
        }
        return true;
    }
}
