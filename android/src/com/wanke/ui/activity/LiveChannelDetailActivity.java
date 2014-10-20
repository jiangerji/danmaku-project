package com.wanke.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.wanke.tv.R;

public class LiveChannelDetailActivity extends BaseActivity {
    public final static String CHANNEL_ID = "channelId";

    @Override
    protected int getFlag() {
        return FLAG_NO_SEARCH_VIEW;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_video_detail);

        initView();
    }

    private void initView() {
        View view = findViewById(R.id.video_play_btn);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LiveChannelDetailActivity.this,
                        VideoActivity.class);
                startActivity(intent);
            }
        });
    }
}
