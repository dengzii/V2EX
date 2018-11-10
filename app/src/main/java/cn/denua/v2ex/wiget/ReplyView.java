/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.wiget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.blankj.utilcode.util.SpanUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.model.Reply;
import cn.denua.v2ex.ui.UserDetailActivity;
import cn.denua.v2ex.utils.ImageLoader;

/*
 * reply item view
 *
 * @author denua
 * @date 2018/11/07 21
 */
public class ReplyView extends FrameLayout  {

    private static final int TYPE_AT    = 0;
    private static final int TYPE_LINK  = 1;
    private static final int TYPE_IMAGE = 2;

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
    TextView mContent;
    @BindView(R.id.tv_floor)
    TextView mFloor;
    @BindView(R.id.tv_poster)
    TextView mPoster;
    @BindView(R.id.iv_like)
    ImageView mLike;

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
        int like = reply.getLike();

        mUserName.setText(reply.getMember().getUsername());
        mAgo.setText(reply.getAgo());
        mVia.setText(reply.getVia());
        mPoster.setVisibility(reply.isPoster()?VISIBLE:GONE);
        mLikeCount.setText(like==0?"":String.valueOf(like));
        mLike.setOnClickListener(v -> thankReply(reply.getId()));

        mContent.setText(getSpannableReplyContent(reply.getContent()));
        mContent.setMovementMethod(LinkMovementMethod.getInstance());
        mFloor.setText(String.format(getResources().getString(R.string.place_holder_floor), reply.getFloor()));
        mUserName.setOnClickListener(this::goTOUserDetail);
        mUserPic.setOnClickListener(this::goTOUserDetail);

        ImageLoader.load(reply.getMember().getAvatar_large(), mUserPic, this);
    }

    private SpannableStringBuilder getSpannableReplyContent(String content){

        String input = content.replaceAll("<br>","")
                .replace("@\n", "");

        String regex = "<a href=\"/member/\\w+\">(\\w+)</a>" +
                "|<a target=\"_blank\" href=\"(http[^\"]+)\" [^>]+>[^>]+>" +
                "|<img src=\"(http[^\"]+)\" [^>]+>";

        Matcher matcher = Pattern.compile(regex).matcher(input);

        String[] split = input.split(regex);
        SpanUtils spanUtils = new SpanUtils();
        int index=0;

        while (matcher.find()){
            spanUtils.append(split[index++])
                    .setForegroundColor(Color.BLACK);

            com.orhanobut.logger.Logger.d("Group", split[index-1], matcher.group(1), matcher.group(2), matcher.group(3));
            if (matcher.group(1)!=null){
                spanUtils.append("@" + matcher.group(1))
                        .setClickSpan(new ReplyAtMemberClickSpan(matcher.group(1)))
                        .setForegroundColor(Color.RED)
                        .setBold();
            }else if (matcher.group(2) != null){
                spanUtils.append(matcher.group(2))
                        .setUrl(matcher.group(2))
                        .setForegroundColor(Color.BLUE)
                        .setClickSpan(new LinkClickSpan(matcher.group(2)));
            }else if (matcher.group(3) != null){
                spanUtils.appendImage(R.drawable.ic_launcher_background)
                        .setClickSpan(new ImageClickSpan(matcher.group(3)));
            }
        }
        if (index != split.length){
            spanUtils.append(split[index]);
        }
        return spanUtils.create();
    }

    private void thankReply(int id){
        Toast.makeText(context, "Thank you "+id, Toast.LENGTH_SHORT).show();
    }

    private void goTOUserDetail(View view){

        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra("username",reply.getMember().getUsername());
        context.startActivity(intent);
    }

    private void initView(){

        inflate(context, R.layout.view_reply, this);
        ButterKnife.bind(this);
    }

    private class ReplyAtMemberClickSpan extends ClickableSpan{

        private String username;
        ReplyAtMemberClickSpan(String username){
            this.username = username;
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.setColor(Color.BLUE);
        }

        @Override
        public void onClick(@NonNull View widget) {
            Toast.makeText(context, this.username, Toast.LENGTH_SHORT).show();
        }
    }

    private class LinkClickSpan extends ClickableSpan{

        private String mUri;
        LinkClickSpan(String uri){
            this.mUri = uri;
        }
        @Override
        public void onClick(@NonNull View widget) {
            Toast.makeText(context, "Link:"+mUri, Toast.LENGTH_SHORT).show();
        }
    }

    private class ImageClickSpan extends ClickableSpan{

        private String mUri;

        public ImageClickSpan(String mUri) {
            this.mUri = mUri;
        }

        @Override
        public void onClick(@NonNull View widget) {
            Toast.makeText(context, "Image: "+mUri, Toast.LENGTH_SHORT).show();
        }
    }
}
