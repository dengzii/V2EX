/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import cn.denua.v2ex.api.NodeApi;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Node;
import cn.denua.v2ex.model.Topic;
import cn.denua.v2ex.utils.HtmlUtil;
import cn.denua.v2ex.utils.RxUtil;
import io.reactivex.functions.Function;

/*
 * @author denua
 * @date 2018/12/04 19
 */
@SuppressWarnings("unchecked")
public class NodeService<T> extends BaseService<T> {

    private static NodeApi mNodeApi = RetrofitManager.create(NodeApi.class);

    public NodeService(IResponsibleView iResponsibleView){
        super(iResponsibleView);
    }

    public static void getAllNode(IResponsibleView iResponsibleView,
                                  ResponseListener<List<Node>> responseListener){

        mNodeApi.getAllNode()
                .compose(RxUtil.io2main())
                .map(jsonElements -> {
                    List<Node> nodes = new ArrayList<>(jsonElements.size());
                    Gson gson = new Gson();
                    for (JsonElement jsonObject:jsonElements){
                        nodes.add(gson.fromJson(jsonObject, Node.class));
                    }
                    return nodes;
                })
                .subscribe(new RxObserver2<>(iResponsibleView, responseListener));
    }

    public static void getNodeInfo(IResponsibleView iResponsibleView,
                                   ResponseListener<Node> responseListener,
                                   String name){
        mNodeApi.getNodeInfo(name)
                .compose(RxUtil.io2main())
                .map(jsonObject -> new Gson().fromJson(jsonObject, Node.class))
                .subscribe(new RxObserver2<>(iResponsibleView, responseListener));
    }

    public static void getNodeTopicList(IResponsibleView iResponsibleView,
                                        ResponseListener<List<Topic>> responseListener,
                                        String name,
                                        int page){

        mNodeApi.getNode(name, page)
                .compose(RxUtil.io2main())
                .map(HtmlUtil::getTopics)
                .subscribe(new RxObserver2<>(iResponsibleView, responseListener));
    }
}
