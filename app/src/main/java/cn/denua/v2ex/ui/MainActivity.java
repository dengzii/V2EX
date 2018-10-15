package cn.denua.v2ex.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.denua.v2ex.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

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

//        new Thread(() -> {
//            try {
//                V2EX.test((bitmap) -> runOnUiThread(()-> {
//                    Log.d(TAG, "test: onCaptcha");
//                    ivCaptcha.setImageBitmap(bitmap);
//                }));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
    }
}
