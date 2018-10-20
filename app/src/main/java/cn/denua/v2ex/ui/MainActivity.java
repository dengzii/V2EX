package cn.denua.v2ex.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.denua.v2ex.R;
import cn.denua.v2ex.api.V2EX;
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

        new Thread(() -> V2EX.preLogin(MainActivity.this::setIvCaptcha)).start();

    }

    public void setIvCaptcha(Bitmap bitmap){
        runOnUiThread(()->ivCaptcha.setImageBitmap(bitmap));
    }

    @OnClick(R.id.bt_test)
    public void test(View view){

        new Thread(()->V2EX.login("denua1","vxmm1713.", etCheckCode.getText().toString())).start();
//        webView.loadUrl("https://www.v2ex.com/signin");
//        WebSettings settings = webView.getSettings();
//        settings.setAllowContentAccess(true);
//        settings.setAppCachePath(getCacheDir().getAbsolutePath());
//        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 7.0; PLUS Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.98 Mobile Safari/537.36");
    }
}
