package com.example.administrator.testloginscau.myview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.example.administrator.testloginscau.R;


public class JlyySwipeRefreshLayout extends SwipeRefreshLayout {
    public JlyySwipeRefreshLayout(Context context) {
        super(context);
    }

    public JlyySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setColorSchemeResources(R.color.titleBackgroundColor);
    }
}
