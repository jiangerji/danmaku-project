package com.wanke.ui.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wanke.db.dao.HistoryDao;
import com.wanke.network.http.CommonHttpUtils;
import com.wanke.network.http.Constants;
import com.wanke.network.http.HttpExceptionButFoundCache;
import com.wanke.tv.R;
import com.wanke.ui.ToastUtil;
import com.wanke.ui.UiUtils;
import com.wanke.util.PreferenceUtil;

public class LiveChannelDetailActivity extends BaseActivity {
    public final static String CHANNEL_ID = "channelId";
    public final static String CHANNEL_NAME = "channelName";
    public final static String CHANNEL_OWNER_NICKNAME = "channelOwnerNickname";
    public final static String CHANNEL_ONLINE = "channelOnline";
    public final static String CHANNEL_GAME_NAME = "gameName";

    private int mChannelId = 0;
    private String mChannelName = "";
    private String mChannelOwnerNickname = "";
    private int mChannelOwnerUid = 0;
    private int mChannelOnline = 0;
    private int mChannelFans = 0;
    private String mChannelDetail = "";
    private boolean mChannelSubscribed = false;
    private String mChannelCover = "";
    private String mChannelGameName;

    private HistoryDao mDao;

    private DisplayImageOptions mOptions = UiUtils.getOptionsFadeIn(100);
    private DisplayImageOptions mAvatarOptions = UiUtils.getOptionsRound((int) (4 * UiUtils.getDensity(null)));

    ImageView mSubscribeBtn;

    private View mShareBtn;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDao = new HistoryDao(this);
        setContentView(R.layout.activity_video_detail);

        Intent intent = getIntent();
        mChannelName = intent.getStringExtra(CHANNEL_NAME);
        mChannelOwnerNickname = intent.getStringExtra(CHANNEL_OWNER_NICKNAME);
        mChannelOnline = intent.getIntExtra(CHANNEL_ONLINE, 0);
        mChannelId = intent.getIntExtra(CHANNEL_ID, 0);
        mChannelGameName = intent.getStringExtra(CHANNEL_GAME_NAME);

        setTitle(mChannelName);

