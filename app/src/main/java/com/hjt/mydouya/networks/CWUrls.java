package com.hjt.mydouya.networks;

/**
 * Created by HJT on 2017/11/16.
 */

public class CWUrls {
    public static final String PREFIX = "https://api.weibo.com/2/"; // 方便版本更新
    public static final String HOME_TIME_LINE = PREFIX + "statuses/home_timeline.json";
    public static final String USER_TIME_LINE = PREFIX + "statuses/user_timeline.json";
    public static final String STATUS_REPOST = PREFIX + "statuses/repost.json";
    public static final String COMMENT_CREATE = PREFIX + "comments/create.json";
    public static final String COMMENT_SHOW = PREFIX + "comments/show.json";
    public static final String GET_UID = PREFIX + "account/get_uid.json";
    public static final String GET_USER = PREFIX + "users/show.json";

}
