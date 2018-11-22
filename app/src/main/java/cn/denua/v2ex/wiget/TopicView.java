/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.wiget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.base.App;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.ui.NodeActivity;
import cn.denua.v2ex.ui.TopicActivity;
import cn.denua.v2ex.ui.UserDetailActivity;
import cn.denua.v2ex.utils.ImageLoader;

/*
 * Topic 话题列表的 item, 自定义 view
 *
 * @author denua
 * @date 2018/10/31 12
 */
public class TopicView extends FrameLayout {

    @BindView(R.id.iv_user_pic)
    ImageView ivUserPic;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_username)
    TextView tvUsername;

    @BindView(R.id.tv_replay)
    TextView tvReply;
    @BindView(R.id.tv_latest_touched)
    TextView tvLastTouched;
    @BindView(R.id.tv_node)
    TextView tvNode=null;
    @Nullable
    @BindView(R.id.tv_last_reply)
    TextView tvLastReply=null;
    @BindView(R.id.tv_up_vote)
    TextView tvUpVote=null;

    private Context context;
    private Topic topic;
    private boolean mIsSimple;

    @SuppressLint("ClickableViewAccessibility")
    public TopicView(Context context, boolean mIsSimple) {
        super(context);
        this.mIsSimple = mIsSimple;
        this.context = App.getApplication();
        initView();
        setOnClickListener(v -> goToTopicDetail());
        tvTitle.setOnTouchListener((v,e)->{onTouchEvent(e);return false;});
    }

    public TopicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.mIsSimple = false;
        initView();
    }

    private void initView(){
        inflate(context, mIsSimple
                ? R.layout.view_member_topic
                : R.layout.view_topic, this);
        ButterKnife.bind(this);
    }

    public void  loadDataFromTopic(Topic topic){

        if (!mIsSimple){
            String userPicUrl = topic.getMember().getAvatar_large();
            ImageLoader.load(userPicUrl, ivUserPic, this);
            tvUsername.setText(topic.getMember().getUsername());
            tvUsername.setOnClickListener(v->goToUserDetail());
            ivUserPic.setOnClickListener(v -> goToUserDetail());
        }else{
            tvLastReply.setText(topic.getLast_reply_by());
            int upVote = topic.getUpVote();
            tvUpVote.setText(upVote>0?String.valueOf(upVote):"");
        }

        String lastTouched = TimeUtils.getFitTimeSpanByNow(
                topic.getLast_touched()*1000, 4);
        lastTouched = lastTouched.startsWith("-")?lastTouched.substring(1):lastTouched;

        this.topic = topic;
        tvTitle.setText(topic.getTitle());
        tvReply.setText(String.format(getResources().getString(R.string.place_holder_reply), topic.getReplies()));
        tvLastTouched.setText(lastTouched);
        tvNode.setText(topic.getNode().getName());

        tvNode.setOnClickListener(v->goToNodeDetail());

    }



    private void goToUserDetail(){

        UserDetailActivity.start(context, topic.getMember());
    }

    private void goToNodeDetail(){

        Intent intent = new Intent(context, NodeActivity.class);
        intent.putExtra("node", topic.getNode());
        context.startActivity(intent);
    }

    private void goToTopicDetail(){

        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra("topic",topic);
        context.startActivity(intent);
    }

    public void setLastItem() {

        this.tvNode.setVisibility(INVISIBLE);
        TextView textView = new TextView(context);
        textView.setText(R.string.no_more);
        textView.setPadding(0,0,0, 80);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParams);
        addView(textView);
    }
}
