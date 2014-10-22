package com.wanke.ui.fragment;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wanke.model.GameInfo;
import com.wanke.model.ParserUtil;
import com.wanke.network.http.CommonHttpUtils;
import com.wanke.network.http.HttpExceptionButFoundCache;
import com.wanke.tv.R;
import com.wanke.ui.activity.LiveChannelActivity;
import com.wanke.ui.adapter.GameAdapter;

public class FragmentGame extends BaseFragment {

    private PullToRefreshGridView mPullRefreshGridView;
    private GameAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mPullRefreshGridView = (PullToRefreshGridView) inflater.inflate(R.layout.fragment_game,
                container,
                false);
        mPullRefreshGridView.setMode(Mode.DISABLED);

        mAdapter = new GameAdapter();
        mPullRefreshGridView.setAdapter(mAdapter);
        mPullRefreshGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id) {
                GameInfo gameInfo = mAdapter.getItem(position);
                Intent intent = new Intent();
                intent.setClass(parent.getContext(),
                        LiveChannelActivity.class);
                intent.putExtra(LiveChannelActivity.GAME_ID,
                        gameInfo.getGameId());
                intent.putExtra(LiveChannelActivity.GAME_NAME,
                        gameInfo.getGameName());

                getActivity().startActivity(intent);
            }
        });

        init();

        Log.d(TAG, "FragmentGame: onCreateView");
        return mPullRefreshGridView;
    }

    private RequestCallBack<String> mCallBack = new RequestCallBack<String>() {

        @Override
        public void onFailure(HttpException error, String msg) {
            if (error instanceof HttpExceptionButFoundCache) {
                parseResult(msg);
            }
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            //            if (responseInfo.statusCode == 200) {
            //                try {
            //                    JSONObject object = new JSONObject(responseInfo.result);
            //                    mAdapter.setGameInfos(ParserUtil.parseGamesInfo(object.getJSONArray("data")));
            //                } catch (Exception e) {
            //                }
            parseResult(responseInfo.result);
            //            }
        }
    };

    private void parseResult(String content) {
        try {
            JSONObject object = new JSONObject(content);
            mAdapter.setGameInfos(ParserUtil.parseGamesInfo(object.getJSONArray("data")));
        } catch (Exception e) {
        }
    }

    private void init() {
        String action = "games";
        CommonHttpUtils.get(action, null, mCallBack, "games");
    }
}
