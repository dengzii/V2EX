/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SpanUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.Config;
import cn.denua.v2ex.ConfigRefEnum;
import cn.denua.v2ex.R;
import cn.denua.v2ex.model.Reply;
import cn.denua.v2ex.ui.UserDetailActivity;
import cn.denua.v2ex.ui.WebViewActivity;
import cn.denua.v2ex.utils.ImageLoader;

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
    TextView mLikeCount;
    @BindView(R.id.tv_content)
    RichTextView mContent;
    @BindView(R.id.tv_floor)
    TextView mFloor;
    @BindView(R.id.tv_poster)
    TextView mPoster;
    @BindView(R.id.iv_like)
    ImageView mLike;

    private Reply mReply;
    private Context mContext;
    private int mColorLink;
    private int mColorAt;
    private int mColorText;

    public ReplyView(@NonNull Context context) {
        super(context);
        this.mContext = context;

        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.attr_color_text_link, typedValue, true);
        mColorLink = typedValue.data;
        getContext().getTheme().resolveAttribute(R.attr.attr_color_text, typedValue, true);
        mColorText = typedValue.data;
        mColorAt = mColorLink;
        initView();
    }

    public ReplyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ReplyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setReply(Reply reply){
        this.mReply = reply;
        int like = reply.getLike();

        mUserName.setText(reply.getMember().getUsername());
        mAgo.setText(reply.getAgo());
        mVia.setText(reply.getVia());
        mPoster.setVisibility(reply.isPoster()?VISIBLE:GONE);
        mLikeCount.setText(like==0?"":String.valueOf(like));
        mLike.setOnClickListener(v -> thankReply(reply.getId()));

        mContent.setText(getSpannableReplyContent(reply.getContent()));
        mContent.setMovementMethod(LinkMovementMethod.getInstance());
        mFloor.setText(String.format(getResources().getString(R.string.place_holder_floor),
                reply.getFloor()+1));
        mUserName.setOnClickListener(this::goToUserDetail);
        mUserPic.setOnClickListener(this::goToUserDetail);

        mContent.setLineSpacing(0, Config.getConfig(ConfigRefEnum.CONFIG_REPLY_LINE_HEIGHT));

        ImageLoader.load(reply.getMember().getAvatar_large(), mUserPic, this);
    }

    private SpannableStringBuilder getSpannableReplyContent(String content){

        String input = content.replaceAll("<br>","")
                                .replace("@\n", "");

        String regex =  "<a href=\"/member/\\w+\">(\\w+)</a>" +
                        "|<a target=\"_blank\" href=\"(http[^\"]+)\" [^>]+>[^>]+>" +
                        "|<img src=\"(http[^\"]+)\" [^>]+>";

        Matcher matcher = Pattern.compile(regex).matcher(input);
        String[] split = input.split(regex);
        SpanUtils spanUtils = new SpanUtils();

        int index=0;

        while (matcher.find()){
            if (split.length != 0){
                spanUtils.append(split[index++]).setForegroundColor(mColorText);
            }
            if (matcher.group(1)!=null){
                spanUtils.append("@" + matcher.group(1))
                        .setClickSpan(new ReplyAtMemberClickSpan(matcher.group(1)));
            }else if (matcher.group(2) != null){
                spanUtils.append(matcher.group(2))
                        .setClickSpan(new LinkClickSpan(matcher.group(2)));
            }else if (matcher.group(3) != null){
                spanUtils.appendImage(R.drawable.ic_launcher_round)
                        .setClickSpan(new ImageClickSpan(matcher.group(3)));
            }
        }
        if (index != split.length){
            spanUtils.append(split[index]);
        }
        return spanUtils.create();
    }

    private void thankReply(int id){
        Toast.makeText(mContext, "Thank you "+id, Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("unused")
    private void goToUserDetail(View view){

        UserDetailActivity.start(mContext, mReply.getMember());
    }

    private void initView(){

        inflate(mContext, R.layout.view_reply, this);
        ButterKnife.bind(this);
    }

    private class ReplyAtMemberClickSpan extends ClickableSpan{

        private String username;
        ReplyAtMemberClickSpan(String username){
            this.username = username;
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.setColor(mColorAt);
            ds.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }

        @Override
        public void onClick(@NonNull View widget) {
            Toast.makeText(mContext, this.username, Toast.LENGTH_SHORT).show();
        }
    }

    private class LinkClickSpan extends ClickableSpan{

        private String mUri;
        LinkClickSpan(String uri){
            this.mUri = uri;
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.setColor(mColorLink);
            ds.setUnderlineText(true);
        }

        @Override
        public void onClick(@NonNull View widget) {
            WebViewActivity.start(getContext(), mUri);
        }
    }

    class ImageClickSpan extends ClickableSpan{

        private String mUri;

        ImageClickSpan(String mUri) {
            this.mUri = mUri;
        }

        @Override
        public void onClick(@NonNull View widget) {
            WebViewActivity.start(getContext(), mUri);
        }
    }
}
