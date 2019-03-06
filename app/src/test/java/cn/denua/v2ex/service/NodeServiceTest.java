package cn.denua.v2ex.service;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Node;

import static org.junit.Assert.*;

/**
 * cn.denua.v2ex.service
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/3/6
 */
public class NodeServiceTest {

    @Before
    public void init(){
        RetrofitManager.init(null);
    }

    @Test
    public void getNodeInfo(){

        NodeService.getNodeInfo(new IResponsibleView() {
            @Override
            public void onStartRequest() {
                System.out.println("NodeServiceTest.onStartRequest");
            }
            @Override
            public void onProcessData(int progress) {
                System.out.println("NodeServiceTest.onProcessData");
            }
            @Override
            public void onCompleteRequest() {
                System.out.println("NodeServiceTest.onCompleteRequest");
            }
            @Override
            public void onFailMsg(String msg) {
                System.err.println(msg);
            }

            @Override
            public int getContextStatus() {
                return IResponsibleView.VIEW_STATUS_ACTIVATED;
            }

            @Override
            public Context getContext() {
                return null;
            }
        }, new ResponseListener<Node>() {
            @Override
            public void onComplete(Node result) {
                System.out.println(result);
            }

            @Override
            public boolean onFailed(String msg) {
                System.out.println(msg);
                return false;
            }
        }, "lol");
    }

    @Test
    public void getAllNode() {

    }

}