package cn.denua.v2ex.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import cn.denua.v2ex.R;
import cn.denua.v2ex.Tab;
import cn.denua.v2ex.base.BaseActivity;

/**
 * cn.denua.v2ex.ui
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/3/1
 */
public class CustomTabActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private List<Tab> mTabs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecyclerView = find(R.id.rv_list);


    }
}
