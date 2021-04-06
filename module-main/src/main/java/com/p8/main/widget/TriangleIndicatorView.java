package com.p8.main.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import com.p8.main.R;

/**
 * @author : WX.Y
 * date : 2020/10/22 15:30
 * description : 可以移动的三角指示器
 */
public class TriangleIndicatorView extends View {

    public static final float TRIANGLE_WIDTH_SCALE_OF_W = 1 / 24.0f;

    public static final float TRIANGLE_HEIGHT_SCALE_OF_WIDTH = 0.6f;
    /**
     * 三角形底边宽
     */
    private int mTriangleWidth;
    /**
     * 三角形高度
     */
    private int mTriangleHeight;
    /**
     * 三角形起始点
     */
    private int mTriangleInitPos;
    /**
     * 三角形移动偏移
     */
    private float moveWidth;

    private Paint mPaint;
    private Path mPath;

    private int triangleColor = Color.parseColor("#FFFFFF");
    private boolean openAnimator = true;
    private int mTableSize = 2;

    private int mWidth;
    private int mTableWidth;

    private int currentPosition = 0;

    float[] mTriangleStarts = new float[2];
    float[] mTriangleTransition = new float[2];
    float[] mTriangleEnds = new float[2];

    public TriangleIndicatorView(Context context) {
        this(context, null);
    }

    public TriangleIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TriangleIndicatorView);
            triangleColor = a.getColor(R.styleable.TriangleIndicatorView_triangleColor, Color.parseColor("#FFFFFF"));
            mTableSize = a.getInteger(R.styleable.TriangleIndicatorView_tableSize, 2);
            openAnimator = a.getBoolean(R.styleable.TriangleIndicatorView_openAnimator, true);
            a.recycle();
        }
        init();
    }

    private void init() {
        initPaint();
        initTriangle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(triangleColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mTableWidth = mWidth / mTableSize;
        initTriangle();
        setMeasuredDimension(mWidth, mTriangleHeight);
    }

    /**
     * 初始化三角形
     */
    private void initTriangle() {
        mTriangleWidth = (int) (mWidth * TRIANGLE_WIDTH_SCALE_OF_W);
        mTriangleHeight = (int) (mTriangleWidth * TRIANGLE_HEIGHT_SCALE_OF_WIDTH);
        mTriangleStarts[0] = mWidth / (mTableSize << 1) - (mTriangleWidth >> 1) + currentPosition * mTableWidth + moveWidth;
        mTriangleStarts[1] = 0;
        mTriangleTransition[0] = mTriangleStarts[0] + (mTriangleWidth >> 1);
        mTriangleTransition[1] = mTriangleHeight;
        mTriangleEnds[0] = mTriangleStarts[0] + mTriangleWidth;
        mTriangleEnds[1] = 0;
    }

    public int getMoveWidth(int startPosition, int endPosition) {
        int moveWidth;
        moveWidth = (endPosition - startPosition) * mTableWidth;
        return moveWidth;
    }

    private boolean isAnimatorRunning = false;

    public void pointTo(int index) {
        if (isAnimatorRunning) {
            return;
        }
        moveWidth = getMoveWidth(currentPosition, index);
        if (moveWidth != 0) {
            if (!openAnimator) {
                setMoveWidth(moveWidth);
                currentPosition = index;
                return;
            }
            isAnimatorRunning = true;
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "moveWidth", moveWidth).setDuration(500L);
            objectAnimator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    currentPosition = index;
                    isAnimatorRunning = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimator.start();
        }
    }

    @Keep
    public void setMoveWidth(float moveWidth) {
        this.moveWidth = moveWidth;
        initTriangle();
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mPath.moveTo(mTriangleStarts[0], mTriangleStarts[1]);
        mPath.lineTo(mTriangleTransition[0], mTriangleTransition[1]);
        mPath.lineTo(mTriangleEnds[0], mTriangleEnds[1]);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        mPath.reset();
    }

}

