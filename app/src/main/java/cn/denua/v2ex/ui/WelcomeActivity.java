package cn.denua.v2ex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;

import cn.denua.v2ex.Config;
import cn.denua.v2ex.base.BaseActivity;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Account;
import cn.denua.v2ex.service.UserService;

public class WelcomeActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeNoActionBar();

        if (!Config.restoreAccount(this)) {
            UserService.getInfo(new ResponseListener<Account>() {
                @Override
                public void onFailed(String msg) {
                    finish(false);
                }
                @Override
                public void onComplete(Account result) {
                    Config.sAccount = result;
                    checkSignIn();
                }
            });
        }else{
            checkSignIn();
        }
    }

    public void finish(boolean s) {
        Config.IsLogin = s;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void checkSignIn(){

        UserService.signIn(true, new ResponseListener<Integer>() {
            @Override
            public void onComplete(Integer result) {
                Config.sSignIn = result;
                finish(true);
            }
            @Override
            public void onFailed(String msg) {
                ToastUtils.showShort(msg);
                finish(true);
            }
        });
    }
}

