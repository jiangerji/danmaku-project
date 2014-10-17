package com.wanke.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class ParserUtil {
    private final static String TAG = "parse";

    public static ArrayList<ChannelInfo> parseChannelsInfo(JSONArray channels) {
        ArrayList<ChannelInfo> channelInfos = new ArrayList<ChannelInfo>();
        JSONObject channel;
        try {

            for (int i = 0; i < channels.length(); i++) {
                channel = channels.getJSONObject(i);

                ChannelInfo channelInfo = new ChannelInfo();
                channelInfo.setRoomId(channel.getInt("roomId"));
                channelInfo.setRoomName(channel.getString("roomName"));
                channelInfo.setRoomCover(channel.getString("roomCover"));
                channelInfo.setGameId(channel.getInt("gameId"));
                channelInfo.setGameName(channel.getString("gameName"));
                channelInfo.setOwnerNickName(channel.getString("ownerNickname"));
                channelInfo.setFans(channel.getInt("fans"));
                channelInfo.setOnline(channel.getInt("online"));

                channelInfos.add(channelInfo);

                Log.d(TAG, "Channel Info:" + channelInfo.toString());
            }
        } catch (Exception e) {
        }

        return channelInfos;
    }
}
