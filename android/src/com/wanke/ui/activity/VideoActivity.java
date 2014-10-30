package com.wanke.ui.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

import master.flame.danmaku.ui.widget.DanmakuSurfaceView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wanke.danmaku.DanmakuController;
import com.wanke.danmaku.DanmakuController.DanmakuListener;
import com.wanke.danmaku.DanmakuManager;
import com.wanke.danmaku.protocol.PushChatResponse;
import com.wanke.network.http.CommonHttpUtils;
import com.wanke.tv.R;
import com.wanke.ui.ToastUtil;
import com.wanke.ui.UiUtils;
import com.wanke.ui.adapter.HotDamankuAdapter;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class VideoActivity extends BaseActivity implements
        SurfaceHolder.Callback,
        IVideoPlayer {
    public final static String TAG = "VideoActivity";

    public final static String KEY_ROOM_TITLE = "roomTitle";

    public final static String LOCATION = "com.compdigitec.libvlcandroidsample.VideoActivity.location";

    private String mFilePath;

    // display surface
    private SurfaceView mSurface;
    private SurfaceHolder mHolder;
    private FrameLayout mSurfaceFrame;

    // media player
    private LibVLC mLibvlc;
    private final static int VideoSizeChanged = -1;

    private View mRootView;
    private View mVideoControllContainer;

    private String mRoomTitle;

    @SuppressLint("InflateParams")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mRootView = getLayoutInflater().inflate(R.layout.activity_video_play,
                null);
        requestFullScreen();

        setContentView(mRootView);

        // Receive path to play from intent
        Intent intent = getIntent();
        Bundle extraBundle = intent.getExtras();
        if (extraBundle != null) {
            mFilePath = extraBundle.getString(LOCATION);
            mRoomTitle = extraBundle.getString(KEY_ROOM_TITLE);
        }

        mFilePath = "file:///storage/sdcard0/1.mp4";

        Log.d(TAG, "Playing back " + mFilePath);

        mSurfaceFrame = (FrameLayout) findViewById(R.id.player_surface_frame);
        mSurface = (SurfaceView) findViewById(R.id.video_surface);
        mHolder = mSurface.getHolder();
        mHolder.addCallback(this);

        initVideoPanel();
        initDanmaku();
    }

    // 控制是否显示弹幕
    private ImageView mDanmakuSwitch = null;
    private Chronometer mElapseTime = null;

    private View mTopPanel = null;
    private TextView mTopPanelTime;

    private View mTopDanmakuPanel;
    private View mTopDanmakuPanelHotBtn;
    private EditText mTopDanmakuPanelContent;
    private View mTopDanmakuPanelSendBtn;
    private View mInvokeDanmakuPanelBtn;
    private ImageView mLockPlayerBtn;

    private ImageView mVideoPlayerPlayBtn;
    private TextView mScreenAdjust;
    private View mSettingBtn;

    /**
     * 初始化视频播放的工具栏
     * 
     * @param videoController
     */
    private void initVideoPanel() {
        mNavigationBarHeight = getNavigationBarHeight();
        Log.d(TAG, "navigation bar height:" + mNavigationBarHeight);

        // 初始化底部控制栏
        mVideoControllContainer = findViewById(R.id.video_controll_container);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        if (!isTablet(this)) {
            layoutParams.setMargins(0, 0, mNavigationBarHeight, 0);
        } else {
            layoutParams.setMargins(0, 0, 0, 0);
        }
        mVideoControllContainer.setLayoutParams(layoutParams);

        mVideoPlayerPlayBtn = (ImageView) findViewById(R.id.video_player_panel_controller_btn);
        mVideoPlayerPlayBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                togglePlayState();
            }
        });

        TextView roomTitle = (TextView) findViewById(R.id.video_title);
        roomTitle.setText(mRoomTitle);
        roomTitle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View shareBtn = findViewById(R.id.video_top_panel_share);
        shareBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openShare();
            }
        });

        // init top panel
        mTopPanel = findViewById(R.id.video_top_panel);
        layoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        if (!isTablet(this)) {
            layoutParams.setMargins(0, 0, mNavigationBarHeight, 0);
        } else {
            layoutParams.setMargins(0, 0, 0, 0);
        }
        mTopPanel.setLayoutParams(layoutParams);

        // 顶部时间控件
        mTopPanelTime = (TextView) mTopPanel.findViewById(R.id.video_top_panel_time);
        updateTime();

        mDanmakuSwitch = (ImageView) mVideoControllContainer.findViewById(R.id.danmuku_btn);
        mDanmakuSwitch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //                sendDanmaku("Hello World!");
                if (DanmakuManager.getInstance().isShown()) {
                    DanmakuManager.getInstance().hide();
                    mDanmakuSwitch.setImageResource(R.drawable.video_player_open_danmaku);
                } else {
                    DanmakuManager.getInstance().show();
                    mDanmakuSwitch.setImageResource(R.drawable.video_player_close_danmaku);
                }
            }
        });

        mScreenAdjust = (TextView) findViewById(R.id.screen_adjust_btn);
        mScreenAdjust.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCurrentScreenState == SCREEN_AUTO) {
                    mCurrentScreenState = SCREEN_FULL;
                } else if (mCurrentScreenState == SCREEN_FULL) {
                    mCurrentScreenState = SCREEN_16x9;
                } else {
                    mCurrentScreenState = SCREEN_AUTO;
                }

                adjustScreen();
            }
        });

        mSettingBtn = findViewById(R.id.setting_btn);
        mSettingBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openSetting();
            }
        });

        mElapseTime = (Chronometer) mVideoControllContainer.findViewById(R.id.elapse_time);
        mElapseTime.start();

        // 初始化顶部弹幕工具栏
        mTopDanmakuPanel = findViewById(R.id.video_top_danmaku_panel);
        mTopDanmakuPanelContent = (EditText) findViewById(R.id.video_top_danmaku_panel_content);
        mTopDanmakuPanelHotBtn = findViewById(R.id.video_top_danmaku_panel_hot_danmaku);
        mTopDanmakuPanelSendBtn = findViewById(R.id.video_top_danmaku_panel_send);
        mInvokeDanmakuPanelBtn = findViewById(R.id.video_invoke_danmaku_panel_btn);

        layoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        if (!isTablet(this)) {
            layoutParams.setMargins(0, 0, mNavigationBarHeight, 0);
        } else {
            layoutParams.setMargins(0, 0, 0, 0);
        }
        mTopDanmakuPanel.setLayoutParams(layoutParams);

        mInvokeDanmakuPanelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mTopDanmakuPanel.getVisibility() == View.VISIBLE) {
                    hideDanmakuPanel();
                } else {
                    hideVideoBarOnly();
                    showDanmakuPanel();
                }
            }
        });

        mTopDanmakuPanelHotBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mHotDanmakus != null) {
                    if (mHotDanmakus.getVisibility() == View.INVISIBLE) {
                        mHotDanmakus.setVisibility(View.VISIBLE);
                    } else {
                        mHotDanmakus.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        mTopDanmakuPanelSendBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String danmakuContent = mTopDanmakuPanelContent.getText()
                        .toString();
                if (TextUtils.isEmpty(danmakuContent)) {
                    return;
                }

                //                DanmakuController.getInstance().sendChat(danmakuContent);
                sendDanmaku(danmakuContent);
                mTopDanmakuPanelContent.setText("");
            }
        });

        initHotDanmakuList();

        mLockPlayerBtn = (ImageView) findViewById(R.id.video_player_lock_btn);
        mLockPlayerBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleLockState();
            }
        });
    }

    /**
     * 打开设置界面
     */
    private void openSetting() {
        showToast("waiting :)");
    }

    private boolean mLockState = false;

    /**
     * 切换锁定播放器状态
     */
    private void toggleLockState() {
        mLockState = !mLockState;

        if (mLockState) {
            mLockPlayerBtn.setImageResource(R.drawable.video_player_locked);
            mLockPlayerBtn.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.fade_out);
            animation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mLockPlayerBtn.setAlpha(0.3f);
                }
            });
            mLockPlayerBtn.startAnimation(animation);

            hideVideoBar();
            hideDanmakuPanel();
        } else {
            mLockPlayerBtn.setImageResource(R.drawable.video_player_unlock);
            mLockPlayerBtn.clearAnimation();
            mLockPlayerBtn.setAlpha(1.0f);
        }
    }

    private int mCurrentScreenState = SCREEN_AUTO;

    private final static int SCREEN_AUTO = 1;
    private final static int SCREEN_FULL = 2;
    private final static int SCREEN_16x9 = 3;

    private void adjustScreen() {
        // TODO: 调整分辨率
        int w = getWindow().getDecorView().getWidth();
        int h = getWindow().getDecorView().getHeight();

        switch (mCurrentScreenState) {
        case SCREEN_AUTO:
            mScreenAdjust.setText(R.string.screen_scale_type_full);
            break;

        case SCREEN_FULL:
            mScreenAdjust.setText(R.string.screen_scale_type_16x9);
            //            Message message = Message.obtain(mHandler, VideoSizeChanged, w, h);
            //            message.sendToTarget();
            break;

        case SCREEN_16x9:
            mScreenAdjust.setText(R.string.screen_scale_type_auto);
            break;

        default:
            break;
        }
    }

    private void openShare() {
        // TODO: 实现分享接口
        showToast("open share!");
    }

    private boolean mPlayState = true;

    private void togglePlayState() {
        if (mLibvlc != null) {
            if (mPlayState) {
                mLibvlc.pause();
            } else {
                mLibvlc.play();
            }
        }

        mPlayState = !mPlayState;
        if (mPlayState) {
            mVideoPlayerPlayBtn.setImageResource(R.drawable.video_player_pause);
        } else {
            mVideoPlayerPlayBtn.setImageResource(R.drawable.video_player_play);
        }
    }

    private ListView mHotDanmakus;
    private HotDamankuAdapter mHotDamankuAdapter;

    /**
     * 初始化热门弹幕列表
     */
    private void initHotDanmakuList() {
        mHotDanmakus = (ListView) findViewById(R.id.video_top_danmaku_panel_hot_danmaku_list);//new ListView(this);
        int width = UiUtils.getScreenWidth(this) / 5 * 2;

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                width, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_RIGHT,
                R.id.video_top_danmaku_panel);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.video_top_danmaku_panel);
        layoutParams.setMargins(0, 2, 0, 0);
        mHotDanmakus.setLayoutParams(layoutParams);

        mHotDamankuAdapter = new HotDamankuAdapter(this);
        mHotDanmakus.setAdapter(mHotDamankuAdapter);

        mHotDanmakus.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id) {
                String content = (String) mHotDamankuAdapter.getItem(position);
                sendDanmaku(content);
            }
        });

        getHotDanmakus();
    }

    private void getHotDanmakus() {
        String action = "danmaku";

        CommonHttpUtils.get(action, null, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    int error = object.getInt("error");
                    if (error == 0) {
                        ArrayList<String> danmakus = new ArrayList<String>();
                        JSONArray datas = object.getJSONArray("data");
                        for (int i = 0; i < datas.length(); i++) {
                            danmakus.add(datas.get(i).toString());
                        }

                        mHotDamankuAdapter.set(danmakus);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                // 获取热词失败
            }
        }, "hot-danmaku", 30);
    }

    private long mPreSendDanmakuTime = 0;

    private void sendDanmaku(String content) {
        long currTime = System.currentTimeMillis();
        if (currTime - mPreSendDanmakuTime > 3000) {
            DanmakuManager.getInstance().sendDanmaku(content);
            mPreSendDanmakuTime = currTime;
        } else {
            mDanmakuHandler.sendEmptyMessage(DANMAKU_SEND_TOO_FAST);
        }
    }

    private DanmakuSurfaceView mDanmakuView;

    private void initDanmaku() {
        mDanmakuView = (DanmakuSurfaceView) findViewById(R.id.danmamu_surface);
        if (mDanmakuView != null) {
            DanmakuManager.getInstance()
                    .init((DanmakuSurfaceView) mDanmakuView);
            DanmakuManager.getInstance().start();
        }
    }

    private void deinitDanmaku() {
        DanmakuManager.getInstance().deinit();
    }

    /**
     * 判断当前设备是手机还是平板，代码来自Google I/O App for Android
     * 
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    int mNavigationBarHeight = 0;

    private int getNavigationBarHeight() {
        Resources resources = getResources();
        // navigation_bar_height
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen",
                "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    int mUiState = 1;// 0 hide: 1: visible, 2: all visible

    private int uiHideOptions = 0;
    private int uiVisibleOptions = 0;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void requestFullScreen() {
        uiHideOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

        uiVisibleOptions = View.SYSTEM_UI_FLAG_VISIBLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mRootView.setSystemUiVisibility(uiHideOptions);

        View fullScreenController = mRootView
                .findViewById(R.id.full_screen_controller);

        fullScreenController.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mLockState) {
                    mLockPlayerBtn.clearAnimation();
                    mLockPlayerBtn.setAlpha(1.0f);
                    return;
                }

                int i = mRootView.getSystemUiVisibility();
                Log.d(TAG, "system ui visibility:" + i);

                if ((i & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) {
                    mRootView.setSystemUiVisibility(uiVisibleOptions);
                    mVideoControllContainer.setVisibility(View.INVISIBLE);
                } else if ((i & View.SYSTEM_UI_FLAG_VISIBLE) == View.SYSTEM_UI_FLAG_VISIBLE
                        && mVideoControllContainer.getVisibility() == View.INVISIBLE) {
                    hideDanmakuPanel();
                    showVideoBar();
                } else {
                    hideVideoBar();
                }
            }
        });
    }

    /**
     * 显示顶部弹幕工具栏
     */
    private void showDanmakuPanel() {
        if (mTopDanmakuPanel.getVisibility() == View.VISIBLE) {
            return;
        }

        if (mTopDanmakuPanel != null) {
            mTopDanmakuPanel.startAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.slide_in_top));
        }
        mTopDanmakuPanel.setVisibility(View.VISIBLE);
    }

    private void hideDanmakuPanel() {
        if (mTopDanmakuPanel.getVisibility() == View.INVISIBLE) {
            return;
        }

        if (mTopDanmakuPanel != null) {
            mTopDanmakuPanel.startAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.slide_out_top));
        }
        mTopDanmakuPanel.setVisibility(View.INVISIBLE);

        if (mHotDanmakus != null) {
            mHotDanmakus.setVisibility(View.INVISIBLE);
        }
    }

    // 显示视频播放工具栏
    private void showVideoBar() {
        if (mVideoControllContainer.getVisibility() == View.INVISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.slide_in_bottom);
            mVideoControllContainer.setVisibility(View.VISIBLE);
            animation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mRootView.setSystemUiVisibility(uiVisibleOptions);
                    mVideoControllContainer.setVisibility(View.VISIBLE);
                }
            });
            mVideoControllContainer.startAnimation(animation);
        }

        if (mTopPanel.getVisibility() == View.INVISIBLE) {
            mTopPanel.setVisibility(View.VISIBLE);
            mTopPanel.startAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.slide_in_top));
        }
    }

    // 隐藏视频播放工具栏
    private void hideVideoBar() {
        if (mVideoControllContainer.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.slide_out_bottom);
            mVideoControllContainer.setVisibility(View.INVISIBLE);
            animation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mRootView.setSystemUiVisibility(uiHideOptions);
                    mVideoControllContainer.setVisibility(View.INVISIBLE);
                }
            });
            mVideoControllContainer.startAnimation(animation);
        } else {
            mRootView.setSystemUiVisibility(uiHideOptions);
        }

        if (mTopPanel.getVisibility() == View.VISIBLE) {
            mTopPanel.setVisibility(View.INVISIBLE);
            mTopPanel.startAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.slide_out_top));
        }
    }

    private void hideVideoBarOnly() {
        if (mVideoControllContainer.getVisibility() == View.VISIBLE) {
            mVideoControllContainer.setVisibility(View.INVISIBLE);
            mVideoControllContainer.startAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.slide_out_bottom));
        }

        if (mTopPanel.getVisibility() == View.VISIBLE) {
            mTopPanel.setVisibility(View.INVISIBLE);
            mTopPanel.startAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.slide_out_top));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setSize(mVideoWidth, mVideoHeight);
    }

    @Override
    protected void onStart() {
        super.onStart();

        createPlayer(mFilePath);
        DanmakuController.getInstance().connect(new DanmakuListener() {

            @Override
            public void onSendChatStatus(int status) {
                // 发送失败
            }

            @Override
            public void onPushChatReceive(PushChatResponse pushChatResponse) {
                // 收到弹幕
                Message message = mDanmakuHandler.obtainMessage(DANMAKU_PUSH_CAHT);
                message.obj = pushChatResponse;
                mDanmakuHandler.sendMessage(message);
            }

            @Override
            public void onLoginStatus(int status) {
                Log.d(TAG, "Login Status:" + status);
                // 登录状态
                if (status < 0) {
                    mDanmakuHandler.sendEmptyMessage(DANMAKU_LOGIN_FAILED);
                }
            }

            @Override
            public void onConnectionStatus(int status) {
                // 连接状态
                Log.d(TAG, "Connection Status:" + status);
                if (status < 0) {
                    mDanmakuHandler.sendEmptyMessage(DANMAKU_CONNECTION_FAILED);
                }
            }
        });
    }

    private final static int DANMAKU_PUSH_CAHT = 0x01;

    // 连接弹幕服务器失败
    private final static int DANMAKU_CONNECTION_FAILED = 0x00010001;
    // 登录弹幕服务器失败
    private final static int DANMAKU_LOGIN_FAILED = 0x00010002;
    // 发送弹幕速度太快
    private final static int DANMAKU_SEND_TOO_FAST = 0x00020001;
    private Toast mDanmakuSendTooFast = null;
    private Handler mDanmakuHandler = new Handler(new Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
            case DANMAKU_PUSH_CAHT:
                String chatContent = ((PushChatResponse) msg.obj).getContent();
                sendDanmaku(chatContent);
                break;

            case DANMAKU_CONNECTION_FAILED:
                ToastUtil.showToast(VideoActivity.this,
                        R.string.danmaku_connection_failed);
                break;

            case DANMAKU_LOGIN_FAILED:
                ToastUtil.showToast(VideoActivity.this,
                        R.string.danmaku_login_failed);
                break;

            case DANMAKU_SEND_TOO_FAST:
                try {
                    if (mDanmakuSendTooFast != null) {
                        mDanmakuSendTooFast.cancel();
                    }
                } catch (Exception e) {
                }

                mDanmakuSendTooFast = ToastUtil.showToast(VideoActivity.this,
                        R.string.danmaku_send_too_fast);
                break;

            default:
                break;
            }
            return false;
        }
    });

    @Override
    protected void onStop() {
        super.onStop();

        releasePlayer();
        DanmakuController.getInstance().disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        deinitDanmaku();
    }

    // size of the video
    private int mVideoHeight;
    private int mVideoWidth;
    private int mVideoVisibleHeight;
    private int mVideoVisibleWidth;
    private int mSarNum;
    private int mSarDen;

    /*************
     * Surface
     *************/
    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceChanged(SurfaceHolder surfaceholder, int format,
            int width, int height) {
        if (mLibvlc != null) {
            if (mHolder != null) {
                mLibvlc.attachSurface(mHolder.getSurface(), this);
            }
        }
    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
    }

    private void setSize(int width, int height) {
        int sw;
        int sh;

        // get screen size
        sw = getWindow().getDecorView().getWidth();
        sh = getWindow().getDecorView().getHeight();

        double dw = sw, dh = sh;
        boolean isPortrait;

        isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        if (sw > sh && isPortrait || sw < sh && !isPortrait) {
            dw = sh;
            dh = sw;
        }

        // sanity check
        if (dw * dh == 0 || mVideoWidth * mVideoHeight == 0) {
            Log.e(TAG, "Invalid surface size");
            return;
        }

        // compute the aspect ratio
        double ar, vw;
        if (mSarDen == mSarNum) {
            /* No indication about the density, assuming 1:1 */
            vw = mVideoVisibleWidth;
            ar = (double) mVideoVisibleWidth / (double) mVideoVisibleHeight;
        } else {
            /* Use the specified aspect ratio */
            vw = mVideoVisibleWidth * (double) mSarNum / mSarDen;
            ar = vw / mVideoVisibleHeight;
        }

        // compute the display aspect ratio
        double dar = dw / dh;

        switch (mCurrentScreenState) {
        case SCREEN_AUTO:
            if (dar < ar)
                dh = dw / ar;
            else
                dw = dh * ar;
            break;
        case SCREEN_FULL:
            break;
        case SCREEN_16x9:
            ar = 16.0 / 9.0;
            if (dar < ar)
                dh = dw / ar;
            else
                dw = dh * ar;
            break;
        //        case SURFACE_4_3:
        //            ar = 4.0 / 3.0;
        //            if (dar < ar)
        //                dh = dw / ar;
        //            else
        //                dw = dh * ar;
        //            break;
        //        case SURFACE_ORIGINAL:
        //            dh = mVideoVisibleHeight;
        //            dw = vw;
        //            break;
        }

        if (mHolder != null) {
            mHolder.setFixedSize(mVideoWidth, mVideoHeight);
        }

        // set display size
        LayoutParams lp = mSurface.getLayoutParams();
        lp.width = (int) Math.ceil(dw * mVideoWidth / mVideoVisibleWidth);
        lp.height = (int) Math.ceil(dh * mVideoHeight / mVideoVisibleHeight);
        mSurface.setLayoutParams(lp);

        // set frame size (crop if necessary)
        lp = mSurfaceFrame.getLayoutParams();
        lp.width = (int) Math.floor(dw);
        lp.height = (int) Math.floor(dh);
        mSurfaceFrame.setLayoutParams(lp);

        mSurface.invalidate();
    }

    private void setSize1(int width, int height) {
        mVideoWidth = width;
        mVideoHeight = height;
        if (mVideoWidth * mVideoHeight <= 1) {
            return;
        }

        // get screen size
        int w = getWindow().getDecorView().getWidth();
        int h = getWindow().getDecorView().getHeight();

        // getWindow().getDecorView() doesn't always take orientation into
        // account, we have to correct the values
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (w > h && isPortrait || w < h && !isPortrait) {
            int i = w;
            w = h;
            h = i;
        }

        float videoAR = (float) mVideoWidth / (float) mVideoHeight;
        float screenAR = (float) w / (float) h;

        if (screenAR < videoAR)
            h = (int) (w / videoAR);
        else
            w = (int) (h * videoAR);

        // force surface buffer size
        if (mHolder != null) {
            mHolder.setFixedSize(mVideoWidth, mVideoHeight);
        }

        // set display size
        LayoutParams lp = mSurface.getLayoutParams();
        lp.width = w;
        lp.height = h;
        mSurface.setLayoutParams(lp);
        mSurface.invalidate();

        Log.d(TAG, "Set Video Surface View Width=" + w + ", Height=" + h
                + ", fix width=" + mVideoWidth + ", fix height=" + mVideoHeight);
    }

    @Override
    public void setSurfaceSize(int width, int height, int visible_width,
            int visible_height, int sar_num, int sar_den) {
        Log.d(TAG, "setSurfaceSize:" + width + " " + height + " "
                + visible_width + " " + visible_height + " " + sar_num + " "
                + sar_den);
        //        //        if (mCurrentScreenState == SCREEN_FULL) {
        //        width = 1280;
        //        height = 720;
        //        //        }
        if (width * height == 0)
            return;

        // store video size
        mVideoHeight = height;
        mVideoWidth = width;
        mVideoVisibleHeight = visible_height;
        mVideoVisibleWidth = visible_width;
        mSarNum = sar_num;
        mSarDen = sar_den;

        Message msg = Message.obtain(mHandler,
                VideoSizeChanged,
                mVideoWidth,
                mVideoHeight);
        msg.sendToTarget();
    }

    /*************
     * Player
     *************/

    private void createPlayer(String media) {
        releasePlayer();
        try {
            if (media.length() > 0) {
                Toast toast = Toast.makeText(this, media, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
                        0);
                toast.show();
            }

            // Create a new media player
            mLibvlc = LibVLC.getInstance();
            mLibvlc.setHardwareAcceleration(LibVLC.HW_ACCELERATION_DECODING);
            mLibvlc.setAout(LibVLC.AOUT_OPENSLES);
            mLibvlc.setTimeStretching(true);
            mLibvlc.setChroma("RV32");
            mLibvlc.setVerboseMode(false);
            // LibVLC.restart(this);
            mLibvlc.init(this);
            EventHandler.getInstance().addHandler(mHandler);
            mHolder.setFormat(PixelFormat.RGBX_8888);
            mHolder.setKeepScreenOn(true);
            MediaList list = mLibvlc.getMediaList();
            list.clear();
            list.add(new Media(mLibvlc, media));
            mLibvlc.playIndex(0);
        } catch (Exception e) {
            Toast.makeText(this, "Error creating player!", Toast.LENGTH_LONG)
                    .show();
            Log.d(TAG, "Create Player Exception:" + e.toString());
        }
    }

    private void releasePlayer() {
        if (mLibvlc == null)
            return;
        EventHandler.getInstance().removeHandler(mHandler);
        mLibvlc.stop();
        mLibvlc.detachSurface();
        mHolder = null;
        mLibvlc.closeAout();
        mLibvlc.destroy();
        mLibvlc = null;

        mVideoWidth = 0;
        mVideoHeight = 0;
    }

    /*************
     * Events
     *************/
    private Handler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<VideoActivity> mOwner;

        public MyHandler(VideoActivity owner) {
            mOwner = new WeakReference<VideoActivity>(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoActivity player = mOwner.get();

            // SamplePlayer events
            if (msg.what == VideoSizeChanged) {
                Log.d(TAG, "Video Size Changed:" + msg.arg1 + " " + msg.arg2);
                player.setSize(msg.arg1, msg.arg2);
                return;
            }

            // Libvlc events
            Bundle b = msg.getData();
            switch (b.getInt("event")) {
            case EventHandler.MediaPlayerEndReached:
                player.releasePlayer();
                break;
            case EventHandler.MediaPlayerPlaying:
            case EventHandler.MediaPlayerPaused:
            case EventHandler.MediaPlayerStopped:
            default:
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mTimeTickReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mTimeTickReceiver);
    }

    private final BroadcastReceiver mTimeTickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Time Tick:" + intent);
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                updateTime();
            }
        }
    };

    private void updateTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if (mTopPanelTime != null) {
            mTopPanelTime.setText(String.format("%02d:%02d", hour, minute));
        }
    }
}
