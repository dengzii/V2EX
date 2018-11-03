/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseNetworkActivity;
import cn.denua.v2ex.model.Node;

/*
 * Node
 *
 * @author denua
 * @date 2018/11/01 10
 */
public class NodeActivity extends BaseNetworkActivity {

    private Node node;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_node);

        node = getIntent().getParcelableExtra("node");
    }
}
