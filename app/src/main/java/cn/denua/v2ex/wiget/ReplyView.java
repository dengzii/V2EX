/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.wiget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.blankj.utilcode.util.SpanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
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
    TextView mLike;
    @BindView(R.id.tv_content)
    TextView mContent;
    @BindView(R.id.tv_floor)
    TextView mFloor;
    @BindView(R.id.tv_poster)
    TextView mPoster;

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
        mPoster.setVisibility(reply.isPoster()?VISIBLE:GONE);
        mLike.setText(String.valueOf(reply.getLike()));
        mContent.setText(getFormatReplyContent(reply.getContent()));
        mContent.setMovementMethod(LinkMovementMethod.getInstance());
        mContent.setHighlightColor(Color.parseColor("#ff0000"));
        mFloor.setText(String.format(getResources().getString(R.string.place_holder_floor), reply.getFloor()));
        mUserName.setOnClickListener(this::goTOUserDetail);
        mUserPic.setOnClickListener(this::goTOUserDetail);
        ImageLoader.load(reply.getMember().getAvatar_large(), mUserPic, this);
    }

    private SpannableString getFormatReplyContent(String content){

        String mContent = content.replace("<br>", "")
                                 .replace("@\n","@")
                                 .replaceAll("<img src[^>]+>","[IMAGE]")
                                 .replaceAll("<a target[^>]+>http[^<]+</a>", "[LINK]");

        String regex = "<a href=\"/member/\\w+\">(\\w+)</a>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mContent);

        List<int[]> group = new ArrayList<>();
        Queue<String> at = new PriorityQueue<>();
        while (matcher.find()){
            String result = matcher.group(1);
            mContent = mContent.replaceFirst(regex, result);

            int start = matcher.start();
            int end = start + result.length();
            at.add(result);
            group.add(new int[]{start-1, end});
        }
        SpannableString spannableString = SpannableString.valueOf(mContent);

        UnderlineSpan underlineSpan = new UnderlineSpan(){
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(Color.RED);
                ds.setUnderlineText(false);
            }
        };
        for (int[] item:group){
            int start   = item[0];
            int end     = item[1];

            spannableString.setSpan(
                    new ReplyAtMemberClickSpan(at.poll()),
                    start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(
                    new StyleSpan(Typeface.BOLD),
                    start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(
                    underlineSpan,
                    start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return spannableString;
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
}
