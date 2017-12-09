package com.hjt.mydouya.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hjt.mydouya.R;
import com.hjt.mydouya.entities.CommentEntity;
import com.hjt.mydouya.entities.PicUrlsEntity;
import com.hjt.mydouya.utils.CircleTransform;
import com.hjt.mydouya.utils.RichTextUtils;
import com.hjt.mydouya.utils.TimeFormatUtils;

import java.util.List;

/**
 * Created by HJT on 2017/11/16.
 */
public class MyAllCommentListAdapter extends RecyclerView.Adapter {
    private List<CommentEntity> mMyAllCommentsList;
    private Context mContext;

    public MyAllCommentListAdapter(Context context, List<CommentEntity> list) {
        this.mContext = context;
        this.mMyAllCommentsList = list;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_comment, parent,
                false);

        return new MyAllCommentsPageHodler(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyAllCommentsPageHodler) {
            // getItemCount已经可以排除对象为空的情况，如果是空的就不会到这来
            MyAllCommentsPageHodler myAllCommentsPageHolder = (MyAllCommentsPageHodler) holder;
            CommentEntity commentEntity = mMyAllCommentsList.get(position);

            // 设置头像
            Glide.with(mContext).load(commentEntity.user.profile_image_url).transform(new
                    CircleTransform(mContext)).into(myAllCommentsPageHolder.ivHeader);

            // 设置文本信息
            myAllCommentsPageHolder.tvUserName.setText(commentEntity.user.screen_name);
            myAllCommentsPageHolder.tvTime.setText(TimeFormatUtils.parseToYYMMDD(commentEntity
                    .created_at));
            myAllCommentsPageHolder.tvSource.setText(Html.fromHtml(commentEntity.source).toString
                    ());
            myAllCommentsPageHolder.tvContent.setText(RichTextUtils.getRichText(mContext,
                    commentEntity.text));

            // 设置被评论的内容图片
            List<PicUrlsEntity> pics = commentEntity.status.pic_urls;
            if (pics != null && pics.size() > 0) {
                PicUrlsEntity pic = pics.get(0);
                myAllCommentsPageHolder.ivContent.setVisibility(View.VISIBLE);
                pic.original_pic = pic.thumbnail_pic.replace("thumbnail", "large");
                pic.bmiddle_pic = pic.thumbnail_pic.replace("thumbnail", "bmiddle");
                // 设置图片
                Glide.with(mContext).load(pic.bmiddle_pic).into(myAllCommentsPageHolder.ivContent);

            } else {// 如果没有图片是视频，则将图片换成被评论人的头像
//                myAllCommentsPageHolder.ivContent.setVisibility(View.GONE);
                // 换成头像
                Glide.with(mContext).load(commentEntity.status.user.profile_image_url).into(myAllCommentsPageHolder.ivContent);

            }
            // 设置被评论的内容和作者名字
            myAllCommentsPageHolder.tvCommentUsername.setText("@" + commentEntity.status.user
                    .screen_name);
            myAllCommentsPageHolder.tvCommentContent.setText(commentEntity.status.text);

        }
    }

    @Override
    public int getItemCount() {
        return mMyAllCommentsList.size();
    }

    public class MyAllCommentsPageHodler extends RecyclerView.ViewHolder {
        private ImageView ivHeader;
        private TextView tvUserName;
        private TextView tvTime;
        private TextView tvSource;
        private TextView tvContent;
        private ImageView ivContent;
        private TextView tvCommentUsername;
        private TextView tvCommentContent;


        public MyAllCommentsPageHodler(View itemView) {
            super(itemView);
            ivHeader = (ImageView) itemView.findViewById(R.id.ivHeader);
            ivContent = (ImageView) itemView.findViewById(R.id.ivContent);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvSource = (TextView) itemView.findViewById(R.id.tvSource);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvCommentUsername = (TextView) itemView.findViewById(R.id.tvCommentUsername);
            tvCommentContent = (TextView) itemView.findViewById(R.id.tvCommentContent);
        }
    }
}