package com.wanke.ui.activity.my;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wanke.model.AccountInfo;
import com.wanke.network.http.Constants;
import com.wanke.tv.R;
import com.wanke.ui.UiUtils;
import com.wanke.ui.activity.BaseActivity;
import com.wanke.util.AccountUtil;
import com.wanke.util.AccountUtil.UserInfoCallback;
import com.wanke.util.PreferenceUtil;

public class InformationActivity extends BaseActivity {
    public final static String KEY_UID = "uid";

    public final static int LOGOUT_SUCC = 0x21;

    private EditText mEmail;
    private TextView mName;
    private Button mExit;
    SharedPreferences.Editor editor = null;

    private String mUid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        setTitle(R.string.information_activity_title);

        Intent intent = getIntent();
        mUid = intent.getStringExtra(KEY_UID);

        if (TextUtils.isEmpty(mUid)) {
            finish();
        } else {
            initView();

            getUserInfo();
        }
    }

    private View mAvatarBg;
    private ImageView mAvatar;
    private DisplayImageOptions mCircleOption = UiUtils.getOptionCircle();

    private void initView() {
        mAvatarBg = findViewById(R.id.account_avatar_bg);
        mAvatarBg.getViewTreeObserver()
                .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

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

        mAvatar = (ImageView) findViewById(R.id.account_avatar);
        String avatar = PreferenceUtil.getAvatar();

        if (!TextUtils.isEmpty(avatar)) {
            ImageLoader.getInstance()
                    .displayImage(Constants.buildImageUrl(avatar),
                            mAvatar, mCircleOption);
        } else {
            mAvatar.setImageResource(R.drawable.default_avatar);
        }

        mEmail = (EditText) findViewById(R.id.account_email);

        mName = (TextView) findViewById(R.id.account_name);
        mName.setText(PreferenceUtil.getUsername());

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

    private void setAccountInfo(AccountInfo info) {
        if (info != null) {
            mEmail.setText(info.getEmail());
            mName.setText(info.getUsername());
        }
    }

    private void getUserInfo() {
        showWaitingDialog();

        AccountUtil.userInfo(mUid, new UserInfoCallback() {

            @Override
            public void onUserInfoSuccess(final AccountInfo info) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        setAccountInfo(info);
                    }
                });
                dismissWaitingDialog();
            }

            @Override
            public void onUserInfoFailed(int error, String msg) {
                showToast(R.string.information_activity_get_user_info_error);
                dismissWaitingDialog();
            }

            @Override
            public void onUserInfoException(String msg) {
                showToast(R.string.information_activity_get_user_info_error);
                dismissWaitingDialog();
            }
        });
    }
}
