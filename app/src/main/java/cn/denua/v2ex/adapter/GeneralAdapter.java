package cn.denua.v2ex.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * cn.denua.v2ex.adapter
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/3/6
 */
public class GeneralAdapter<T> extends RecyclerView.Adapter<GeneralViewHolder> {

    private Context mContext;
    private List<T> mData;

    public GeneralAdapter(Context context, List<T> mData){
        this.mContext = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public GeneralViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
