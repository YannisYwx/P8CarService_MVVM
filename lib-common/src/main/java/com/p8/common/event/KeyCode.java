package com.p8.common.event;

/**
 * @author : WX.Y
 * date : 2021/3/11 17:56
 * description :
 */
public interface KeyCode {
    interface Main {
    }

    interface Home {
        String KEYWORD = "keyword";
        String CATEGORY = "category";
        String TAG = "tag";
        String COLUMN = "column";
        String TITLE = "title";
        String ALBUMID = "albumid";
        String HOTWORD = "hotword";
        String RADIO_ID = "radioId";
        String ANNOUNCER_ID = "announcerId";
        String CATEGORY_ID = "categoryId";
        String ANNOUNCER_NAME= "announcerName";
        String TAB= "tab";
    }

    interface Listen {
        String TAB_INDEX="tab_index";
        String ALBUMID = "albumid";
    }
    interface Discover {
        String PATH="path";
    }

    interface Video {
        String CHANNEL="channel";
    }
    interface Task {
        String TYPE="type";
    }
    interface Patrol {
        String FACE_PATH="face_path";
    }
}
