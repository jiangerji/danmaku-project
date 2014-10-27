package com.wanke.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.wanke.tv.R;
import com.wanke.ui.activity.BaseActivity;
import com.wanke.ui.activity.FeedbackActivity;
import com.wanke.ui.widget.SwitchSeekBar;
import com.wanke.ui.widget.SwitchSeekBar.OnCheckedChangeListener;
import com.wanke.util.PreferenceUtil;

public class SettingActivity extends BaseActivity {

    private RelativeLayout mChangePassword;

    private SwitchSeekBar mHardwareDecode;
    private SwitchSeekBar mAutoPlayIn23G;
    private SeekBar mDanmakuAlphaSetting;
    private View mFeedbackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //        mChangePassword = (RelativeLayout) findViewById(R.id.tv_login_changepassword);
        //        click();

        mHardwareDecode = (SwitchSeekBar) findViewById(R.id.hardware_decoder);
        mHardwareDecode.setChecked(PreferenceUtil.getDecodeByHardware());
        mHardwareDecode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(boolean isChecked) {
                PreferenceUtil.setDecodeByHardware(isChecked);
            }
        });

        mAutoPlayIn23G = (SwitchSeekBar) findViewById(R.id.auto_play_setting);
        mAutoPlayIn23G.setChecked(PreferenceUtil.getAutoPlayIn23G());
        mAutoPlayIn23G.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(boolean isChecked) {
                PreferenceUtil.setAutoPlayIn23G(isChecked);
            }
        });

        mDanmakuAlphaSetting = (SeekBar) findViewById(R.id.danmaku_alpha_setting);
        mDanmakuAlphaSetting.setMax(70);
        mDanmakuAlphaSetting.setProgress((int) ((PreferenceUtil.getDanmakuAlpha() - 0x30)
                * 70.f / (0xFF - 0x30)));
        mDanmakuAlphaSetting.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress();
                PreferenceUtil.setDanmakuAlpha((int) (value
                        * ((255.0f - 0x30) / 70.0f) + 0x30));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(
                    SeekBar seekBar, int progress, boolean fromUser) {

            }
        });

        mFeedbackBtn = findViewById(R.id.feedback_btn);
        mFeedbackBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });
    }

    public void click() {
        mChangePassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //                Intent intent;
                //                intent = new Intent(SettingActivity.this, ChangePassword.class);
                //                startActivity(intent);
            }
        });
    }

}
