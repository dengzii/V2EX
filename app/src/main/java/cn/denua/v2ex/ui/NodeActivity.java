/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseNetworkActivity;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Node;
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

    @BindView(R.id.iv_node_avatar)
    ImageView mIvAvatar;

    private Node mNode;

    private ResponseListener<Node> mResponseListener = new ResponseListener<Node>() {
        @Override
        public void onComplete(Node result) {
            mTvTitle.setText(String.format("%s\t%s", result.getTitle_alternative(),result.getTitle()));
            mTvSummary.setText(result.getHeader());
            String path =result.getParent_node_name() != null
                    ? (result.getParent_node_name() + " > ")
                    : ""
                    + result.getName();
            mTvPath.setText( path);
            mTvStar.setText(String.format("收藏: %d".toLowerCase(), result.getStarts()));
            mTvTopicCount.setText(String.format("话题数: %d".toLowerCase(), result.getTopics()));
            ImageLoader.load(result.getAvart_large(), mIvAvatar, NodeActivity.this);
        }
        @Override
        public boolean onFailed(String msg) {
            ToastUtils.showShort(msg);
            return false;
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
        NodeService.getNodeInfo(this, mResponseListener, mNode.getName());
    }
}
