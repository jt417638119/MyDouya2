package com.hjt.mydouya.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hjt.mydouya.R;
import com.hjt.mydouya.entities.PicUrlsEntity;
import com.hjt.mydouya.entities.StatusEntity;
import com.hjt.mydouya.utils.RichTextUtils;
import com.hjt.mydouya.utils.TimeFormatUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by HJT on 2017/11/16.
 */

public class HomepageListAdapter extends RecyclerView.Adapter {
    private List<StatusEntity> mDataSet;
    private OnItemClickListener mOnItemClickListener;
    private OnCommentItemClickListener mOnCommentItemClickListener;
    private Context mContext;

    public HomepageListAdapter(List<StatusEntity> mDataSet, Context context) {
        this.mDataSet = mDataSet;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weibo_content,
                parent, false);
        return new HomepageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HomepageViewHolder) {
            HomepageViewHolder homepageViewHolder = (HomepageViewHolder) holder;
            StatusEntity entity = mDataSet.get(position);

            /**
             * 加载头像, 个人用户信息
             */
            homepageViewHolder.tvUserName.setText(entity.user.screen_name);
            homepageViewHolder.tvTime.setText(TimeFormatUtils.parseToYYMMDD(entity.created_at));
            homepageViewHolder.tvSource.setText(Html.fromHtml(entity.source).toString());
            homepageViewHolder.tvContent.setText(RichTextUtils.getRichText(mContext, entity.text)
            ); // 内容
            // 要为text中标志的字段添加点击事件
            homepageViewHolder.tvContent.setMovementMethod(LinkMovementMethod.getInstance());
            Glide.with(mContext)
                    .load(entity.user.profile_image_url)
                    .into(homepageViewHolder.ivHeader); // 加载头像
            List<PicUrlsEntity> pics = entity.pic_urls; // 提取内容图片
            if (pics != null && pics.size() > 0) {
                // 因为是ViewHodler，可能会重复利用，则需要重新设置
                homepageViewHolder.ivContent.setVisibility(View.VISIBLE);
                PicUrlsEntity pic = pics.get(0);
                pic.original_pic = pic.thumbnail_pic.replace("thumbnail", "large");
                pic.bmiddle_pic = pic.thumbnail_pic.replace("thumbnail", "bmiddle");
                Glide.with(mContext)
                        .load(pic.bmiddle_pic)
                        .into(homepageViewHolder.ivContent);
            } else {// 将图片框设置成不可见
                homepageViewHolder.ivContent.setVisibility(View.GONE);
            }
            /**
             * 转发
             */
            // 转发内容
            StatusEntity reStatus = entity.retweeted_status;// 转发内容
            if (reStatus != null) { // 转发内容非空
                // 防止滚动的时候llRe不见了
                homepageViewHolder.llRe.setVisibility(View.VISIBLE);
                // 前面要加上被转发内容的作者，这样正则才能匹配并且换颜色
                homepageViewHolder.tvReContent.setText(RichTextUtils.getRichText(mContext,
                        "@" + reStatus.user.screen_name +""+reStatus.text)); // 内容
                // 要为text中标志的字段添加点击事件
                homepageViewHolder.tvReContent.setMovementMethod(LinkMovementMethod.getInstance());
                // 提取内容图片
                List<PicUrlsEntity> rePics = reStatus.pic_urls; // 提取内容图片
                if (rePics != null && rePics.size() > 0) {
                    // 因为是ViewHodler，可能会重复利用，则需要重新设置
                    homepageViewHolder.ivReContent.setVisibility(View.VISIBLE);
                    PicUrlsEntity pic = rePics.get(0);
                    pic.original_pic = pic.thumbnail_pic.replace("thumbnail", "large");
                    pic.bmiddle_pic = pic.thumbnail_pic.replace("thumbnail", "bmiddle");
                    Glide.with(mContext)
                            .load(pic.bmiddle_pic)
                            .into(homepageViewHolder.ivReContent);
                } else {// 将图片框设置成不可见
                    homepageViewHolder.ivReContent.setVisibility(View.GONE);
                }
            } else { // 没有转发
                homepageViewHolder.llRe.setVisibility(View.GONE);
            }
            /**
             * 转发按钮，点赞，评论
             */
            homepageViewHolder.tvRetweet.setText(String.valueOf(entity.reposts_count));
            homepageViewHolder.tvComment.setText(String.valueOf(entity.comments_count));
            homepageViewHolder.tvLike.setText(String.valueOf(entity.attitudes_count));

        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    // 调用匿名内部类
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
    // 调用匿名内部类
    public void setOnCommentItemClickListener(OnCommentItemClickListener listener) {
        mOnCommentItemClickListener = listener;
    }

    class HomepageViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivHeader;
        public TextView tvUserName;
        public TextView tvTime;
        public TextView tvSource;
        public TextView tvContent;
        public ImageView ivContent;
        public LinearLayout llRe;
        public TextView tvReContent;
        public ImageView ivReContent;
        public TextView tvRetweet;
        public TextView tvComment;
        public TextView tvLike;

        public HomepageViewHolder(View itemView) {
            super(itemView);
            // 初始化每个item组件
            initialize(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        // 调用自定义的监听方法，不然就固定死了
                        mOnItemClickListener.onItemClick(v, getLayoutPosition());
                    }
                }
            });
            tvComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnCommentItemClickListener != null ) {
                        // 调用自定义的监听方法，不然就固定死了
                        mOnCommentItemClickListener.onCommentItemClick(v,getLayoutPosition());
                    }
                }
            });
        }

        public void initialize(View itemView) {
            ivHeader = (ImageView) itemView.findViewById(R.id.ivHeader);
            ivContent = (ImageView) itemView.findViewById(R.id.ivContent);
            ivReContent = (ImageView) itemView.findViewById(R.id.ivReContent);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvSource = (TextView) itemView.findViewById(R.id.tvSource);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            llRe = (LinearLayout) itemView.findViewById(R.id.llRe);
            tvReContent = (TextView) itemView.findViewById(R.id.tvReContent);
            tvRetweet = (TextView) itemView.findViewById(R.id.tvRetweet);
            tvComment = (TextView) itemView.findViewById(R.id.tvComment);
            tvLike = (TextView) itemView.findViewById(R.id.tvLike);
        }
    }

    // 匿名内部类，供对象去实现，最后给父类调用
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    // 匿名内部类，供对象去实现，最后给父类调用
    public interface OnCommentItemClickListener {
        void onCommentItemClick(View v, int position);
    }


}