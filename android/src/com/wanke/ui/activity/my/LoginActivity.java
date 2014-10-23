package com.wanke.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;

import com.wanke.tv.R;
import com.wanke.ui.ToastUtil;
import com.wanke.ui.activity.BaseActivity;
import com.wanke.util.PreferenceUtil;

public class LoginActivity extends BaseActivity {
    protected static final String TAG = "MyLoginActivity";
    private View mRegister, mLogin, mDeleteUsernameBtn, mAvatarBg;
    private EditText mPassword, mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(R.string.login);

        initView();
    }

    private void initView() {
        mRegister = findViewById(R.id.register_btn);
        mRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent;
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        mDeleteUsernameBtn = findViewById(R.id.delete_username_input_btn);
        mDeleteUsernameBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mAccount.setText("");
            }
        });

        mAccount = (EditText) findViewById(R.id.login_account);
        mPassword = (EditText) findViewById(R.id.login_password);

        mLogin = findViewById(R.id.login_btn);
        mLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String account = mAccount.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(account)) {
                    ToastUtil.showToast(LoginActivity.this,
                            R.string.login_username_is_empty);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.showToast(LoginActivity.this,
                            R.string.login_password_is_empty);
                    return;
                }

                String savedPassword = PreferenceUtil.getPassword();
                if (savedPassword == null || savedPassword.equals(password)) {
                    // TODO: 登录成功
                    PreferenceUtil.saveAccountInfo(account, password);
                    finish();
                } else {
                    ToastUtil.showToast(LoginActivity.this,
                            R.string.login_password_is_error);
                    return;
                }
            }
        });

        mAvatarBg = findViewById(R.id.account_avatar_bg);
        mAvatarBg.getViewTreeObserver()
                .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        int width = mAvatarBg.getWidth();
                        if (width > 0) {
                            int height = 480 * width / 854;
                            LayoutParams layoutParams = mAvatarBg.getLayoutParams();
                            layoutParams.height = height;
                            mAvatarBg.setLayoutParams(layoutParams);

                            mAvatarBg.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }

                    }
                });
    }

    @Override
    protected
            void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RegisterActivity.REGISTER_SUCC) {
            finish();
        }
    }
}
