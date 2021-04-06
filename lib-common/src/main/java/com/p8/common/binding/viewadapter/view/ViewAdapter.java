package com.p8.common.binding.viewadapter.view;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.jakewharton.rxbinding3.view.RxView;
import com.p8.common.binding.command.BindingCommand;
import com.p8.common.observer.ViewClickObserver;

import java.util.concurrent.TimeUnit;

/**
 * @author : WX.Y
 * date : 2021/3/24 14:56
 * description :
 */
public class ViewAdapter {
    /**
     * 防重复点击间隔(秒)
     */
    public static final int CLICK_INTERVAL = 1;

    /**
     * requireAll 是意思是是否需要绑定全部参数, false为否
     * View的onClick事件绑定
     * onClickCommand 绑定的命令,
     * isThrottleFirst 是否开启防止过快点击
     */
    @BindingAdapter(value = {"onClickCommand", "isThrottleFirst"}, requireAll = false)
    public static void onClickCommand(View view, final BindingCommand<?> clickCommand, final boolean isThrottleFirst) {
        if (isThrottleFirst) {
            RxView.clicks(view).subscribe(new ViewClickObserver() {
                @Override
                public void onClick() {
                    if(clickCommand!=null) {
                        clickCommand.execute();
                    }
                }
            });
        } else {
            RxView.clicks(view).
                    throttleFirst(CLICK_INTERVAL, TimeUnit.SECONDS).subscribe(new ViewClickObserver() {
                @Override
                public void onClick() {
                    if(clickCommand!=null) {
                        clickCommand.execute();
                    }
                }
            }); //1秒钟内只允许点击1次
        }
    }

    /**
     * view是否需要获取焦点
     */
    @BindingAdapter({"requestFocus"})
    public static void requestFocusCommand(View view, final Boolean needRequestFocus) {
        if (needRequestFocus) {
            view.setFocusableInTouchMode(true);
            view.requestFocus();
        } else {
            view.clearFocus();
        }
    }


    /**
     * view的焦点发生变化的事件绑定
     */
    @BindingAdapter({"onFocusChangeCommand"})
    public static void onFocusChangeCommand(View view, final BindingCommand<Boolean> onFocusChangeCommand) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (onFocusChangeCommand != null) {
                    onFocusChangeCommand.execute(hasFocus);
                }
            }
        });
    }

    /**
     * view的显示隐藏
     */
    @BindingAdapter(value = {"isVisible"}, requireAll = false)
    public static void isVisible(View view, final Boolean visibility) {
        if (visibility) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter(value = {"imgRes"}, requireAll = false)
    public static void setRes(ImageView imageView,int res) {
        imageView.setImageResource(res);
    }
}

