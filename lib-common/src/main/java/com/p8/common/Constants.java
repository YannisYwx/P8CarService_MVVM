package com.p8.common;

import android.os.Environment;

import androidx.annotation.IntDef;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author : WX.Y
 * date : 2020/9/8 17:06
 * description :App常量
 */
public interface Constants {

    String KEY_TOKEN = "_KEY_TOKEN";
    String KEY_USER_ID = "_KEY_KEY_USER_ID";
    /**
     * 用户类型
     */
    @IntDef({UserType.LARGE, UserType.MEDIUM, UserType.SMALL, UserType.LAND, UserType.PLATFORM,
            UserType.PLACE, UserType.BUILD, UserType.ONESELF, UserType.OTHER})
    @Retention(RetentionPolicy.SOURCE)
    @interface UserType {
        /**
         * 大主
         */
        int LARGE = 0;
        /**
         * 中主
         */
        int MEDIUM = 5;
        /**
         * 小主
         */
        int SMALL = 3;
        /**
         * 地主
         */
        int LAND = 1;
        /**
         * 台主
         */
        int PLATFORM = 4;
        /**
         * 场主
         */
        int PLACE = 2;
        /**
         * 自主
         */
        int ONESELF = 6;
        /**
         * 施主
         */
        int BUILD = 7;
        /**
         * 地主
         */
        int OTHER = 8;

    }

    /**
     * 没有图标标志位
     */
    int FLAG_NO_ICON = -1;

    /**
     * 用户类型
     */
    String USER_TYPE = "_USER_TYPE";

    interface Key {
        /**
         * 用户登录信息key
         */
        String LOGIN_INFO = "_KEY_USER_INFO";

        /**
         * 用户登录信息key
         */
        String LANDLORD = "_KEY_LANDLORD";

        /**
         * 订单详情
         */
        String ORDER_INFO = "_KEY_ORDER_INFO";

        /**
         * 二维码扫描结果
         */
        String SCAN_QE_CODE_RESULT = "_SCAN_QE_CODE_RESULT";
    }

    String SP_NAME = "P8_APP";

    String LOCATION_ADDRESS = "_location_address";

    /**
     * apk文件下载保存目录
     */
    String DOWNLOAD_FOLDER = "/P8_download/";

    String DOWNLOAD_PATH = App.getInstance().getExternalFilesDir(null) + "/p8_inspection/download/pdf/";

    /**
     * 默认分页大小
     */
    int PAGE_SIZE = 10;

    /**
     * 移动端平台
     */
    int PLATFORM_CODE = 1;

    String EXTRA = "_extra";

    String TRANSITION_ANIMATION_NEWS_PHOTOS = "transition_animation_news_photos";
    /**
     * 文章id
     */
    String ARTICLE_ID = "ARTICLE_ID";
    /**
     * 文章图片res
     */
    String ARTICLE_IMG_RES = "ARTICLE_IMG_RES";

    /**
     * android/data/com.pa.inspection/cache/data
     */
    String PATH_DATA = App.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";

    String PATH_CACHE = PATH_DATA + "/NetCache";

    String PATH_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "codeest" + File.separator + "GeekNews";


    interface SpConstants {
        String IS_FIRST_IN = "_isFirstIn";
    }

    interface DeviceCmd {
        /**
         * 上升和下降
         */
        String CMD_UP = "ma11e";
        String CMD_DOWN = "ma10e";

        /**
         * 泊车状态
         */
        String CMD_STALL = "mb11e";
        String CMD_NO_STALL = "mb10e";

        /**
         * 断电供电状态
         */
        String CMD_OUTAGE = "md11e";
        String CMD_POWER = "md10e";

        /**
         * 液位状态
         */
        String CMD_LIQUID_ALARM = "mf11e";
        String CMD_LIQUID_NORMAL = "mf10e";

        /**
         * 电池电压状态
         */
        String CMD_VOLTAGE_ALARM = "mg11e";
        String CMD_VOLTAGE_NORMAL = "mg10e";

        /**
         * 指示灯开关状态
         */
        String CMD_CLOSE = "mh10e";
        String CMD_OPEN = "mh11e";

        /**
         * 查询设备健康
         */
        String CMD_DEVICE_INFO_1 = "mc10e";
        String CMD_DEVICE_INFO_2 = "mc11e";
    }

    /**
     * 设备状态
     */
    interface DeviceStatus {
        /**
         * 为选择
         */
        String NOT_CHOICE = "-1";

        /**
         * 无车
         */
        String NO_CAR = "0";
        /**
         * 有车
         */
        String HAVE_CAR = "1";
        /**
         * 待激活
         */
        String TO_ACTIVATE = "2";
        /**
         * 初始化中
         */
        String INITIALIZING = "3";
        /**
         * 异常
         */
        String ERROR = "4";
        /**
         * 全部
         */
        String ALL = "";

    }

    interface P8Code {
        int SUCCESS = 1;
        int FAILED = 0;
        int TOKEN_ERROR = 408;
    }
}
