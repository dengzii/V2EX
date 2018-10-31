/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.wiget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.R;

/*
 * Topic 话题列表的 item, 自定义 view
 *
 * @author denua
 * @date 2018/10/31 12
 */
public class TopicView extends FrameLayout {

    @BindView(R.id.iv_user_pic)
    private ImageView ivUserPic;
    @BindView(R.id.tv_title)
    private TextView tvTitle;
    @BindView(R.id.tv_username)
    private TextView tvUsername;
    @BindView(R.id.tv_replay)
    private TextView tvReply;
    @BindView(R.id.tv_latest_touched)
    private TextView tvLastTouched;
    @BindView(R.id.tv_node)
    private TextView tvNode;

    public TopicView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_topic, this,true);
        ButterKnife.bind(this);
    }

    public TopicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_topic, this,true);
        ButterKnife.bind(this);
    }

    public TopicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setData(String ... args){
        tvTitle.setText(args[0]);
        tvUsername.setText(args[1]);
        tvReply.setText(args[2]);
        tvLastTouched.setText(args[3]);
        tvNode.setText(args[4]);
    }

    public ImageView getIvUserPic(){
        return this.ivUserPic;
    }
}
