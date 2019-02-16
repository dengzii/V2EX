package cn.denua.v2ex.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * cn.denua.v2ex.widget
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/2/16
 */
public class CustomWebView extends WebView {

    private boolean isFinished = false;
    private LoadFinishListener mLoadFinishListener;

    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isFinished){
            isFinished = getContentHeight() > 0;
            if ((mLoadFinishListener != null) && isFinished) mLoadFinishListener.onFinish();
        }
    }

    public void setLoadFinishListener(LoadFinishListener mLoadFinishListener) {
        this.mLoadFinishListener = mLoadFinishListener;
    }

    public interface LoadFinishListener{
        void onFinish();
    }
}
