package com.ray.simplecircleimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-07-25 16:03
 *  description : 圆形图片 通过BitmapShader实现
 */
public class CircleImageView extends android.support.v7.widget.AppCompatImageView {

    private int mWidth;
    private float mRadius;
    private Paint mPaint;
    private Matrix mScaleMatrix;
    private Drawable mDrawable;

    public CircleImageView(Context context) {
        this(context, null, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mScaleMatrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = Math.min(MeasureSpec.getSize(widthMeasureSpec),  MeasureSpec.getSize(heightMeasureSpec));
        mRadius = mWidth*1.f/2;
        setMeasuredDimension(mWidth, mWidth);
    }

    private void setupShader(){
        Drawable drawable = getDrawable();
        if (drawable != null && mDrawable != drawable) {
            Bitmap bitmap = drawable2Bitmap(drawable);
            BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            int bitMapSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
            float scale = mWidth*1.f/bitMapSize;
            mScaleMatrix.setScale(scale, scale);
            bitmapShader.setLocalMatrix(mScaleMatrix);
            mPaint.setShader(bitmapShader);
            mDrawable = drawable;
        }

    }

    private Bitmap drawable2Bitmap(Drawable drawable){
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        int intrinsicWidth = drawable.getIntrinsicWidth() <= 0 ? mWidth : drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight() <= 0 ? mWidth : drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,intrinsicWidth,intrinsicHeight);
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setupShader();
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
    }
}
