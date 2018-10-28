package cn.denua.v2ex.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.d("onCreate");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Logger.d("onAttach");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d("onResume");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d("onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.e("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Logger.e("onDetach");
    }



}
