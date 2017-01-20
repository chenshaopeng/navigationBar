package com.navigationbar.navigationbarview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.navigationbar.navigationbarview.R;

import java.lang.reflect.TypeVariable;

/**
 * Created by 陈韶鹏 on 2017/1/11.
 */

public class NavigationBar extends View {

    private static final int FLAG_NORMAL = 1;
    private static final int FLAG_TOUCH = 2;
    private int screenHeight;
    private int measureHeight;
    private float startX;

    private float startY;
    private float itemHeight;
    private int mTextSize;
    private Paint mTextPaint;
    private int measureWidth;
    private int textColor;

    private int bgColorNormal;
    private int bgColorTouch;
    float viewY;

    private int bgStatus = FLAG_NORMAL;

    public static final String[] characters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L" , "M", "N"
            , "O", "P", "Q", "R", "S","T", "U", "V", "W", "X", "Y", "Z", "#"};
    private OnGetCharacterCallBack onGetCharacterCallBack;
    private float textMargin;
    private float viewPaddingLeft;
    private float viewPaddingTop;
    private float viewPaddingRight;
    private float viewPaddingBottom;
    public NavigationBar(Context context) {
        this(context, null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NavigationBar, defStyleAttr, 0);
        textColor = typedArray.getColor(R.styleable.NavigationBar_textColor, Color.WHITE);
        bgColorNormal = typedArray.getColor(R.styleable.NavigationBar_backgroundColorNormal, Color.GRAY);
        bgColorTouch = typedArray.getColor(R.styleable.NavigationBar_backgroundColorTouch, Color.GRAY);

        float padding = dp2px(context, typedArray.getDimension(R.styleable.NavigationBar_android_padding, 0));
        float paddingLeft = dp2px(context, typedArray.getDimension(R.styleable.NavigationBar_android_paddingLeft, 0));
        float paddingTop = dp2px(context, typedArray.getDimension(R.styleable.NavigationBar_android_paddingTop, 0));
        float paddingRight = dp2px(context, typedArray.getDimension(R.styleable.NavigationBar_android_paddingRight, 0));
        float paddingBottom = dp2px(context, typedArray.getDimension(R.styleable.NavigationBar_android_paddingBottom, 0));

        if (padding > 0) {
            viewPaddingLeft = padding;
            viewPaddingTop = padding;
            viewPaddingRight = padding;
            viewPaddingBottom = padding;
        } else {
            viewPaddingLeft = paddingLeft;
            viewPaddingTop = paddingTop;
            viewPaddingRight = paddingRight;
            viewPaddingBottom = paddingBottom;
        }

        typedArray.recycle();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        mTextPaint = new Paint();
        Log.d("chen","screenHeight: " + screenHeight);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        startX = measureWidth / 2;
        itemHeight = (measureHeight-viewPaddingTop - viewPaddingBottom) / characters.length ;

        mTextSize = (int) (itemHeight *2 / 3 + 0.5f);
        textMargin = (itemHeight - mTextSize) / 2 + viewPaddingTop;
        viewY = screenHeight - measureHeight;

        Log.d("chen","measureHeight: " + measureHeight);
        Log.d("chen", "itemHeight: " + itemHeight);
        Log.d("chen", "startY: " + startY);
        setMeasuredDimension(measureWidth, measureHeight);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        startY = Math.abs(fontMetrics.ascent + fontMetrics.leading) + textMargin;
        if (bgStatus == FLAG_NORMAL) {
            canvas.drawColor(bgColorNormal);
        } else {
            canvas.drawColor(bgColorTouch);
        }
        for (int i = 0; i< characters.length; i++) {
            float charWidth = mTextPaint.measureText(characters[i]);
            float offsetX = charWidth / 2;
            canvas.drawText(characters[i], startX - offsetX, startY + itemHeight * i, mTextPaint);
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                bgStatus = FLAG_TOUCH;
                float downY = event.getY();
                if (downY > 0 && downY < measureHeight) {
                    getCharacter(downY);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY();
                if (moveY > 0 && moveY < measureHeight) {
                    getCharacter(moveY);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                bgStatus = FLAG_NORMAL;
                invalidate();
                break;
        }
        return true;
    }

    private String getCharacter (float touchY) {
        int position = (int) (touchY / itemHeight);
        if (position < 0) {
            position = 0;
        }
        if (position >= characters.length) {
            position = characters.length-1;
        }
        if (onGetCharacterCallBack != null) {
            onGetCharacterCallBack.onGetCharacter(characters[position], position);
        }
        return characters[position];
    }

    public void setOnGetCharacterCallBack(OnGetCharacterCallBack onGetCharacterCallBack) {
        this.onGetCharacterCallBack = onGetCharacterCallBack;

    }

    public interface OnGetCharacterCallBack {
        void onGetCharacter(String character, int position);
    }

    public float dp2px (Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
