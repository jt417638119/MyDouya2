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
import com.hjt.mydouya.entities.CommentEntity;
import com.hjt.mydouya.entities.PicUrlsEntity;
import com.hjt.mydouya.entities.StatusEntity;
import com.hjt.mydouya.utils.RichTextUtils;
import com.hjt.mydouya.utils.TimeFormatUtils;

import java.util.List;

/**
 * Created by HJT on 2017/11/16.
 */
public class ArticleCommentListAdapter extends RecyclerView.Adapter {
    private List<CommentEntity> mCommentEntityList;
    private Context mContext;

    public ArticleCommentListAdapter(Context context, List<CommentEntity> mCommentEntityList) {
        this.mCommentEntityList = mCommentEntityList;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weibo_comment,
                parent, false);
        return new ArticleCommentPageHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleCommentPageHolder) {
            ArticleCommentPageHolder articleCommentHodler = (ArticleCommentPageHolder) holder;
            CommentEntity commentEntity = mCommentEntityList.get(position);
            articleCommentHodler.tvUserName.setText(commentEntity.user.screen_name);
            articleCommentHodler.tvTime.setText(TimeFormatUtils.parseToYYMMDD(commentEntity
                    .created_at));
            articleCommentHodler.tvContent.setText(RichTextUtils.getRichText(mContext,
                    commentEntity.text));
            Glide.with(mContext).load(commentEntity.user.profile_image_url).into
                    (articleCommentHodler.ivHeader);
        }
    }

    @Override
    public int getItemCount() {
        return mCommentEntityList.size();
    }

    public class ArticleCommentPageHolder extends RecyclerView.ViewHolder {
        private ImageView ivHeader;
        private TextView tvUserName;
        private TextView tvTime;
        private TextView tvContent;

        public ArticleCommentPageHolder(View itemView) {
            super(itemView);
            initialize(itemView);
        }

        private void initialize(View itemView) {
            ivHeader = (ImageView) itemView.findViewById(R.id.ivHeader);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
        }


    }
}