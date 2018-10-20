package cn.denua.v2ex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.iv_captcha)
    public ImageView ivCaptcha;
    @BindView(R.id.et_check_code)
    public EditText etCheckCode;
    @BindView(R.id.webview)
    public WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        ButterKnife.bind(this);

    }

    @OnClick(R.id.bt_test)
    public void test(View view){

        startActivity(new Intent(this, LoginActivity.class));
    }
}
