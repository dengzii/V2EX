/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.service;

import android.text.Html;

import java.util.List;

import cn.denua.v2ex.api.MemberApi;
import cn.denua.v2ex.base.BaseService;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.http.RxObserver;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.utils.HtmlUtil;
import io.reactivex.disposables.Disposable;

/*
 * 用户相关请求
 *
 * @author denua
 * @date 2018/11/19 23
 */
public class MemberService extends BaseService<IResponsibleView, List<Topic>> {

    private static MemberApi mMemberApi = RetrofitManager.create(MemberApi.class);

    public MemberService(IResponsibleView iResponsibleView, ResponseListener<List<Topic>> responseListener) {
        super(iResponsibleView, responseListener);
    }

    public void getCreatedTopics(String username){

        mMemberApi.getMemberTopics(username)
//                .compose(RxUtil.io2main())
                .subscribe(new RxObserver<String>(){

                    @Override
                    public void onSubscribe(Disposable d) {
                        onStartRequest();
                    }

                    @Override
                    public void _onNext(String s) {
                        returnSuccess(HtmlUtil.getMemberTopic(s));
                    }

                    @Override
                    public void _onError(String msg) {
                        returnFailed(msg);
                    }
                });
    }
}
