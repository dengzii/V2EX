/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.service;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Topic;

import static org.junit.Assert.*;

/*
 * test
 *
 * @author denua
 * @date 2018/11/20 17
 */
public class MemberServiceTest {

    @Before
    public void setUp() throws Exception {

        RetrofitManager.init(null);
    }

    @Test
    public void getCreatedTopics() {

        new MemberService(iResponsibleView, new ResponseListener<List<Topic>>() {
            @Override
            public void onFailed(String msg) {
                System.err.println(msg);
            }
            @Override
            public void onComplete(List<Topic> result) {
                for (Topic topic:result){
                    System.out.println(topic);
                }
            }
        }).getCreatedTopics("keelii");
    }


    private IResponsibleView iResponsibleView = new IResponsibleView() {
        @Override
        public void onStartRequest() {
            System.out.println("MemberServiceTest.onStartRequest");;
        }

        @Override
        public void onProcessData(int progress) {
            System.out.println("MemberServiceTest.onProcessData");
        }

        @Override
        public void onCompleteRequest() {
            System.out.println("MemberServiceTest.onCompleteRequest");
        }

        @Override
        public int getContextStatus() {
            return IResponsibleView.VIEW_STATUS_ACTIVATED;
        }
    };
}