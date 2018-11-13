package cn.denua.v2ex.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseNetworkActivity;
import cn.denua.v2ex.interfaces.NextResponseListener;
import cn.denua.v2ex.model.Account;
import cn.denua.v2ex.service.LoginService;
import cn.denua.v2ex.Config;
import cn.denua.v2ex.wiget.ProgressDialog;

/*
 * LoginActivity
 *
 * @author denua
 * @date 2018/10/20
 */
public class LoginActivity extends BaseNetworkActivity implements NextResponseListener<Bitmap,Account> {

    public static final int RESULT_SUCCESS = 6;

    @BindView(R.id.et_account)
    TextInputEditText etAccount;
    @BindView(R.id.et_password)
    TextInputEditText etPassword;
    @BindView(R.id.et_check_code)
    TextInputEditText etCaptchaCode;
    @BindView(R.id.iv_captcha)
    ImageView ivCaptcha;
    @BindView(R.id.progress_captcha)
    ProgressBar progressBar;
    private LoginService loginService;

    private ProgressDialog progressDialog = new ProgressDialog();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        ButterKnife.bind(this);

        setTitle(R.string.login);
        progressDialog.setTitle(getResources().getString(R.string.logging_in));
        loginService = new LoginService<>(this,this);
        loginService.preLogin();
        ivCaptcha.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.bt_login)
    public void login(View view){

        String mAccount = etAccount.getText().toString().trim();
        String mPassword = etPassword.getText().toString().trim();
        String mCaptcha = etCaptchaCode.getText().toString().trim();

        if (mAccount.equals("")){
            etAccount.setError(getString(R.string.captcha_format_error));
            return;
        }
        if (mPassword.equals("")){
            etPassword.setError(getString(R.string.password_format_error));
            return;
        }
        if (mCaptcha.equals("")){
            etCaptchaCode.setError(getString(R.string.captcha_format_error));
            return;
        }

        loginService.login(
                etAccount.getText().toString(),
                etPassword.getText().toString(),
                etCaptchaCode.getText().toString());
        progressDialog.show(getSupportFragmentManager(), "login_progress");
    }

    @OnClick(R.id.iv_captcha)
    public void refresh(ImageView view){
        loginService.preLogin();
        ivCaptcha.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStartRequest() {
        super.onStartRequest();

    }

    @Override
    public void onCompleteRequest() {
        super.onCompleteRequest();
    }

    @Override
    public void onFailed(String msg) {
        if (progressDialog !=null && progressDialog.isAdded()){
            progressDialog.dismiss();
        }
        if (msg.equals(LoginService.STATUS_WRONG_FIELDS)) {
            loginService.preLogin();
        }
        progressBar.setVisibility(View.GONE);
        ToastUtils.showShort(msg);
    }

    @Override
    public void onNextResult(Bitmap next) {
        ivCaptcha.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        ivCaptcha.setImageBitmap(next);
    }

    @Override
    public void onComplete(Account result) {

        progressDialog.dismiss();

        Config.account = result;
        Config.IsLogin = true;
        setResult(RESULT_SUCCESS);
        Config.persistentAccount();
        finish();
    }
}
