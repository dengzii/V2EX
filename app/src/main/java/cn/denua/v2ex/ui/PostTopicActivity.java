/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.base.BaseNetworkActivity;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.service.TopicService;

/*
 * @author denua
 * @email denua@foxmail.com
 * @date 2018/12/30 12
 */
public class PostTopicActivity extends BaseNetworkActivity implements ResponseListener<List<Topic>> {

    @BindView(R.id.et_title)
    EditText mEtTitle;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.et_node)
    EditText mEtNode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_post_topic);
        ButterKnife.bind(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_post_topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        new TopicService(this, this).postTopic(mEtTitle.getText().toString(),
                mEtContent.getText().toString(),
                mEtNode.getText().toString());
        return super.onOptionsItemSelected(item);
    }

    public void confirm(){


    }

    @Override
    public void onComplete(List<Topic> result) {
        TopicActivity.start(this, result.get(0));
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
