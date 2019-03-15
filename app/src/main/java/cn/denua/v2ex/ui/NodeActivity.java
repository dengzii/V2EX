/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.denua.v2ex.R;
import cn.denua.v2ex.adapter.PullRefreshReplyAdapter;
import cn.denua.v2ex.adapter.TopicRecyclerViewAdapter;
import cn.denua.v2ex.base.BaseNetworkActivity;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Node;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.service.NodeService;
import cn.denua.v2ex.utils.ImageLoader;

/*
 * Node
 *
 * @author denua
 * @date 2018/11/01 10
 */
public class NodeActivity extends BaseNetworkActivity {

    @BindView(R.id.tv_node_title)
    TextView mTvTitle;
    @BindView(R.id.tv_node_summary)
    TextView mTvSummary;
    @BindView(R.id.tv_node_path)
    TextView mTvPath;
    @BindView(R.id.tv_node_topic_count)
    TextView mTvTopicCount;
    @BindView(R.id.tv_star)
    TextView mTvStar;
    @BindView(R.id.tv_node_name)
    TextView mTvName;

    @BindView(R.id.iv_node_avatar)
    ImageView mIvAvatar;

    @BindView(R.id.rv_list)
    RecyclerView mRvList;

    private PullRefreshReplyAdapter mRefreshAdapter;
    private Node mNode;

    private int mCurrentPage = 1;
    private int mPageCount =  0;
    private List<Topic> mTopic = new ArrayList<>();

    private ResponseListener<Node> mResponseListener = new ResponseListener<Node>() {
        @Override
        public void onComplete(Node result) {

            mNode = result;
            mTvTitle.setText(result.getTitle());
            mTvSummary.setText(result.getHeader());
            String path = (result.getParent_node_name() != null
                    ? (result.getParent_node_name() + " > ")
                    : "") + result.getName();
            mTvPath.setText( path);
            mTvStar.setText(String.format("收藏: %d".toLowerCase(), result.getStarts()));
            mTvTopicCount.setText(String.format("话题数: %d".toLowerCase(), result.getTopics()));
            ImageLoader.load(result.getAvatar_large(), mIvAvatar, NodeActivity.this);
            mTvName.setText(mNode.getTitle_alternative());
            mPageCount = result.getTopics() / 20;
        }
        @Override
        public boolean onFailed(String msg) {
            ToastUtils.showShort(msg);
            return true;
        }
    };

    private ResponseListener<List<Topic>> mTopicListener = new ResponseListener<List<Topic>>() {
        @Override
        public void onComplete(List<Topic> result) {
            mTopic.addAll(result);
            mRefreshAdapter.setStatus(mPageCount == mCurrentPage
                            ? PullRefreshReplyAdapter.FooterStatus.COMPLETE
                            : PullRefreshReplyAdapter.FooterStatus.LOADING);
            mRefreshAdapter.notifyRangeChanged(mTopic.size() - result.size(), result.size());
        }
        @Override
        public boolean onFailed(String msg) {
            ToastUtils.showShort(msg);
            return true;
        }
    };

    public static void start(Context context, Node node){

        Intent intent = new Intent(context, NodeActivity.class);
        intent.putExtra("node", node);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNoToolbar();
        setContentView(R.layout.act_node);
        ButterKnife.bind(this);

        mNode = getIntent().getParcelableExtra("node");

        mRefreshAdapter = new PullRefreshReplyAdapter(
                this, new TopicRecyclerViewAdapter(this, mTopic));
        mRefreshAdapter.setBottomPadding(mNavBarHeight);
        mRefreshAdapter.setOnPullUpListener(() -> {
            mCurrentPage ++;
            NodeService.getNodeTopicList(this, mTopicListener, mNode.getName(), mCurrentPage);
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvList.setLayoutManager(layoutManager);
        mRvList.setAdapter(mRefreshAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onRefresh();
        NodeService.getNodeTopicList(this, mTopicListener, mNode.getName(), mCurrentPage);
    }

    @OnClick(R.id.fb_follow)
    public void follow(){
        ToastUtils.showShort("follow");
    }

    private void onRefresh(){
        NodeService.getNodeInfo(this, mResponseListener, mNode.getName());
    }
}
