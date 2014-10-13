package com.wanke.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class TestActivity extends Activity {
    private final static String TAG = "Test";

    private void parseRecommand(String msg) {
        try {
            JSONObject all = new JSONObject(msg);

            // 解析热门直播频道
            Log.d(TAG, "Hot");
            JSONArray hots = all.getJSONArray("hot");
            JSONObject hot = null;
            String roomId;
            String title;
            String anchorName;
            String gameId;
            String gameName;
            int audienceCount;
            for (int i = 0; i < hots.length(); i++) {
                hot = hots.getJSONObject(i);
                roomId = hot.getString("roomId");
                title = hot.getString("title");
                anchorName = hot.getString("anchorName");
                gameId = hot.getString("gameId");
                gameName = hot.getString("gameName");
                audienceCount = hot.getInt("audience");

                Log.d(TAG, "  room id:" + roomId);
                Log.d(TAG, "  room title:" + title);
                Log.d(TAG, "  anchor name:" + anchorName);
                Log.d(TAG, "  game id:" + gameId);
                Log.d(TAG, "  game name:" + gameName);
                Log.d(TAG, "  audience count:" + audienceCount);
                Log.d(TAG, "  ========================");
            }

            // 解析热门游戏推荐直播列表
            JSONArray games = all.getJSONArray("games");
            JSONObject game;
            JSONArray channels;
            JSONObject channel;
            for (int i = 0; i < games.length(); i++) {
                game = games.getJSONObject(i);
                gameId = game.getString("gameId");
                gameName = game.getString("gameName");
                Log.d(TAG, "================================");
                Log.d(TAG, "  " + gameName + " 推荐列表");
                channels = game.getJSONArray("channels");
                parseChannels(channels);
                //                for (int j = 0; j < channels.length(); j++) {
                //                    channel = channels.getJSONObject(j);
                //                    roomId = channel.getString("roomId");
                //                    title = channel.getString("title");
                //                    anchorName = channel.getString("anchorName");
                //                    gameId = channel.getString("gameId");
                //                    gameName = channel.getString("gameName");
                //                    audienceCount = channel.getInt("audience");
                //
                //                    Log.d(TAG, "    room id:" + roomId);
                //                    Log.d(TAG, "    room title:" + title);
                //                    Log.d(TAG, "    anchor name:" + anchorName);
                //                    Log.d(TAG, "    game id:" + gameId);
                //                    Log.d(TAG, "    game name:" + gameName);
                //                    Log.d(TAG, "    audience count:" + audienceCount);
                //                    Log.d(TAG, "");
                //                }

            }

        } catch (Exception e) {
            Log.d(TAG, "Parse Recommand Exception:" + e.toString());
        }
    }

    private void getRecommend() {
        String url = "http://192.168.41.101/server-data/recommend.txt";
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {
                    @Override
                    public void onLoading(
                            long total, long current, boolean isUploading) {
                        Log.d(TAG, "onLoading:" + total + " " + current + " "
                                + isUploading);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        parseRecommand(responseInfo.result);
                    }

                    @Override
                    public void onStart() {
                        Log.d(TAG, "onStart");
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.d(TAG, "onFailure:" + error.toString() + " " + msg);
                    }
                });
    }

    private void parseAds(String msg) {
        try {
            JSONObject all = new JSONObject(msg);
            JSONArray ads = all.getJSONArray("ads");
            JSONObject ad;
            String id;
            String title;
            String type;
            Log.d(TAG, "  ");
            Log.d(TAG, "Ads");
            for (int i = 0; i < ads.length(); i++) {
                ad = ads.getJSONObject(i);
                id = ad.getString("id");
                title = ad.getString("title");
                type = ad.getString("type");

                Log.d(TAG, "  id=" + id);
                Log.d(TAG, "  title=" + title);
                Log.d(TAG, "  type=" + type);
                Log.d(TAG, "  ");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void getAds() {
        String url = "http://192.168.41.101/server-data/ads.txt";
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {
                    @Override
                    public void onLoading(
                            long total, long current, boolean isUploading) {
                        Log.d(TAG, "onLoading:" + total + " " + current + " "
                                + isUploading);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        parseAds(responseInfo.result);
                    }

                    @Override
                    public void onStart() {
                        Log.d(TAG, "onStart");
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.d(TAG, "onFailure:" + error.toString() + " " + msg);
                    }
                });
    }

    private void parseChannels(JSONArray channels) {
        JSONObject channel;
        String roomId;
        String title;
        String anchorName;
        String gameId;
        String gameName;
        int audienceCount;
        for (int j = 0; j < channels.length(); j++) {
            try {
                channel = channels.getJSONObject(j);
                roomId = channel.getString("roomId");
                title = channel.getString("title");
                anchorName = channel.getString("anchorName");
                gameId = channel.getString("gameId");
                gameName = channel.getString("gameName");
                audienceCount = channel.getInt("audience");

                Log.d(TAG, "    room id:" + roomId);
                Log.d(TAG, "    room title:" + title);
                Log.d(TAG, "    anchor name:" + anchorName);
                Log.d(TAG, "    game id:" + gameId);
                Log.d(TAG, "    game name:" + gameName);
                Log.d(TAG, "    audience count:" + audienceCount);
                Log.d(TAG, "    ");
            } catch (Exception e) {

            }
        }
    }

    private void parseLives(String msg) {
        try {
            Log.d(TAG, "====All Lives====");
            JSONObject all = new JSONObject(msg);
            parseChannels(all.getJSONArray("channels"));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void getLives() {
        String url = "http://192.168.41.101/server-data/lives";
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {
                    @Override
                    public void onLoading(
                            long total, long current, boolean isUploading) {
                        Log.d(TAG, "onLoading:" + total + " " + current + " "
                                + isUploading);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        parseLives(responseInfo.result);
                    }

                    @Override
                    public void onStart() {
                        Log.d(TAG, "onStart");
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.d(TAG, "onFailure:" + error.toString() + " " + msg);
                    }
                });
    }

    private void parseGames(String msg) {
        try {
            JSONObject all = new JSONObject(msg);
            JSONArray games = all.getJSONArray("games");
            JSONObject game;
            String gameId;
            String gameName;
            Log.d(TAG, "====Game List====");
            for (int i = 0; i < games.length(); i++) {
                game = games.getJSONObject(i);

                gameId = game.getString("gameId");
                gameName = game.getString("gameName");

                Log.d(TAG, "  Game Id:" + gameId);
                Log.d(TAG, "  Game Name:" + gameName);
                Log.d(TAG, "    ");
            }
        } catch (Exception e) {
        }
    }

    // 获取所有游戏列表数据，之后需要实现为分页获取
    private void getGames() {
        String url = "http://192.168.41.101/server-data/games";
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {
                    @Override
                    public void onLoading(
                            long total, long current, boolean isUploading) {
                        Log.d(TAG, "onLoading:" + total + " " + current + " "
                                + isUploading);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        parseGames(responseInfo.result);
                    }

                    @Override
                    public void onStart() {
                        Log.d(TAG, "onStart");
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.d(TAG, "onFailure:" + error.toString() + " " + msg);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        getRecommend();
        //        getAds();
        //        getLives();
        getGames();
    }

}
