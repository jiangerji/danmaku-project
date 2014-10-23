package com.wanke.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wanke.network.http.Constants;
import com.wanke.tv.R;
import com.wanke.ui.UiUtils;
import com.wanke.ui.activity.my.AboutActivity;
import com.wanke.ui.activity.my.InformationActivity;
import com.wanke.ui.activity.my.LoginActivity;
import com.wanke.ui.activity.my.SettingActivity;
import com.wanke.util.PreferenceUtil;

public class FragmentMy extends BaseFragment implements View.OnClickListener {

    private TextView mName;
    private View mAboutus, mSetting, mFav, mHistory, mInformation;
    private View mAvatarBg;
    private View mRootView = null;
    private ImageView mAvatar;

    // 是否已经有账户登录过
    private boolean mHaveAccount = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_my, container, false);

        initView();
        initListener();

        PreferenceUtil.registerPreferencesListener(prefListener);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        PreferenceUtil.unregisterPreferencesListener(prefListener);
    }

    private void initView() {
        mAvatarBg = mRootView.findViewById(R.id.account_avatar_bg);
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

        mAboutus = mRootView.findViewById(R.id.my_aboutus);
        mSetting = mRootView.findViewById(R.id.my_setting);
        mFav = mRootView.findViewById(R.id.my_fav);
        mHistory = mRootView.findViewById(R.id.my_history);
        mInformation = mRootView.findViewById(R.id.my_information);
        mName = (TextView) mRootView.findViewById(R.id.my_account_name);

        mAvatar = (ImageView) mRootView.findViewById(R.id.account_avatar);

        initAccountView();
    }

    private DisplayImageOptions mCircleOption = UiUtils.getOptionCircle();

    private void initAccountView() {
        String account = PreferenceUtil.getUsername();
        if (!TextUtils.isEmpty(account)) {
            mName.setText(account);
            mHaveAccount = true;
        } else {
            mName.setText(R.string.fragment_my_click_to_login);
            mHaveAccount = false;
        }

        // TODO: 显示头像
        String avatar = PreferenceUtil.getAvatar();

        if (!TextUtils.isEmpty(avatar)) {
            ImageLoader.getInstance()
                    .displayImage(Constants.buildImageUrl(avatar),
                            mAvatar, mCircleOption);
        } else {
            mAvatar.setImageResource(R.drawable.default_avatar);
        }
    }

    private SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences sharedPrefs,
                String key) {
            if (key.equals(PreferenceUtil.KEY_USERNAME)
                    || key.equalsIgnoreCase(PreferenceUtil.KEY_PASSWORD)) {
                initAccountView();
            }
        }
    };

    private void initListener() {
        mAvatar.setOnClickListener(this);
        mFav.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mAboutus.setOnClickListener(this);
        mInformation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
        case R.id.my_aboutus:
            intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
            break;

        case R.id.account_avatar:
            if (!mHaveAccount) {
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            } else {
                intent = new Intent(getActivity(), InformationActivity.class);
                startActivity(intent);
            }
            break;

        case R.id.my_setting:
            intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
            break;

        case R.id.my_information:
            break;

        default:
            break;
        }
        //        if (view == mAbout) {
        //            String account = sp.getString("account", "");
        //            if (TextUtils.isEmpty(account)) {
        //                Intent intent;
        //                intent = new Intent(getActivity(), MyLoginActivity.class);
        //                startActivity(intent);
        //            } else {
        //                Toast.makeText(getActivity(), R.string.toast_not_login, 1)
        //                        .show();
        //            }
        //
        //        }
        //        if (view == mInformation) {
        //            String account = sp.getString("account", "");
        //            if (TextUtils.isEmpty(account)) {
        //                Toast.makeText(getActivity(), R.string.toast_not_login, 1)
        //                        .show();
        //            } else {
        //                Intent intent;
        //                intent = new Intent(getActivity(), MyInformation.class);
        //                startActivity(intent);
        //            }
        //
        //        }
        //        if (view == mHistory) {
        //            String account = sp.getString("account", "");
        //            if (TextUtils.isEmpty(account)) {
        //                Intent intent;
        //                intent = new Intent(getActivity(), MyInformation.class);
        //                startActivity(intent);
        //            } else {
        //                Toast.makeText(getActivity(), R.string.toast_not_login, 1)
        //                        .show();
        //            }
        //
        //        }
        //        if (view == mLogin) {
        //            String account = sp.getString("account", "");
        //            if (TextUtils.isEmpty(account)) {
        //                Intent intent;
        //                intent = new Intent(getActivity(), MyLoginActivity.class);
        //                startActivity(intent);
        //            }
        //        }
        //        if (view == mAboutus) {
        //            Intent intent;
        //            intent = new Intent(getActivity(), MyAbout.class);
        //            startActivity(intent);
        //        }
        //        if (view == mSetting) {
        //            Intent intent;
        //            intent = new Intent(getActivity(), MySetting.class);
        //            startActivity(intent);
        //        }
    }
}
