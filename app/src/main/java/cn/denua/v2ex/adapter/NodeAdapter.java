/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.denua.v2ex.R;
import cn.denua.v2ex.model.Node;

/*
 * @author denua
 * @date 2018/12/05 13
 */
public class NodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Node> mNodes;

    public NodeAdapter(List<Node> nodes){
        this.mNodes = nodes;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = new TextView(parent.getContext());
        TextView textView= new TextView(parent.getContext());
        textView.setPadding(10,10,10,10);
        textView.setBackgroundResource(R.drawable.shape_round_corners_5dp);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new NodeViewHolder(view);
    }

    public void setNodes(List<Node> nodes){
        this.mNodes = nodes;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((NodeViewHolder) holder).viewGroup.setText(mNodes.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mNodes.size();
    }

    private static class NodeViewHolder extends RecyclerView.ViewHolder{

        TextView viewGroup;
        NodeViewHolder(View itemView) {
            super(itemView);
            this.viewGroup = (TextView) itemView;
        }
    }
}
