package cn.denua.v2ex.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cn.denua.v2ex.Config;
import cn.denua.v2ex.ConfigRefEnum;
import cn.denua.v2ex.R;
import cn.denua.v2ex.Tab;
import cn.denua.v2ex.TabEnum;
import cn.denua.v2ex.adapter.TabSelectAdapter;
import cn.denua.v2ex.base.BaseActivity;
import cn.denua.v2ex.utils.DialogUtil;

/**
 * cn.denua.v2ex.ui
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/3/1
 */
public class CustomTabActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private TabSelectAdapter mTabSelectAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Tab> mTabs = new ArrayList<>();

    private int mFrom = -1;
    private View mDragView;

    public static void start(Context context){
        context.startActivity(new Intent(context, CustomTabActivity.class));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_custom_tab);
        setTitle(R.string.summary_home_tab);

        mRecyclerView = find(R.id.rv_list);
        mDragView = find(R.id.drag_view);
        mDragView.setAlpha(0.7F);

        mTabs = Config.getConfig(ConfigRefEnum.CONFIG_HOME_TAB);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTabSelectAdapter = new TabSelectAdapter(mTabs, CustomTabActivity.this::onDrag);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mTabSelectAdapter);

        mRecyclerView.setOnTouchListener((view, motionEvent) -> {

            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mFrom != -1){
                        move(motionEvent);
                        return true;
                    }
                    break;
                default:
                    mDragView.setVisibility(View.GONE);
                    if ( mFrom != -1) swipe(motionEvent);
                    mFrom = -1;
                    break;
            }
            return false;
        });
    }

    @Override
    protected void onDestroy() {

        Set<String> tabSet = new HashSet<>();
        int i = 0;
        for (Tab tab:mTabs){
            tab.setIndex(i++);
            tabSet.add(new Gson().toJson(tab));
        }
        SharedPreferences.Editor editor = Config.getConfSP().edit();
        editor.putStringSet(ConfigRefEnum.CONFIG_HOME_TAB.getKey(), tabSet);
        editor.apply();
        Config.setConfig(ConfigRefEnum.CONFIG_HOME_TAB, mTabs);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,Menu.NONE,0, "addTab");
        menu.add(Menu.NONE, Menu.NONE, 1,"addNode");
        menu.getItem(0).setOnMenuItemClickListener(item -> {
            DialogUtil.showInputDialog(this, "Add By Tab", "建议格式: 标题_英文名称",
                    "创意_creative",
                    value -> {
                        String[] s = value.split("_");
                        mTabs.add(new Tab(TabEnum.TAB, s[0], s.length == 2 ? s[1] : s[0]));
                        mTabSelectAdapter.notifyItemInserted(mTabs.size());
                    });
            return false;
        });
        menu.getItem(1).setOnMenuItemClickListener(item -> {
            DialogUtil.showInputDialog(this, "Add By Node", "建议格式: 标题_英文名称",
                    "问与答_qna",
                    value -> {
                        String[] s = value.split("_");
                        mTabs.add(new Tab(TabEnum.NODE, s[0], s.length == 2 ? s[1] : s[0]));
                        mTabSelectAdapter.notifyItemInserted(mTabs.size());
            });
            return false;
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void onDrag(int pos){
        mFrom = pos;
    }

    private void move(MotionEvent motionEvent){
        mDragView.setY(motionEvent.getY() - mDragView.getHeight() / 2);
        if (mDragView.getVisibility() != View.VISIBLE){
            ((TextView) mDragView.findViewById(R.id.tv_title)).setText(mTabs.get(mFrom).toString());
            mDragView.setVisibility(View.VISIBLE);
        }
        int lastItemPosition = mLayoutManager.findLastVisibleItemPosition();
        View lastVisibleItem = mLayoutManager.findViewByPosition(lastItemPosition);
        if (lastVisibleItem == null)    return;

        if (motionEvent.getY() > lastVisibleItem.getY()){
            mRecyclerView.smoothScrollBy(mRecyclerView.getScrollX(), mRecyclerView.getScrollY() + 20);
        }
    }

    private void swipe(MotionEvent motionEvent){
        int firstVisibleItem = mLayoutManager.findFirstCompletelyVisibleItemPosition();

        View firstItem = mLayoutManager.findViewByPosition(firstVisibleItem);
        float span = 0;
        if (firstItem != null){
            span = firstItem.getY() <= 0 ? firstItem.getY() : 0;
        }

        float y = (motionEvent.getY()) + span;
        int itemInScreen = (int) y / mDragView.getHeight();
        int moveTo = itemInScreen + firstVisibleItem;
        if (mFrom != moveTo) {
            if (mFrom > moveTo){
                int temp = mFrom;
                mFrom = moveTo;
                moveTo = temp;
            }
            if (mFrom < 0 || moveTo >= mTabs.size()){
                return;
            }

            mTabs.add(mFrom, mTabs.get(moveTo));
            mTabs.add(moveTo + 1, mTabs.get(mFrom + 1));
            mTabs.remove(mFrom + 1);
            mTabs.remove(moveTo + 1);

            mTabSelectAdapter.notifyItemMoved(mFrom, moveTo);
            mTabSelectAdapter.notifyItemRangeChanged(mFrom, moveTo - mFrom + 1);
        }
    }

}
