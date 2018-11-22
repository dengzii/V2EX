/*
 * Copyright (c) 2018 denua.
 */

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
import cn.denua.v2ex.adapter.TopicRecyclerViewAdapter;
import cn.denua.v2ex.base.BaseNetworkFragment;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Member;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.service.MemberService;

/*
 * Member topic fragment for member detail activity
 *
 * @author denua
 * @date 2018/11/16 15
 */
public class MemberTopicFragment extends BaseNetworkFragment implements ResponseListener<Member> {

    @BindView(R.id.rv_topics)
    RecyclerView mRvTopics;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    private TopicRecyclerViewAdapter mRecyclerViewAdapter;
    private List<Topic> mTopics = new ArrayList<>();
    private Member mMember;

    private int mCurrentPage = 0;

    public static MemberTopicFragment create(Member member){

        MemberTopicFragment memberTopicFragment = new MemberTopicFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("member", member);
        memberTopicFragment.setArguments(bundle);
        return memberTopicFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mMember = getArguments() != null ? getArguments().getParcelable("member") : null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_member_topic, container, false);
        ButterKnife.bind(this, view);

        mRecyclerViewAdapter = new TopicRecyclerViewAdapter(getContext(), mTopics);
        mRecyclerViewAdapter.setIsSimpleView(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        mRvTopics.setLayoutManager(layoutManager);
        mRvTopics.setNestedScrollingEnabled(true);
        mRvTopics.setAdapter(mRecyclerViewAdapter);

        mRefreshLayout.setOnRefreshListener(this::onRefresh);
//        mRefreshLayout.setRefreshing(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void onRefresh(){

        new MemberService(this, this)
                .getCreatedTopics(mMember, mCurrentPage);
    }

    @Override
    public void onCompleteRequest() {
        super.onCompleteRequest();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onComplete(Member result) {
        ToastUtils.showShort("success");
        this.mTopics = result.getCreatedTopics();
        mRecyclerViewAdapter.setTopics(mTopics);
        mRecyclerViewAdapter.notifyDataSetChanged();

    }

    @Override
    public void onFailed(String msg) {
        ToastUtils.showShort(msg);
    }
}
