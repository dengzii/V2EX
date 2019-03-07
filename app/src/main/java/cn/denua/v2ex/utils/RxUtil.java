package cn.denua.v2ex.utils;

import com.google.gson.JsonObject;

import cn.denua.v2ex.http.RetrofitManager;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxUtil {


   public static <T> ObservableTransformer<T, T> io2main(){

       return upstream ->
               RetrofitManager.DEBUG
               ? upstream
               : upstream.subscribeOn(Schedulers.io())
                       .observeOn(AndroidSchedulers.mainThread());
   }

    public static <T> ObservableTransformer<T, T> io2io(){

        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public static <T> ObservableTransformer<T, T> io2computation(){

        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation());
    }

    public static <T> ObservableTransformer<JsonObject, T> JsonApiPreProcess(){
       return upstream -> upstream.flatMap(jsonObject ->  null
           // todo
       );
   }
}
