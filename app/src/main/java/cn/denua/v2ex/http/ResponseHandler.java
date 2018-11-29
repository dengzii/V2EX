package cn.denua.v2ex.http;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Handle the response
 * handle obvious exception
 * convert network thread to UI thread.
 *
 * @author denua
 * @date 2018/10/20
 */
public abstract class ResponseHandler<T> implements Callback<T> {

    protected Handler handler;

    public ResponseHandler(){
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        t.printStackTrace();
        handler.post(()->handle(false, null, call,
                t.getLocalizedMessage() + "\t" + t.getMessage()));
    }

    /**
     * convert to main thread
     *
     * @param call original call
     * @param response  response
     */
    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {

        if (!response.isSuccessful() ){
            handler.post(()->handle(false, null, call,
                    "Request doesn't success, " + response.code() + "-" + response.message()));
        }

        handler.post(()->handle(true, response.body(), call, null));
    }

    /**
     * Handle result and convert network thread on main thread
     *
     * @param success If return http status code 200
     * @param result The genericity result
     * @param call The original call
     * @param msg The error message
     */
    public abstract void handle(boolean success, T result, Call<T> call, String msg);
}
