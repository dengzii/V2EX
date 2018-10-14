package cn.denua.v2ex.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.denua.v2ex.R;
import cn.denua.v2ex.api.V2EX;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.iv_captcha)
    public ImageView ivCaptcha;
    @BindView(R.id.et_check_code)
    public EditText etCheckCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        ButterKnife.bind(this);
    }
    @OnClick(R.id.bt_test)
    public void test(View view){

        V2EX.login("", "");
    }
}
