package com.wanke.ui.adapter;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wanke.model.ChannelInfo;
import com.wanke.model.GameInfo;
import com.wanke.model.ParserUtil;
import com.wanke.network.http.CommonHttpUtils;
import com.wanke.tv.R;

public class RecommendAdapter extends BaseAdapter {
    private final static String TAG = "recommend";

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
    public View getView(int position, View convertView, ViewGroup parent) {
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

            GridView gridView = (GridView) convertView.findViewById(R.id.recommend_games);
            gridView.setAdapter(new LiveChannelAdapter());

            // 获取推荐直播列表
            String actionUrl = "recommend?gameId=" + gameInfo.getGameId()
                    + "&offset=0&limit=4";
            CommonHttpUtils.get(actionUrl, new RequestCallBack<String>() {

                @Override
                public void onFailure(HttpException error, String msg) {
                    Log.d(TAG, "Exception:" + msg);
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.d(TAG, "onSuccess:" + responseInfo.statusCode);
                    if (responseInfo.statusCode == 200) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result);
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
            });
        }

        return convertView;
    }
}
