package com.android.base.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * author cipherGG
 * Created by Administrator on 2016/3/22.
 * describe textView显示html
 */
public class HtmlText implements Html.ImageGetter {

    private Context mContext;
    private TextView mTextView;

    public static void setText(){

    }

    // 需要将加载html的textView和context传进来
    public HtmlText(Context context, TextView textView) {
        this.mTextView = textView;
        this.mContext = context;
    }

    @Override
    public Drawable getDrawable(String source) {
        final URLDrawable drawable = new URLDrawable();
        Glide.with(mContext)
                .load(source)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        // 接口中嘛  就这么传吧
                        drawable.bitmap = resource;
                        // 设置图片大小，不然会和文字重叠，注意是resource的大小
                        drawable.setBounds(0, 0, resource.getWidth(), resource.getHeight());
                        // 收到图片后记得刷新textView
                        mTextView.invalidate();
                        // 这行不加会造成图片尺寸不对
                        mTextView.setText(mTextView.getText());
                    }
                });

        return drawable;
    }

    private class URLDrawable extends BitmapDrawable {
        protected Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
            }
        }
    }
}




