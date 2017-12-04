package com.hjt.mydouya.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hjt.mydouya.R;
import com.hjt.mydouya.utils.CircleTransform;

/**
 * Created by HJT on 2017/11/14.
 */

public class MessageFragment extends BaseFragment {
    private View view;
    private ImageView ivComments;
    private ImageView ivAt;
    private ImageView ivLike;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_message,container,false);
        init();
        return view;
    }

    private void init() {
        ivComments = (ImageView) view.findViewById(R.id.ivComments);
        ivAt = (ImageView) view.findViewById(R.id.ivAt);
        ivLike = (ImageView) view.findViewById(R.id.ivLike);

        Glide.with(getActivity()).load(R.mipmap.ic_messages_comments).transform(new CircleTransform(getContext())).into(ivComments);
        Glide.with(getActivity()).load(R.mipmap.ic_messages_at).transform(new CircleTransform(getContext())).into(ivAt);
        Glide.with(getActivity()).load(R.mipmap.ic_messages_good).transform(new CircleTransform(getContext())).into(ivLike);
    }


}
