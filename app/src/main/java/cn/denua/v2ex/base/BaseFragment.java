package cn.denua.v2ex.base;


import android.support.v4.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment {


    private String contentType = "None";

    protected View savedView = null;

    public BaseFragment(){}


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


}
