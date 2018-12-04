/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import cn.denua.v2ex.api.NodeApi;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Node;
import cn.denua.v2ex.utils.RxUtil;

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

    public void getAllNode(ResponseListener<T> responseListener){

        setResponseListener(responseListener);
        mNodeApi.getAllNode()
                .compose(RxUtil.io2main())
                .subscribe(new RxObserver<JsonArray>(this) {
                    @Override
                    public void _onNext(JsonArray jsonElements) {
                        List<Node> nodes = new ArrayList<>(jsonElements.size());
                        Gson gson = new Gson();
                        for (JsonElement jsonObject:jsonElements){
                            nodes.add(gson.fromJson(jsonObject, Node.class));
                        }
                        returnSuccess((T) nodes);
                    }
                });
    }
}
