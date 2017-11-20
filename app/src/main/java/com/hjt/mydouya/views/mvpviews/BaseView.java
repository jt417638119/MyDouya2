package com.hjt.mydouya.views.mvpviews;

import android.app.Activity;

/**
 * Created by ougonden on 17/11/20.
 */

public interface BaseView {
    Activity getActivity();
    void onError(String error);
}
