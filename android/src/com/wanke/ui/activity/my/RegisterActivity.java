package com.wanke.ui.activity.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.wanke.tv.R;
import com.wanke.ui.ToastUtil;
import com.wanke.ui.activity.BaseActivity;
import com.wanke.util.PreferenceUtil;
import com.wanke.util.RegexUtil;

public class RegisterActivity extends BaseActivity {
    public final static int REGISTER_SUCC = 0x11;

    private EditText mPassword, mAccount, mEmil, mPasswordConfirm;
    private Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    protected void initView() {
        mAccount = (EditText) findViewById(R.id.register_account);
        mPassword = (EditText) findViewById(R.id.login_password);
        mEmil = (EditText) findViewById(R.id.register_email);
        mPasswordConfirm = (EditText) findViewById(R.id.verify_password);
        mRegister = (Button) findViewById(R.id.register_btn);

        mRegister.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String account = mAccount.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String email = mEmil.getText().toString().trim();
                String passwordConfirm = mPasswordConfirm.getText().toString()
                        .trim();

                if (TextUtils.isEmpty(account)) {
                    ToastUtil.showToast(RegisterActivity.this,
                            R.string.login_username_is_empty);
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    ToastUtil.showToast(RegisterActivity.this,
                            R.string.register_email_is_empty);
                    return;
                }

                if (!RegexUtil.isValidEmail(email)) {
                    ToastUtil.showToast(RegisterActivity.this,
                            R.string.register_email_is_error);
                    return;
                }

                if (TextUtils.isEmpty(password)
                        || TextUtils.isEmpty(passwordConfirm)) {
                    ToastUtil.showToast(RegisterActivity.this,
                            R.string.login_password_is_empty);
                    return;
                }
                if (password.equals(passwordConfirm)) {
                    // TODO: 注册流程
                    PreferenceUtil.saveAccountInfo(account, password);
                    setResult(REGISTER_SUCC);
                    finish();
                } else {
                    ToastUtil.showToast(RegisterActivity.this,
                            R.string.confirm_password_is_error);
                    return;
                }
            }
        });

    }
}
