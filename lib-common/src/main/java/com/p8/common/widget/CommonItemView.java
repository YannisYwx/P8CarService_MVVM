package com.p8.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.p8.common.R;

/**
 * @author : WX.Y
 * date : 2020/10/12 11:10
 * description :
 */
public class CommonItemView extends ConstraintLayout {

    private TextView tvLabel, tvRight;
    private ImageView ivIcon, ivArrow;

    private String label, textRight;
    private Drawable icon, arrow;
    private boolean hasBottomLine = true, hasArrow = true;

    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int dividerColor = Color.parseColor("#F303E3");
    private int labelColor, textRightColor;
    private float dividerHeight = getResources().getDimension(R.dimen.dividerHeight);
    private Paint mPaint;

    public CommonItemView(@NonNull Context context) {
        this(context, null);
    }

    public CommonItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        View.inflate(context, R.layout.view_common_item, this);
        float dividerHeight = getResources().getDimension(R.dimen.dividerHeight);
        if (attrs != null) {
            //读取属性
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonItemView);
            label = typedArray.getString(R.styleable.CommonItemView_label);
            hasBottomLine = typedArray.getBoolean(R.styleable.CommonItemView_hasBottomLine, true);
            hasArrow = typedArray.getBoolean(R.styleable.CommonItemView_hasArrow, true);
            dividerColor = typedArray.getColor(R.styleable.CommonItemView_dividerColor, Color.parseColor("#E3E3E3"));
            dividerHeight = typedArray.getDimension(R.styleable.CommonItemView_dividerHeight, getResources().getDimension(R.dimen.dividerHeight));
            labelColor = typedArray.getColor(R.styleable.CommonItemView_labelColor, Color.parseColor("#333333"));
            textRightColor = typedArray.getColor(R.styleable.CommonItemView_textRightColor, Color.parseColor("#333333"));
            icon = typedArray.getDrawable(R.styleable.CommonItemView_icon);
            arrow = typedArray.getDrawable(R.styleable.CommonItemView_arrow);
            paddingLeft = (int) typedArray.getDimension(R.styleable.CommonItemView_dividerPaddingLeft, 0);
            paddingRight = (int) typedArray.getDimension(R.styleable.CommonItemView_dividerPaddingRight, 0);
            typedArray.recycle();
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStrokeWidth(dividerHeight);
    }

    @Override
    protected void onFinishInflate() {
        tvLabel = findViewById(R.id.tv_label);
        tvRight = findViewById(R.id.tv_right);
        ivIcon = findViewById(R.id.iv_icon);
        ivArrow = findViewById(R.id.iv_arrow);
        setDataToUi();
        super.onFinishInflate();
    }

    private void setDataToUi() {
        tvLabel.setText(TextUtils.isEmpty(label) ? "" : label);
        tvLabel.setTextColor(labelColor);
        tvRight.setText(TextUtils.isEmpty(textRight) ? "" : textRight);
        tvRight.setTextColor(textRightColor);
        if (icon != null) {
            ivIcon.setImageDrawable(icon);
            ivIcon.setVisibility(View.VISIBLE);
        } else {
            ivIcon.setVisibility(View.GONE);
        }
        if (arrow != null) {
            ivArrow.setImageDrawable(arrow);
        }
        ivArrow.setVisibility(hasArrow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (hasBottomLine) {
            canvas.drawLine(paddingLeft, getMeasuredHeight(), getMeasuredWidth() - paddingRight, getMeasuredHeight(), mPaint);
        }
    }

    public void setData(int iconRes, int arrowRes, String label, String textRight, boolean hasShowBottomLine, boolean hasArrow) {
        if (iconRes > 0) {
            this.icon = ResourcesCompat.getDrawable(getResources(), iconRes, null);
        }
        if (arrowRes > 0) {
            this.arrow = ResourcesCompat.getDrawable(getResources(), arrowRes, null);
        }
        this.hasBottomLine = hasShowBottomLine;
        this.hasArrow = hasArrow;
        this.label = label;
        this.textRight = textRight;
        setDataToUi();
        invalidate();
    }

    public void setLabel(String label) {
        setData(-1, -1, label, textRight, hasBottomLine, hasArrow);
    }

    public void setTextRight(String text, int... color) {
        setData(-1, -1, label, text, hasBottomLine, hasArrow);
        if (color.length > 0) {
            tvRight.setTextColor(color[0]);
        }
    }

    public void setIcon(@DrawableRes int iconRes) {
        setData(iconRes, -1, label, textRight, hasBottomLine, hasArrow);
    }

    public void setArrow(@DrawableRes int arrowRes) {
        setData(-1, arrowRes, label, textRight, hasBottomLine, hasArrow);

    }

    public void showBottomLine(boolean isShowBottomLine) {
        setData(-1, -1, label, textRight, isShowBottomLine, hasArrow);
    }
}

