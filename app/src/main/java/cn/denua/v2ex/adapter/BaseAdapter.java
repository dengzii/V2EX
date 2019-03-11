package cn.denua.v2ex.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * cn.denua.v2ex.adapter
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/3/11
 */
public abstract class BaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    private static final int BOTTOM_PADDING = -2;

    private View mBottomPaddingView;
    private boolean mShowMarginBottom = false;

    public void setBottomPaddingView(Context context, int height){

        mBottomPaddingView = new View(context);
        mBottomPaddingView.setLayoutParams(
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        mShowMarginBottom = true;
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    public abstract T onCreateViewHolder2(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        if (mShowMarginBottom && position == getItemCount() - 1){
            return BOTTOM_PADDING;
        }
        return super.getItemViewType(position);
    }
}
