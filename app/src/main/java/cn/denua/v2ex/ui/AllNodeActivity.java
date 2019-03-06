/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.adapter.NodeAdapter;
import cn.denua.v2ex.base.BaseNetworkActivity;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Node;
import cn.denua.v2ex.service.NodeService;
import cn.denua.v2ex.widget.LabelLaoutManager;

/*
 * @author denua
 * @date 2018/12/04 19
 */
public class AllNodeActivity extends BaseNetworkActivity implements ResponseListener<List<Node>> {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private NodeAdapter mNodeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_all_node);
        ButterKnife.bind(this);

        mRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(false);

        mRecyclerView.setLayoutManager(new LabelLaoutManager());
        mNodeAdapter = new NodeAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mNodeAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this::onRefresh);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new NodeService<List<Node>>(this).getAllNode(this, this);
    }

    @Override
    public void onStartRequest() {
        super.onStartRequest();
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public int getContextStatus() {
        super.getContextStatus();
        return IResponsibleView.VIEW_STATUS_ACTIVATED;
    }

    @Override
    public void onCompleteRequest() {
        super.onCompleteRequest();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onComplete(List<Node> result) {

        mNodeAdapter.setNodes(result);
        mNodeAdapter.notifyDataSetChanged();
        Logger.d(result.size());
    }

    @Override
    public boolean onFailed(String msg) {
        ToastUtils.showShort(msg);
        return false;
    }

    public void onRefresh(){

    }
}