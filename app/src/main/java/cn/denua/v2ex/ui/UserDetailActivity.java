/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseNetworkActivity;
import cn.denua.v2ex.model.Member;

/*
 * User detail page
 *
 * @author denua
 * @date 2018/11/04 20
 */
public class UserDetailActivity extends BaseNetworkActivity {


    Member mMember;

    public static void start(Context context, Member member){
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra("member", member);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeNoActionBar();
        setContentView(R.layout.act_user_detail);
        this.mMember = getIntent().getParcelableExtra("member");

        setTitle(mMember.getUsername());
    }

}
