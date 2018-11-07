/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.wiget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.model.Reply;

/*
 * reply item view
 *
 * @author denua
 * @date 2018/11/07 21
 */
public class ReplyView extends FrameLayout  {

    @BindView(R.id.iv_user_pic)
    ImageView mUserPic;
    @BindView(R.id.tv_username)
    TextView mUserName;
    @BindView(R.id.tv_ago)
    TextView mAgo;
    @BindView(R.id.tv_via)
    TextView mVia;
    @BindView(R.id.tv_like)
    TextView mLike;
    @BindView(R.id.tv_content)
    TextView mContent;
    @BindView(R.id.tv_floor)
    TextView mFloor;

    private Reply reply;
    private Context context;

    public ReplyView(@NonNull Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ReplyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ReplyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ReplyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setReply(Reply reply){
        this.reply = reply;

        mUserName.setText(reply.getMember().getUsername());
        mAgo.setText(reply.getAgo());
        mVia.setText(reply.getVia());
        mLike.setText(String.valueOf(reply.getLike()));
        mContent.setText(reply.getContent());
        mFloor.setText(String.valueOf(reply.getFloor()));

        Glide.with(context).load(reply.getMember().getAvatar_large()).into(mUserPic);
    }

    private void initView(){

        inflate(context, R.layout.view_reply, this);
        ButterKnife.bind(this);
    }
}
