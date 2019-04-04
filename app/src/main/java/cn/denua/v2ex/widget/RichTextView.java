package cn.denua.v2ex.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

/**
 * cn.denua.v2ex.widget
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/4/4
 */
public class RichTextView extends android.support.v7.widget.AppCompatTextView {

    public RichTextView(Context context) {
        super(context);
    }

    public RichTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRichText(SpannableStringBuilder stringBuilder, String html){

        Spanned spanned ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY,
                    new XImageGetter(), null);
        }else{
            spanned = Html.fromHtml(html, new XImageGetter(), null);
        }
        stringBuilder.append(spanned);
        super.setText(stringBuilder);
    }

    public void setRichText(String html){

        Spanned spanned ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY,
                    new XImageGetter(), null);
        }else{
            spanned = Html.fromHtml(html, new XImageGetter(), null);
        }
        super.setText(spanned);
    }
    private class XImageGetter implements Html.ImageGetter{
        @Override
        public Drawable getDrawable(String source) {
            WebDrawable webDrawable = new WebDrawable();
            Glide.with(RichTextView.this).load(source).into(new XSimpleTarget(webDrawable));
            return webDrawable;
        }
    }

    private class WebDrawable extends BitmapDrawable{

        Drawable drawable;

         WebDrawable(){ }

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null){
                super.draw(canvas);
            }
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }
    }
    private class XSimpleTarget extends SimpleTarget<Drawable> {

        WebDrawable webDrawable;
        private XSimpleTarget(WebDrawable urlDrawable){
            this.webDrawable = urlDrawable;
        }
        @Override
        public void onResourceReady(@NonNull Drawable resource,
                                    @Nullable Transition<? super Drawable> transition) {
            int w = 600;
            int hh=resource.getIntrinsicHeight();
            int ww=resource.getIntrinsicWidth() ;
            int high = hh * (w - 50)/ww;
            Rect rect = new Rect(20, 20,w-30,high);
            resource.setBounds(rect);
            webDrawable.setBounds(rect);
            webDrawable.setDrawable(resource);

            setText(getText());
            invalidate();
        }
    }
}
