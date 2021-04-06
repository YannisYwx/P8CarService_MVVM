package com.p8.common.databinding.ui;

import android.app.Activity;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kingja.loadsir.callback.Callback;
import com.p8.common.mvvm.view.status.EmptyStatus;
import com.p8.common.mvvm.view.status.ErrorStatus;
import com.p8.common.mvvm.view.status.InitStatus;
import com.p8.common.mvvm.view.status.LoadingStatus;

import java.util.List;

/**
 * @author : WX.Y
 * date : 2021/3/29 16:53
 * description :
 */
public interface IBaseView {
    long SHOW_SPACE = 200L;

    /**
     * 初始化view
     */
    void initView();

    /**
     * 初始化通用视图
     */
    void initCommonView();

    /**
     * 在此处最先需要初始化的数据
     */
    default void initDataBefore(){

    }

    /**
     * 初始化数据
     */
    void initData();

    /**
     * 绑定视图布局
     *
     * @return layout res
     */
    @LayoutRes
    int onBindLayout();

    /**
     * 绑定监听事件
     */
    default void bindListener() {
    }

    default Callback getInitStatus() {
        return new InitStatus();
    }

    /**
     * 加载中视图
     *
     * @return view
     */
    default Callback getLoadingStatus() {
        return new LoadingStatus();
    }

    /**
     * 加载出错视图
     *
     * @return view
     */
    default Callback getErrorStatus() {
        return new ErrorStatus();
    }

    /**
     * 控数据视图
     *
     * @return view
     */
    default Callback getEmptyStatus() {
        return new EmptyStatus();
    }



//    /**
//     * 隐藏软键盘
//     */
//    default void hideSoftInput() {
//        Activity activity = null;
//        if (this instanceof Fragment) {
//            activity = ((Fragment) this).getActivity();
//        } else if (this instanceof Activity) {
//            activity = ((Activity) this);
//        }
//        if (activity == null) {
//            return;
//        }
//        View view = activity.getWindow().getDecorView();
//        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }
//
//    /**
//     * 显示软键盘,调用该方法后,会在onPause时自动隐藏软键盘
//     */
//    default void showSoftInput(View view) {
//        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        view.requestFocus();
//        view.postDelayed(() -> imm.showSoftInput(view, InputMethodManager.SHOW_FORCED), SHOW_SPACE);
//    }

//    default Fragment getTopFragment() {
//        FragmentManager fm = null;
//        if (this instanceof Fragment) {
//            fm = ((Fragment) this).getChildFragmentManager();
//        } else if (this instanceof Activity) {
//            fm = ((FragmentActivity) this).getSupportFragmentManager();
//        }
//        if (fm == null) {
//            return null;
//        }
//
//        List<Fragment> fragmentList = fm.getFragments();
//
//        return fragmentList.get(fragmentList.size() - 1);
//    }

    default FragmentTransaction beginTransaction() {
        FragmentManager fm = null;
        if (this instanceof Fragment) {
            fm = ((Fragment) this).getChildFragmentManager();
        } else if (this instanceof Activity) {
            fm = ((FragmentActivity) this).getSupportFragmentManager();
        }
        if (fm == null) {
            return null;
        }
        return fm.beginTransaction();
    }

    /**
     * 提供额外状态布局
     *
     * @return
     */
    default @Nullable
    List<Callback> getExtraStatus() {
        return null;
    }

    enum CommonBarStyle {
        /**
         * 返回图标(默认)
         */
        LEFT_BACK,
        /**
         * 返回图标+文字
         */
        LEFT_BACK_TEXT,
        /**
         * 附加图标
         */
        LEFT_ICON,
        /**
         * 标题(默认)
         */
        CENTER_TITLE,
        /**
         * 自定义布局
         */
        CENTER_CUSTOM,
        /**
         * 文字
         */
        RIGHT_TEXT,
        /**
         * 图标(默认)
         */
        RIGHT_ICON,
        /**
         * 自定义布局
         */
        RIGHT_CUSTOM,
    }
}


