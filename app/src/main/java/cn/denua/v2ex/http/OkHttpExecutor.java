package cn.denua.v2ex.http;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

public class OkHttpExecutor implements Executor{

    private Handler handler;

    public OkHttpExecutor(){
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void execute(@NonNull Runnable command) {
        handler.post(command);
    }
}
