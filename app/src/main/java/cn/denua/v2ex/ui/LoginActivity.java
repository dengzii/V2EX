package cn.denua.v2ex.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseActivity;
import cn.denua.v2ex.base.BaseNetworkActivity;
import cn.denua.v2ex.interfaces.NextResponseListener;
import cn.denua.v2ex.model.Account;
import cn.denua.v2ex.service.LoginService;
import cn.denua.v2ex.utils.Config;

/*
 * LoginActivity
 *
 * @author denua
 * @date 2018/10/20
 */
public class LoginActivity extends BaseNetworkActivity implements NextResponseListener<Bitmap,Account> {

    public static final int RESULT_SUCCESS = 6;

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_check_code)
    EditText etCaptchaCode;
    @BindView(R.id.iv_captcha)
    ImageView ivCaptcha;

    private LoginService loginService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        ButterKnife.bind(this);

        loginService = new LoginService(this,this);
        loginService.preLogin();
    }

    @OnClick(R.id.bt_login)
    public void login(View view){
        loginService.login(
                etAccount.getText().toString(),
                etPassword.getText().toString(),
                etCaptchaCode.getText().toString());
    }

    @Override
    public void onFailed(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void onNextResult(Bitmap next) {
        ivCaptcha.setImageBitmap(next);
    }

    @Override
    public void onComplete(Account result) {

        Config.account = result;
        setResult(RESULT_SUCCESS);

        finish();
    }
}
