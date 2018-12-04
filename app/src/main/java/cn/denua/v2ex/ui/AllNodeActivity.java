/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseNetworkActivity;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Node;
import cn.denua.v2ex.service.NodeService;

/*
 * @author denua
 * @date 2018/12/04 19
 */
public class AllNodeActivity extends BaseNetworkActivity implements ResponseListener<List<Node>> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_all_node);
        ButterKnife.bind(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        new NodeService<List<Node>>(this).getAllNode(this);
    }

    @Override
    public int getContextStatus() {
        return IResponsibleView.VIEW_STATUS_ACTIVATED;
    }

    @Override
    public void onComplete(List<Node> result) {
        Logger.d(result.size());
    }

    @Override
    public void onFailed(String msg) {
        ToastUtils.showShort(msg);
    }
}
