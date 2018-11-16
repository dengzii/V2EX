/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseNetworkFragment;

/*
 *
 *
 * @author denua
 * @date 2018/11/16 15
 */
public class MemberTopicFragment extends BaseNetworkFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_member_topic, container, false);
        return view;
    }
}