        View view = findViewById(R.id.video_play_btn);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                mDao.add(mChannelId,
                        mChannelName,
                        mChannelGameName,
                        mChannelOwnerNickname,
                        mChannelFans);
                intent.setClass(LiveChannelDetailActivity.this,
                        VideoActivity.class);
                intent.putExtra(VideoActivity.KEY_ROOM_TITLE, mChannelName);
                startActivity(intent);
            }
        });

        mSubscribeBtn = (ImageView) findViewById(R.id.subscribe_btn);
        mSubscribeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean logined = true;
                if (logined) {
                    if (mChannelSubscribed) {
                        unsubscribe();
                    } else {
                        subscribe();
                    }
                } else {
                    ToastUtil.showToastInCenter(LiveChannelDetailActivity.this,
                            R.string.subscribe_need_login);
                }
            }
        });

        //        initView();

        getChannelInfo();
    }

    private void initView() {
        Log.d(TAG, "Init View!");
        TextView ownerNickname = (TextView) findViewById(R.id.owner_nickname);
        ownerNickname.setText(mChannelOwnerNickname);

        TextView online = (TextView) findViewById(R.id.online);
        online.setText("在线：" + mChannelOnline);

        TextView channelTitle = (TextView) findViewById(R.id.room_title);
        channelTitle.setText(mChannelName);
        setTitle(mChannelName);

        TextView channelDetail = (TextView) findViewById(R.id.room_detail);
        channelDetail.setText(mChannelDetail);

        ImageView channelCover = (ImageView) findViewById(R.id.room_cover);
        if (!TextUtils.isEmpty(mChannelCover)) {
            ImageLoader.getInstance()
                    .displayImage(Constants.buildImageUrl(mChannelCover),
                            channelCover,
                            mOptions);
        }

        ImageView channelOwnerAvatar = (ImageView) findViewById(R.id.owner_avatar);
        if (mChannelOwnerUid > 0) {
            ImageLoader.getInstance()
                    .displayImage(Constants.buildImageUrl("" + mChannelOwnerUid
                            + ".png"),
                            channelOwnerAvatar,
                            mAvatarOptions);
        }

        mSubscribeBtn = (ImageView) findViewById(R.id.subscribe_btn);
        if (mChannelSubscribed) {
            mSubscribeBtn.setImageResource(R.drawable.detail_activity_subscribed_btn);
        } else {
            mSubscribeBtn.setImageResource(R.drawable.detail_activity_unsubscribed_btn);
        }

        mShareBtn = findViewById(R.id.share_btn);
        mShareBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }

    private void getChannelInfo() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", PreferenceUtil.getUid());
        params.addQueryStringParameter("roomId", "" + mChannelId);

        CommonHttpUtils.get("channel", params, new RequestCallBack<String>() {

            private void parseResult(String content) {
                try {
                    JSONObject object = new JSONObject(content);

                    mChannelOwnerNickname = object.optString("ownerNickname");
                    mChannelCover = object.optString("roomCover");
                    mChannelDetail = Html.fromHtml(object.optString("detail",
                            "")).toString();
                    mChannelName = object.optString("roomName");
                    mChannelOnline = object.optInt("online", -1);
                    mChannelId = object.optInt("roomId", -1);
                    mChannelFans = object.optInt("fans", -1);
                    mChannelOwnerUid = object.optInt("ownerUid", -1);
                    mChannelSubscribed = object.optBoolean("subscribed");
                    mChannelGameName = object.optString("gameName");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            initView();
                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG,
                            "Parse Channel Info Exception:" + e.toString());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.d(TAG, "Get Channel Info Exception:" + msg);
                if (error instanceof HttpExceptionButFoundCache) {
                    parseResult(msg);
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //                if (responseInfo.statusCode == 200) {
                parseResult(responseInfo.result);
                //                }
            }
        },
                null,
                0);
    }

    private void subscribe() {
        mSubscribeBtn.setEnabled(false);
        mSubscribeBtn.setImageResource(R.drawable.detail_activity_subscribed_btn);

        String uid = PreferenceUtil.getUid();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", "" + uid);
        params.addQueryStringParameter("roomId", "" + mChannelId);

        CommonHttpUtils.get("subscribe", params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                mSubscribeBtn.setEnabled(true);
                mSubscribeBtn.setImageResource(R.drawable.detail_activity_unsubscribed_btn);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                mSubscribeBtn.setEnabled(true);
                if (responseInfo.statusCode == 200) {
                    try {
                        JSONObject object = new JSONObject(responseInfo.result);
                        if (object.getInt("error") != 0) {
                            // 订阅失败
                            mSubscribeBtn.setImageResource(R.drawable.detail_activity_unsubscribed_btn);
                        } else {
                            mChannelSubscribed = true;
                        }
                    } catch (Exception e) {
                        mSubscribeBtn.setImageResource(R.drawable.detail_activity_unsubscribed_btn);
                    }
                }
            }
        },
                null,
                0);
    }

    private void unsubscribe() {
        mSubscribeBtn.setEnabled(false);
        mSubscribeBtn.setImageResource(R.drawable.detail_activity_unsubscribed_btn);

        String uid = PreferenceUtil.getUid();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", "" + uid);
        params.addQueryStringParameter("roomId", "" + mChannelId);

        CommonHttpUtils.get("unsubscribe",
                params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        mSubscribeBtn.setEnabled(true);
                        mSubscribeBtn.setImageResource(R.drawable.detail_activity_subscribed_btn);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        mSubscribeBtn.setEnabled(true);
                        if (responseInfo.statusCode == 200) {
                            try {
                                JSONObject object = new JSONObject(responseInfo.result);
                                if (object.getInt("error") != 0) {
                                    // 取消订阅失败
                                    mSubscribeBtn.setImageResource(R.drawable.detail_activity_subscribed_btn);
                                } else {
                                    mChannelSubscribed = false;
                                }
                            } catch (Exception e) {
                                mSubscribeBtn.setImageResource(R.drawable.detail_activity_subscribed_btn);
                            }
                        }
                    }
                },
                null,
                0);
    }
}
