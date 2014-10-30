package com.wanke.ui.activity.my;

import java.io.File;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wanke.model.AccountInfo;
import com.wanke.network.http.Constants;
import com.wanke.tv.R;
import com.wanke.ui.UiUtils;
import com.wanke.ui.activity.BaseActivity;
import com.wanke.ui.crop.Crop;
import com.wanke.util.AccountUtil;
import com.wanke.util.AccountUtil.UserInfoCallback;
import com.wanke.util.CacheManager;
import com.wanke.util.PreferenceUtil;

public class InformationActivity extends BaseActivity {
    public final static String KEY_UID = "uid";

    public final static int LOGOUT_SUCC = 0x21;

    private EditText mEmail;
    private TextView mName, mFans;
    private Button mExit;
    SharedPreferences.Editor editor = null;

    private String mUid = "";

    private String[] mItems = new String[] { "选择本地图片", "拍照" };

    // 保存用户选择的图片
    private File mBgImageFile = null;
    private final int PHOTO_PICKED_WITH_DATA = 1;
    private final int CAMERA_WITH_DATA = 2;
    private final int NATIVE_CAMERA_WITH_DATA = 3;
    private int ICON_SIZE = 480;

    // 是否有被编辑过
    private boolean mIsEdit = false;

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

    private View mMaleBtn, mFemaleBtn;

