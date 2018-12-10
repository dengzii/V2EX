/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.adapter.TopicRecyclerViewAdapter;
import cn.denua.v2ex.base.BaseNetworkFragment;
import cn.denua.v2ex.interfaces.IResponsibleView;
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
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_error)
    TextView mTvError;

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
        if (savedView !=null){
            return savedView;
        }
        savedView = inflater.inflate(R.layout.frag_member_topic, container, false);
        ButterKnife.bind(this, savedView);

        mRecyclerViewAdapter = new TopicRecyclerViewAdapter(getContext(), mTopics);
        mRecyclerViewAdapter.setIsSimpleView(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvTopics.setLayoutManager(layoutManager);
        mRvTopics.setNestedScrollingEnabled(false);
        mRvTopics.setAdapter(mRecyclerViewAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this::onRefresh);
        mSwipeRefreshLayout.setRefreshing(true);
        onRefresh();
        return savedView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void onRefresh(){
        if (mTopics.size() == 0){
            new MemberService(this, this)
                    .getCreatedTopics(mMember, mCurrentPage);
        }else{
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onStartRequest() {
        super.onStartRequest();
    }

    @Override
    public int getContextStatus() {
        return isDetached()
                ? IResponsibleView.VIEW_STATUS_DESTROYED
                : IResponsibleView.VIEW_STATUS_ACTIVATED;
    }

    @Override
    public void onCompleteRequest() {
        super.onCompleteRequest();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onComplete(Member result) {

        this.mTopics = result.getCreatedTopics();
        mRecyclerViewAdapter.addTopics(mTopics);

        mRecyclerViewAdapter.setHeaderView(getTopicListHeaderView(mTopics.size()));
        mRecyclerViewAdapter.notifyDataSetChanged();
    }
    @Override
    public void onFailed(String msg) {

        switch (msg){
            case MemberService.ERR_HAS_HIDDEN:
            case MemberService.ERR_NEED_LOGIN:
                mTvError.setVisibility(View.VISIBLE);
                mTvError.setText(msg);
                break;
            default:
                ToastUtils.showShort(msg);
                break;
        }
    }
    private View getTopicListHeaderView(int topicCount){

        TextView textView = new TextView(getActivity());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMarginStart(15);
        textView.setLayoutParams(layoutParams);
        textView.setText(String.format(
                getResources().getString(R.string.place_holder_topic_count), topicCount));
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        return textView;
    }
}
