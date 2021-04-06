package com.p8.common.util;

import android.graphics.Color;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.p8.common.App;
import com.p8.common.R;

import java.util.List;

/**
 * @author : WX.Y
 * date : 2020/11/13 11:31
 * description :
 */
public class PictureSelectorUtils {
    /**
     * 相机拍照
     */
    public static final int CAMERA = 0;
    /**
     * 相册选择
     */
    public static final int GALLERY = 1;
    /**
     * 拍照和相册选择
     */
    public static final int CAMERA_AND_GALLERY = 2;

    /**
     * 拍照
     *
     * @param fragment
     */
    public static void photograph(Fragment fragment) {
        selectPicture(fragment, CAMERA, 1);
    }

    public static void selectSinglePictureFromGallery(Fragment fragment) {
        selectPicture(fragment, GALLERY, 1);
    }

    public static void previewPicture(Fragment fragment, int position, List<LocalMedia> selectList) {
        PictureSelector.create(fragment)
                //.themeStyle(themeId) // xml设置主题
                // 动态自定义相册主题
                .setPictureStyle(getDefaultStyle())
                // 自定义页面启动动画
                //.setPictureWindowAnimationStyle(animationStyle)
                // 预览图片长按是否可以下载
                .isNotPreviewDownload(true)
                // 外部传入图片加载引擎，必传项
                .imageEngine(GlideEngine.createGlideEngine())
                .openExternalPreview(position, selectList);
    }

    public static void selectPicture(Fragment fragment, int mode, int maxSelectNum) {

        PictureSelectionModel model;
        if (mode == CAMERA) {
            model = PictureSelector.create(fragment).openCamera(PictureMimeType.ofImage());
        } else {
            model = PictureSelector.create(fragment).openGallery(PictureMimeType.ofImage());
        }
        model.imageEngine(GlideEngine.createGlideEngine())
                // 图片压缩后输出质量 0~ 100
                .compressQuality(80)
                // 是否显示拍照按钮
                .isCamera(mode == CAMERA_AND_GALLERY)
                //最大选择数量
                .maxSelectNum(maxSelectNum)
                // 选择模式 ：PictureConfig.SINGLE 单张, PictureConfig.MULTIPLE 多张
                .selectionMode(maxSelectNum > 1 ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)
                // 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isSingleDirectReturn(false)
                // 是否可预览图片
                .isPreviewImage(true)
                // 图片列表点击 缩放效果 默认true
                .isZoomAnim(true)
                // 是否裁剪
                .isEnableCrop(false)
                // 是否压缩
                .isCompress(true)
                //同步false或异步true 压缩 默认同步
                .synOrAsy(true)
                // 是否圆形裁剪
                .circleDimmedLayer(false)
                // 裁剪输出质量 默认100
                .cutOutQuality(90)
                // 小于100kb的图片不压缩
                .minimumCompressSize(100)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    public static PictureParameterStyle getDefaultStyle() {
        // 相册主题
        PictureParameterStyle style = new PictureParameterStyle();
        // 是否改变状态栏字体颜色(黑白切换)
        style.isChangeStatusBarFontColor = false;
        // 是否开启右下角已完成(0/9)风格
        style.isOpenCompletedNumStyle = false;
        // 是否开启类似QQ相册带数字选择风格
        style.isOpenCheckNumStyle = false;
        // 相册状态栏背景色
        style.pictureStatusBarColor = Color.parseColor("#393a3e");
        // 相册列表标题栏背景色
        style.pictureTitleBarBackgroundColor = Color.parseColor("#393a3e");
        // 相册列表标题栏右侧上拉箭头
        style.pictureTitleUpResId = R.drawable.picture_icon_arrow_up;
        // 相册列表标题栏右侧下拉箭头
        style.pictureTitleDownResId = R.drawable.picture_icon_arrow_down;
        // 相册文件夹列表选中圆点
        style.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
        // 相册返回箭头
        style.pictureLeftBackIcon = R.drawable.picture_icon_back;
        // 标题栏字体颜色
        style.pictureTitleTextColor = ContextCompat.getColor(App.getInstance(), R.color.picture_color_white);
        // 相册右侧取消按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        style.pictureCancelTextColor = ContextCompat.getColor(App.getInstance(), R.color.picture_color_white);
        // 相册列表勾选图片样式
        style.pictureCheckedStyle = R.drawable.picture_checkbox_selector;
        // 相册列表底部背景色
        style.pictureBottomBgColor = ContextCompat.getColor(App.getInstance(), R.color.picture_color_grey);
        // 已选数量圆点背景样式
        style.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        style.picturePreviewTextColor = ContextCompat.getColor(App.getInstance(), R.color.picture_color_fa632d);
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        style.pictureUnPreviewTextColor = ContextCompat.getColor(App.getInstance(), R.color.picture_color_white);
        // 相册列表已完成色值(已完成 可点击色值)
        style.pictureCompleteTextColor = ContextCompat.getColor(App.getInstance(), R.color.picture_color_fa632d);
        // 相册列表未完成色值(请选择 不可点击色值)
        style.pictureUnCompleteTextColor = ContextCompat.getColor(App.getInstance(), R.color.picture_color_white);
        // 预览界面底部背景色
        style.picturePreviewBottomBgColor = ContextCompat.getColor(App.getInstance(), R.color.picture_color_grey);
        // 外部预览界面删除按钮样式
        style.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete;
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        style.pictureOriginalControlStyle = R.drawable.picture_original_wechat_checkbox;
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        style.pictureOriginalFontColor = ContextCompat.getColor(App.getInstance(), R.color.white);
        // 外部预览界面是否显示删除按钮
        style.pictureExternalPreviewGonePreviewDelete = true;
        // 设置NavBar Color SDK Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP有效
        style.pictureNavBarColor = Color.parseColor("#393a3e");
//        // 自定义相册右侧文本内容设置
//        style.pictureRightDefaultText = "";
//        // 自定义相册未完成文本内容
//        style.pictureUnCompleteText = "";
//        // 自定义相册完成文本内容
//        style.pictureCompleteText = "";
//        // 自定义相册列表不可预览文字
//        style.pictureUnPreviewText = "";
//        // 自定义相册列表预览文字
//        style.picturePreviewText = "";
//
//        // 自定义相册标题字体大小
//        style.pictureTitleTextSize = 18;
//        // 自定义相册右侧文字大小
//        style.pictureRightTextSize = 14;
//        // 自定义相册预览文字大小
//        style.picturePreviewTextSize = 14;
//        // 自定义相册完成文字大小
//        style.pictureCompleteTextSize = 14;
//        // 自定义原图文字大小
//        style.pictureOriginalTextSize = 14;
        return style;
    }
}

