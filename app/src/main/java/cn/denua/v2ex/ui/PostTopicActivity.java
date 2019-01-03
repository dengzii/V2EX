/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.denua.v2ex.Config;
import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseNetworkActivity;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Account;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.service.TopicService;
import cn.denua.v2ex.utils.DialogUtil;

/*
 * @author denua
 * @email denua@foxmail.com
 * @date 2018/12/30 12
 */
public class PostTopicActivity extends BaseNetworkActivity implements ResponseListener<List<Topic>> {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.et_title)
    EditText mEtTitle;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.tv_node)
    TextView mTvNode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeNoActionBar();
        setContentView(R.layout.act_post_topic);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);
        mTvNode.setOnClickListener(this::selectNode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_post_topic, menu);
        menu.findItem(R.id.it_confirm).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        new TopicService(this, this).postTopic(mEtTitle.getText().toString(),
                mEtContent.getText().toString(),
                mTvNode.getText().toString());
        return super.onOptionsItemSelected(item);
    }

    public void selectNode(View view){

        DialogUtil.showInputDialog(
                this, "输入节点名称", null, "sandbox", value -> {
                    mTvNode.setText(value);
                });
    }

    @Override
    public void onComplete(List<Topic> result) {
        TopicActivity.start(this, result.get(0).getId());
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