    private void initView() {
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

        mAvatar = (ImageView) findViewById(R.id.account_avatar);
        mAvatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                showDialog();
            }
        });
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

        mFans = (TextView) findViewById(R.id.fans);

        mExit = (Button) findViewById(R.id.logout_btn);

        mExit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PreferenceUtil.clearAccount();
                setResult(LOGOUT_SUCC);
                finish();
            }
        });

        mMaleBtn = findViewById(R.id.male_checkbox);
        mMaleBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setGender(AccountInfo.MALE);
            }
        });
        mFemaleBtn = findViewById(R.id.female_checkbox);
        mFemaleBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setGender(AccountInfo.FEMALE);
            }
        });
    }

    private int mGender = 0;

    private void setGender(int gender) {
        mGender = gender;
        if (mGender == AccountInfo.MALE) {
            mMaleBtn.setSelected(true);
            mFemaleBtn.setSelected(false);
        } else if (mGender == AccountInfo.FEMALE) {
            mMaleBtn.setSelected(false);
            mFemaleBtn.setSelected(true);
        } else {
            mMaleBtn.setSelected(false);
            mFemaleBtn.setSelected(false);
        }
    }

    private void setAccountInfo(AccountInfo info) {
        if (info != null) {
            mEmail.setText(info.getEmail());
            mName.setText(info.getUsername());
            mFans.setText(getResources().getString(R.string.information_activity_fans,
                    info.getFans()));
            setGender(info.getGender());
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

    /**
     * 显示选择对话框
     */
    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("设置头像")
                .setItems(mItems, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                        case 0:
                            doPickPhotoFromGallery();
                            break;
                        case 1:
                            doTakePhoto();
                            break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    protected
            void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        mIsEdit = true;

        switch (requestCode) {
        case NATIVE_CAMERA_WITH_DATA:
            String photo = data.getExtras().getString("photo");

            if (photo != null && !photo.isEmpty()) {
                Log.i(TAG, "photo =========== " + photo);
                mBgImageFile = new File(photo);
                setContentBackground(photo);
            } else {
                Toast.makeText(this, "获取图片失败！", Toast.LENGTH_SHORT).show();
            }

            break;
        case PHOTO_PICKED_WITH_DATA:
            Log.d(TAG,
                    "PHOTO_PICKED_WITH_DATA:"
                            + (data != null ? data.getData() : "null"));

            if (mBgImageFile != null) {
                setContentBackground(mBgImageFile.getAbsolutePath());
            } else {
                Toast.makeText(this, "获取图片失败！", Toast.LENGTH_SHORT).show();
            }
            break;

        case Crop.REQUEST_PICK:
            Uri uri = data.getData();
            Log.i(TAG, "uri======================================" + uri);
            resizeImage(uri);
            break;

        case CAMERA_WITH_DATA:
            Log.d(TAG, "CAMERA_WITH_DATA:"
                    + (data != null ? data.getData() : "null"));
            doCropPhoto(mPhotoFile);
            break;
        case Crop.REQUEST_CROP:
            if (mBgImageFile != null) {
                setContentBackground(mBgImageFile.getAbsolutePath());
            } else {
            }
            break;
        default:
            Log.w(TAG, "onActivityResult : invalid request code");

        }
    }

    private boolean isSdcardExisting() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 从gallay中获取图片
     */
    protected void doPickPhotoFromGallery() {
        try {
            mBgImageFile = CacheManager.getInstance().mkCacheFile("msgbg");
            //  if (false) {
            final Intent intent = getPhotoPickIntent(mBgImageFile);
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
            //} else {
            //  Crop.pickImage(this);
            //}

            Log.d(TAG, "获取在图片：" + mBgImageFile.getAbsolutePath());
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "获取照片失败！",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 从gallery中获取图片
     * 
     * @return the intent
     */
    public Intent getPhotoPickIntent(File f) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", ICON_SIZE);
        intent.putExtra("outputY", ICON_SIZE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    /**
     * Launches Camera to take a picture and store it in a file.
     */
    private File mPhotoFile = null;

    private void doTakePhoto() {
        try {
            // Launch camera to take photo for selected contact
            mPhotoFile = CacheManager.getInstance().mkCacheFile("photo");
            final Intent intent = getTakePickIntent(mPhotoFile);

            startActivityForResult(intent, CAMERA_WITH_DATA);
            Log.d(TAG, "获得camera照片位置:" + mPhotoFile.getAbsolutePath());
        } catch (ActivityNotFoundException e) {

        }
    }

    /**
     * 启动获取图片的intent
     * 
     * @param f
     *            将获取的照片保存的到文件中
     * @return
     */
    public static Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    private void setContentBackground(String bg) {
        if (bg != null) {
            Log.i(TAG, "bg ===" + bg);
            //  mAvatar.setImageDrawable(Drawable.createFromPath(bg));
            ImageLoader.getInstance()
                    .displayImage(Constants.SDImageUrl(bg),
                            mAvatar, mCircleOption);
        }
    }

    /**
     * Sends a newly acquired photo to Gallery for cropping.
     * 
     * @param f
     *            the image file to crop
     */
    protected void doCropPhoto(File f) {
        Log.d(TAG, "裁剪图片：" + f != null ? f.getAbsolutePath() : "");
        try {
            mBgImageFile = CacheManager.getInstance().mkCacheFile("1234");
            Uri inputUri = Uri.fromFile(f);
            Uri outputUri = Uri.fromFile(mBgImageFile);
            new Crop(inputUri).output(outputUri).asSquare()
                    .withMaxSize(ICON_SIZE, ICON_SIZE).start(this);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "剪裁失败！",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        mBgImageFile = CacheManager.getInstance().mkCacheFile("1234");
        Uri outputUri = Uri.fromFile(mBgImageFile);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", ICON_SIZE);
        intent.putExtra("outputY", ICON_SIZE);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        startActivityForResult(intent, 2);
    }

    //    protected void doCropPhoto(Uri inputUri) {
    //        Log.d(TAG, "裁剪图片：" + inputUri);
    //        try {
    //            mBgImageFile = CacheManager.getInstance().mkCacheFile("1234");
    //            Log.i(TAG, "mBgImageFile======================：" + mBgImageFile);
    //            Uri outputUri = Uri.fromFile(mBgImageFile);
    //            Log.i(TAG, "outputUri+++++======================================"
    //                    + outputUri);
    //            new Crop(inputUri).output(outputUri).asSquare()
    //                    .withMaxSize(ICON_SIZE, ICON_SIZE).start(this);
    //        } catch (ActivityNotFoundException e) {
    //            Toast.makeText(this, "剪裁失败！",
    //                    Toast.LENGTH_LONG).show();
    //        }
    //    }

}
