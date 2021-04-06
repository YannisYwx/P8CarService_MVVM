package com.p8.common.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.p8.common.R;

/**
 * @author : WX.Y
 * date : 2021/3/15 17:30
 * description :
 */
public class FontStyleTextView extends AppCompatTextView {
    String fontPath;

    public FontStyleTextView(@NonNull Context context) {
        this(context, null);
    }

    public FontStyleTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontStyleTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontStyleTextView, defStyleAttr, 0);
        fontPath = a.getString(R.styleable.FontStyleTextView_fontPath);
        a.recycle();
        if (fontPath == null) {
            fontPath = "fonts/Lobster-1.4.otf";
        }
        init(context);
    }

    /**
     * 定制字体
     */
    private void init(Context context)
    {
        // 获取资源文件
        AssetManager assets = context.getAssets();
        Typeface font =
                Typeface.createFromAsset(assets, fontPath);
        setTypeface(font);
    }

}

