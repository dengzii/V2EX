/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import cn.denua.v2ex.api.MemberApi;
import cn.denua.v2ex.base.BaseService;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.http.RxObserver;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Member;
import cn.denua.v2ex.utils.HtmlUtil;
import cn.denua.v2ex.utils.RxUtil;
import io.reactivex.disposables.Disposable;

/*
 * 用户相关请求
 *
 * @author denua
 * @date 2018/11/19 23
 */
public class MemberService extends BaseService<IResponsibleView, Member> {

    public static final String ERR_NEED_LOGIN = "主题列表只有在你登录之后才可查看";
    public static final String ERR_HAS_HIDDEN = "主题列表被隐藏";

    private static MemberApi mMemberApi = RetrofitManager.create(MemberApi.class);

    public MemberService(IResponsibleView iResponsibleView, ResponseListener<Member> responseListener) {
        super(iResponsibleView, responseListener);
    }

    public void getMemberDetail(Member member){

        Member memberCopy = (Member) member.clone();
        mMemberApi.getMemberPage(memberCopy.getUsername())
                .compose(RxUtil.io2main())
                .subscribe(new RxObserver<String>() {
                    @Override
                    public void _onNext(String s) {

                    }
                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    public void getMemberDetail(String username){

        mMemberApi.getMember(username)
                .compose(RxUtil.io2main())
                .subscribe(new RxObserver<JsonObject>() {
                    @Override
                    public void _onNext(JsonObject jsonObject) {
                        Member member = new Gson().fromJson(jsonObject, Member.class);
                        returnSuccess(member);
                    }
                    @Override
                    public void _onError(String msg) {
                        returnFailed(msg);
                    }
                });
    }

    public void getCreatedTopics(Member member, int page){

        final Member member1 = (Member) member.clone();
        mMemberApi.getMemberTopics(member1.getUsername(), page)
                .compose(RxUtil.io2main())
                .subscribe(new RxObserver<String>(){
                    @Override
                    public void _onNext(String s) {
                        if (!verify(s)){
                            return;
                        }
                        HtmlUtil.attachCreatedTopics(member1, s);
                        returnSuccess(member1);
                    }
                    @Override
                    public void _onError(String msg) {
                        returnFailed(msg);
                    }
                });
    }

    private boolean verify(String html){

        if (html.contains(ERR_NEED_LOGIN)){
            returnFailed(ERR_NEED_LOGIN);
            return false;
        }else if (html.contains(ERR_HAS_HIDDEN)){
            returnFailed(ERR_HAS_HIDDEN);
            return false;
        }
        return true;
    }
}
