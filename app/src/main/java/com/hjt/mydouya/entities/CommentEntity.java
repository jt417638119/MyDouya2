package com.hjt.mydouya.entities;


public class CommentEntity {
    public String created_at;
    public long id;
    public String text;
    public String source;
    public UserEntity user;
    public String mid;
    public String idStr;
    public CommentEntity reply_comment;

}
