package com.hjt.mydouya.entities;

import java.util.List;

/**
 * Created by HJT on 2017/11/15.
 */

public class StatusEntity {

    public String created_at;
    public long id;
    public String mid;
    public String idstr;
    public String text;
    public int source_allowclick;
    public int source_type;
    public String source;
    public boolean favorited;
    public boolean truncated;
    public String in_reply_to_status_id;
    public String in_reply_to_user_id;
    public String in_reply_to_screen_name;
    public String thumbnail_pic;
    public String bmiddle_pic;
    public String original_pic;
    public Object geo;
    public List<PicUrlsEntity> pic_urls;
    public UserEntity user;
    public StatusEntity retweeted_status;
    public  int  reposts_count;
    public  int  comments_count;
    public  int  attitudes_count;

    public int deleted;
}
