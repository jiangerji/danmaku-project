package com.wanke.ui.fragment;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wanke.model.ParserUtil;
import com.wanke.network.http.CommonHttpUtils;
import com.wanke.tv.R;
import com.wanke.ui.adapter.GameAdapter;

public class FragmentGame extends Fragment {

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

        init();
        return mPullRefreshGridView;
    }

    private RequestCallBack<String> mCallBack = new RequestCallBack<String>() {

        @Override
        public void onFailure(HttpException arg0, String arg1) {

        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            if (responseInfo.statusCode == 200) {
                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    mAdapter.setGameInfos(ParserUtil.parseGamesInfo(object.getJSONArray("data")));
                } catch (Exception e) {
                }

            }
        }
    };

    private void init() {
        String action = "games";
        CommonHttpUtils.get(action, mCallBack);
    }
}
