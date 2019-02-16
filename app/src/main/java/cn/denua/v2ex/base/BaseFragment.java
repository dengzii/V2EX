package cn.denua.v2ex.base;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.View;

import com.blankj.utilcode.util.ResourceUtils;
import com.orhanobut.logger.Logger;

import cn.denua.v2ex.R;

public class BaseFragment extends Fragment {


    private String contentType = "None";

    protected View savedView = null;

    public BaseFragment(){}

    protected void setSwipeRefreshTheme(SwipeRefreshLayout swipeRefreshLayout){
        if (!(getActivity() instanceof BaseActivity)){
            return;
        }
        swipeRefreshLayout.setColorSchemeResources(
                getResolveAttrId(R.attr.attr_color_accent),
                getResolveAttrId(R.attr.attr_color_primary_dark));
    }

    protected int getResolveAttrData(int attr){
        TypedValue typedColor = new TypedValue();
        if (getContext() != null){
            getContext().getTheme().resolveAttribute(attr, typedColor, true);
            return typedColor.data;
        }
        return -1;
    }

    protected int getResolveAttrId(int attr){
        TypedValue typedColor = new TypedValue();
        if (getContext() != null){
            getContext().getTheme().resolveAttribute(attr, typedColor, true);
            return typedColor.resourceId;
        }
        return -1;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


}
