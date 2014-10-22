package com.wanke.ui.activity.my;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.wanke.tv.R;
import com.wanke.ui.activity.BaseActivity;

public class SettingActivity extends BaseActivity {

    private RelativeLayout mChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //        mChangePassword = (RelativeLayout) findViewById(R.id.tv_login_changepassword);
        //        click();
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
