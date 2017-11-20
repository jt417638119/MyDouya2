package com.hjt.mydouya.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by ougonden on 17/11/20.
 */

public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {
    public PullToRefreshRecyclerView(Context context) {
        super(context);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView recyclerView = new RecyclerView(context,attrs);
        recyclerView.setId(com.handmark.pulltorefresh.library.R.id.recyclerview);
        return recyclerView;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        if (mRefreshableView.getChildCount() == 0) {
            return true;
        }
        int count = mRefreshableView.getChildCount();
        View view = mRefreshableView.getChildAt(count - 1); // 获取最后一个View
        int position = mRefreshableView.getChildLayoutPosition(view);
        if (position >= mRefreshableView.getAdapter().getItemCount() - 1) {
            return view.getBottom() <= mRefreshableView.getBottom();
        }
        return false;
    }

    @Override
    protected boolean isReadyForPullStart() {
        if (mRefreshableView.getChildCount() == 0) {
            return true;
        }
        View view = mRefreshableView.getChildAt(0);
        int position = mRefreshableView.getChildLayoutPosition(view);
        if (position == 0) {
            return view.getTop() == mRefreshableView.getTop();
        }
        return false;
    }
}
