package cn.denua.v2ex.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * cn.denua.v2ex.adapter
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/3/6
 */
public class GeneralViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mItemView;
    private View mConvertView;
    private Context mContext;

    public GeneralViewHolder(View itemView) {
        super(itemView);
        this.mConvertView = itemView;
    }

}
