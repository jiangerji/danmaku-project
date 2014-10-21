package com.wanke.ui.adapter;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wanke.model.ChannelInfo;
import com.wanke.model.GameInfo;
import com.wanke.model.ParserUtil;
import com.wanke.network.http.CommonHttpUtils;
import com.wanke.network.http.HttpExceptionButFoundCache;
import com.wanke.tv.R;
import com.wanke.ui.activity.LiveChannelActivity;

public class RecommendAdapter extends BaseAdapter {
    private final static String TAG = "RecommendAdapter";

    private ArrayList<GameInfo> mGames = new ArrayList<GameInfo>();
    private Hashtable<Integer, ArrayList<ChannelInfo>> mChannelInfos = new Hashtable<Integer, ArrayList<ChannelInfo>>();
    private Hashtable<Integer, View> mRecommendViews = new Hashtable<Integer, View>();

    public void addGame(final GameInfo gameInfo) {
        mGames.add(gameInfo);
    }

    @Override
    public int getCount() {
        return mGames.size();
    }

    @Override
    public Object getItem(int position) {
        return mGames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final GameInfo gameInfo = mGames.get(position);

        convertView = mRecommendViews.get(gameInfo.getGameId());

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.recommend_adapter_item,
                    null);

            convertView.setTag(gameInfo);

            mRecommendViews.put(gameInfo.getGameId(), convertView);

            String gameGame = gameInfo.getGameName();
            TextView gameNameTV = (TextView) convertView.findViewById(R.id.game_title);
            gameNameTV.setText(gameGame);

            View moreBtn = convertView.findViewById(R.id.more_btn);
            moreBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(parent.getContext(),
                            LiveChannelActivity.class);
                    intent.putExtra(LiveChannelActivity.GAME_ID,
                            gameInfo.getGameId());
                    intent.putExtra(LiveChannelActivity.GAME_NAME,
                            gameInfo.getGameName());

                    parent.getContext().startActivity(intent);
                }
            });

            GridView gridView = (GridView) convertView.findViewById(R.id.recommend_games);
            gridView.setAdapter(new LiveChannelAdapter());

            // 获取推荐直播列表
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("gameId", "" + gameInfo.getGameId());
            params.addQueryStringParameter("offset", "" + 0);
            params.addQueryStringParameter("limit", "" + 4);

            CommonHttpUtils.get("recommend",
                    params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Log.d(TAG, "Exception:" + msg);
                            if (error instanceof HttpExceptionButFoundCache) {
                                parseResult(msg, gameInfo);
                            }
                        }

                        @Override
                        public void
                                onSuccess(ResponseInfo<String> responseInfo) {
                            Log.d(TAG, "onSuccess:" + responseInfo.statusCode);
                            if (responseInfo.statusCode == 200) {
                                parseResult(responseInfo.result, gameInfo);
                            }
                        }
                    }, "recommend:" + gameInfo.getGameId() + ":4");
        }

        return convertView;
    }

    private void parseResult(String content, GameInfo gameInfo) {
        try {
            JSONObject object = new JSONObject(content);
            JSONArray channels = object.getJSONArray("data");

            mChannelInfos.put(gameInfo.getGameId(),
                    ParserUtil.parseChannelsInfo(channels));
        } catch (Exception e) {
            Log.w(TAG,
                    "Parse channel result exception:"
                            + e.toString());
        }

        GridView gridView = (GridView) mRecommendViews.get(gameInfo.getGameId())
                .findViewById(R.id.recommend_games);
        LiveChannelAdapter adapter = (LiveChannelAdapter) gridView.getAdapter();
        adapter.setChannels(mChannelInfos.get(gameInfo.getGameId()));
        adapter.notifyDataSetChanged();
    }
}
