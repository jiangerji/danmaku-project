package com.wanke.ui.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wanke.model.ParserUtil;
import com.wanke.network.http.CommonHttpUtils;
import com.wanke.tv.R;
import com.wanke.ui.adapter.LiveChannelAdapter;

public class LiveChannelActivity extends BaseActivity {
    public static final String GAME_ID = "gameId";
    public static final String GAME_NAME = "gameName";

    private int mGameId = 0;
    private String mGameName = "";

    PullToRefreshGridView mLiveChannels;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Intent intent = getIntent();
        mGameId = intent.getIntExtra(GAME_ID, 0);

        if (mGameId == 0) {
            finish();
            return;
        }

        setContentView(R.layout.fragment_live);

        mGameName = intent.getStringExtra(GAME_NAME);

        setTitle(getResources().getString(R.string.live_channel_title,
                mGameName));

        Log.d(TAG, "live channel: game id=" + mGameId);
        Log.d(TAG, "live channel: game name=" + mGameName);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAdapter = new LiveChannelAdapter();
        mLiveChannels = (PullToRefreshGridView) findViewById(R.id.channel_list);
        mLiveChannels.setAdapter(mAdapter);

        mLiveChannels.setMode(Mode.DISABLED);
        mLiveChannels.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                getNextPage();
            }
        });

        getNextPage();
    }

    private int mCurrentPageIndex = 0;
    private LiveChannelAdapter mAdapter = null;
    private boolean mInGetNextPage = false;

    private void getNextPage() {
        if (mInGetNextPage) {
            return;
        }

        mInGetNextPage = true;

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("gameId", "" + mGameId);
        params.addQueryStringParameter("offset", "" + mCurrentPageIndex);
        params.addQueryStringParameter("limit", "20");

        CommonHttpUtils.get("recommend", params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.d(TAG, "Exception:" + msg);
                mInGetNextPage = false;
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.d(TAG, "onSuccess:" + responseInfo.statusCode);
                if (responseInfo.statusCode == 200) {
                    try {
                        JSONObject object = new JSONObject(responseInfo.result);
                        JSONArray channels = object.getJSONArray("data");

                        mAdapter.addChannels(ParserUtil.parseChannelsInfo(channels));
                        if (channels.length() >= 20) {
                            mCurrentPageIndex++;
                        }
                    } catch (Exception e) {
                        Log.w(TAG,
                                "Parse channel result exception:"
                                        + e.toString());
                    }
                }

                mInGetNextPage = false;
            }
        });
    }

}
