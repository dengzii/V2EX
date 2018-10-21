package cn.denua.v2ex.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseActivity;
import cn.denua.v2ex.model.Account;
import cn.denua.v2ex.service.LoginListener;
import cn.denua.v2ex.service.LoginService;

/*
 * LoginActivity
 *
 * @author denua
 * @date 2018/10/20
 */
public class LoginActivity extends BaseActivity implements LoginListener {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_check_code)
    EditText etCaptachCode;
    @BindView(R.id.iv_captcha)
    ImageView ivCaptcha;

    private LoginService loginService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        ButterKnife.bind(this);

        loginService = new LoginService(this);
        loginService.preLogin();
    }

    @OnClick(R.id.bt_login)
    public void login(View view){
        loginService.login(
                etAccount.getText().toString(),
                etPassword.getText().toString(),
                etCaptachCode.getText().toString());
    }

    @Override
    public void onCaptcha(Bitmap bitmap) {
        ivCaptcha.setImageBitmap(bitmap);
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(Account accountModel) {
        Toast.makeText(this, accountModel.getId() + " - " + accountModel.getJoin(), Toast.LENGTH_SHORT).show();
    }
}
