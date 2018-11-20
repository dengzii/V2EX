/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.service;

import cn.denua.v2ex.api.MemberApi;
import cn.denua.v2ex.base.BaseService;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.model.Member;

/*
 * 用户相关请求
 *
 * @author denua
 * @date 2018/11/19 23
 */
public class MemberService extends BaseService<IResponsibleView, Member> {

    public static MemberApi mMemberApi = RetrofitManager.create(MemberApi.class);

    public void getCreatedTopics(String username){


    }
}
