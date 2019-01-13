/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.Config;
import cn.denua.v2ex.ConfigRefEnum;
import cn.denua.v2ex.R;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.ui.NodeActivity;
import cn.denua.v2ex.ui.UserDetailActivity;
import cn.denua.v2ex.utils.ImageLoader;
import cn.denua.v2ex.utils.StringUtil;

/*
 * Topic 话题列表的 item, 自定义 view
 *
 * @author denua
 * @date 2018/10/31 12
 */
public class TopicView extends FrameLayout {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_reply)
    TextView tvReply;
    @BindView(R.id.tv_latest_touched)
    TextView tvLastTouched;
    @BindView(R.id.tv_node)
    TextView tvNode;

    @Nullable
    @BindView(R.id.iv_user_pic)
    ImageView ivUserPic;
    @Nullable
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @Nullable
    @BindView(R.id.tv_last_reply)
    TextView tvLastReply;
    @Nullable
    @BindView(R.id.tv_up_vote)
    TextView tvUpVote;

    private Context context;
    private Topic topic;
    private boolean isSimpleView;
    private boolean isStop;

    private static boolean mIsChineseNodeLabel;
    private static boolean mIsShowCreateDate;

    @SuppressLint("ClickableViewAccessibility")
    public TopicView(Context context, boolean isSimpleView) {
        super(context);
        this.isSimpleView = isSimpleView;
        this.context = context;
        initView(context);
        tvTitle.setOnTouchListener((v,e)->{onTouchEvent(e);return false;});
    }


    public TopicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.isSimpleView = false;
        this.context = context;
        initView(context);
    }

    private void initView(Context context){

        mIsChineseNodeLabel = Config.getConfig(ConfigRefEnum.CONFIG_NODE_NAME_INSTEAD_TITLE);
        mIsShowCreateDate = Config.getConfig(ConfigRefEnum.CONFIG_TOPIC_CREATE_INSTEAD_TOUCHED);
        inflate(context, isSimpleView
                            ?R.layout.view_member_topic
                            :R.layout.view_topic, this);
        ButterKnife.bind(this);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (topic != null && !isStop)  bindViewWithTopic();
    }

    public void setLastTouched(String lastTouched){
        tvLastTouched.setText(lastTouched);
    }

    public void  setTopic(Topic topic) {
        this.topic = topic;
    }

    public void stop() {
        isStop = true;
    }

    private void bindViewWithTopic(){

        tvTitle.setText(topic.getTitle());
        tvReply.setText(String.format(getResources().getString(R.string.place_holder_reply),
                topic.getReplies()));
        if (topic.getNode().getTitle().equals("")){
            tvNode.setVisibility(INVISIBLE);
        }else {
            tvNode.setText(!mIsChineseNodeLabel
                    ? topic.getNode().getName()
                    : topic.getNode().getTitle());
        }
        tvNode.setOnClickListener(v -> goToNodeDetail());

        if (isSimpleView) {
            tvLastTouched.setText(topic.getAgo());
            if (tvLastReply != null) {
                tvLastReply.setText(topic.getLast_reply_by());
            }
            if (tvUpVote != null) {
                tvUpVote.setText(topic.getUpVote()==0?"":String.valueOf(topic.getUpVote()));
            }
            return;
        }

        String userPicUrl = topic.getMember().getAvatar_large();
        ImageLoader.load(userPicUrl, ivUserPic, this);
        if (tvUsername != null) {
            tvUsername.setText(topic.getMember().getUsername());
        }
        if (topic.getCreated() == 0 && topic.getAgo() != null){
            tvLastTouched.setText(topic.getAgo());
        }else if (topic.getCreated() != 0){
            tvLastTouched.setText(StringUtil.timestampToStr(
                    mIsShowCreateDate ? topic.getCreated() : topic.getLast_touched()));
        }
        tvUsername.setOnClickListener(v -> goToUserDetail());
        if (ivUserPic != null) {
            ivUserPic.setOnClickListener(v -> goToUserDetail());
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private void goToUserDetail(){

        UserDetailActivity.start(context, topic.getMember());
    }

    private void goToNodeDetail(){

        NodeActivity.start(context, topic.getNode());
    }
}
