/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.utils;

import android.os.Looper;

import com.blankj.utilcode.util.ToastUtils;

/*
 * 处理未捕获的异常
 *
 * @author denua
 * @date 2018/11/04 00
 */
public class UnexpectedExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static UnexpectedExceptionHandler mHandler;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    synchronized
    public static UnexpectedExceptionHandler getInstance(){
        if (mHandler==null){
            synchronized (Thread.UncaughtExceptionHandler.class){
                if (mHandler==null){
                    mHandler = new UnexpectedExceptionHandler();
                }
            }
        }
        return mHandler;
    }

    private UnexpectedExceptionHandler(){}


    public void init(){
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private boolean handleException(Throwable throwable){
        if (null==throwable){
            return false;
        }
        new Thread(()->{
            Looper.prepare();
            ToastUtils.showLong(throwable.toString());
            Looper.loop();
        });
        return false;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleException(e)){
            if (mDefaultHandler != null){
                System.err.println(e.getLocalizedMessage());
                ToastUtils.showLong(e.toString());
                // handle exception by default handler
                mDefaultHandler.uncaughtException(t, e);
            }
        }else {
            System.err.println("END");
        }
    }
}
