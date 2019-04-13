package com.example.administrator.testloginscau.activity;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.example.administrator.testloginscau.R;

public class MainActivity extends ActivityGroup implements  OnClickListener{

    private static final String TAG = "MainActivity";
    private Bundle mBundle = new Bundle();
    private LinearLayout ll_container, ll_first, ll_second, ll_third;
    private String userCredit;
    private String sessionId = "null";
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        ll_first = (LinearLayout) findViewById(R.id.ll_first);
        ll_second = (LinearLayout) findViewById(R.id.ll_second);
        ll_third = (LinearLayout) findViewById(R.id.ll_third);
        ll_first.setOnClickListener(this);
        ll_second.setOnClickListener(this);
        ll_third.setOnClickListener(this);
        mBundle.putString("tag", TAG);
        changeContainerView(ll_first);

        Intent intent1 = getIntent();
        name = intent1.getStringExtra("name");
        userCredit = intent1.getStringExtra("credit");
        sessionId = intent1.getStringExtra("sessionId");
    }
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.ll_first || v.getId()==R.id.ll_second || v.getId()==R.id.ll_third) {
            changeContainerView(v);
        }
    }

    public void changeContainerView(View v) {
        ll_first.setSelected(false);
        ll_second.setSelected(false);
        ll_third.setSelected(false);
        v.setSelected(true);
        if (v == ll_first) {
            toActivity("first", NewsActivity.class);
        } else if (v == ll_second) {
            toActivity("second", GetScoreActivity.class);
        } else if (v == ll_third) {
            toActivity("second", GetCourseActivity.class);
        }
    }

    private void toActivity(String label, Class<?> cls){

        Intent intent = new Intent(this, cls);
        intent.putExtra("credit", userCredit);
        intent.putExtra("name", name);
        intent.putExtra("sessionId", sessionId);
        ll_container.removeAllViews();
        View v = getLocalActivityManager().startActivity(label, intent).getDecorView();
        v.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ll_container.addView(v);
    }
}
