package com.wanke.ui.activity.my;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wanke.tv.R;
import com.wanke.ui.activity.BaseActivity;
import com.wanke.util.PreferenceUtil;

public class InformationActivity extends BaseActivity {
    public final static int LOGOUT_SUCC = 0x21;

    private EditText mEmail;
    private TextView mName;
    private Button mExit;
    SharedPreferences.Editor editor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        initView();
    }

    private void initView() {
        mEmail = (EditText) findViewById(R.id.account_email);
        mName = (TextView) findViewById(R.id.account_name);
        mExit = (Button) findViewById(R.id.logout_btn);

        mExit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PreferenceUtil.clearAccount();
                setResult(LOGOUT_SUCC);
                finish();
            }
        });
    }

}
