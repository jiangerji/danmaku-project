package com.wanke.ui.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wanke.model.ChannelInfo;
import com.wanke.network.http.Constants;
import com.wanke.tv.R;
import com.wanke.ui.UiUtils;
import com.wanke.ui.activity.LiveChannelDetailActivity;

public class LiveChannelAdapter extends BaseAdapter {

    private DisplayImageOptions mOptions = UiUtils.getOptionsFadeIn();

    private ArrayList<ChannelInfo> mChannelInfos = new ArrayList<ChannelInfo>();

    public void setChannels(ArrayList<ChannelInfo> channelInfos) {
        mChannelInfos.clear();
        mChannelInfos.addAll(channelInfos);

        notifyDataSetChanged();
    }

    public void addChannels(ArrayList<ChannelInfo> channelInfos) {
        mChannelInfos.addAll(channelInfos);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mChannelInfos.size();
    }

    @Override
    public ChannelInfo getItem(int position) {
        return mChannelInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        boolean init = false;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.live_channel_item,
                    null);

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ChannelInfo channelInfo = (ChannelInfo) v.getTag();

                    Intent intent = new Intent();
                    intent.setClass(v.getContext(),
                            LiveChannelDetailActivity.class);
                    intent.putExtra(LiveChannelDetailActivity.CHANNEL_ID,
                            channelInfo.getRoomId());
                    v.getContext().startActivity(intent);
                }
            });

            init = true;
        }

        convertView.setTag(getItem(position));

        ImageView liveChannelCover = (ImageView) convertView.findViewById(R.id.live_channel_cover);
        if (init) {
            int height = UiUtils.getScreenWidth(null) / 2 * 180 / 320;
            LayoutParams layoutParams = (LayoutParams) liveChannelCover.getLayoutParams();
            layoutParams.height = height;
            liveChannelCover.setLayoutParams(layoutParams);
        }

        ChannelInfo info = mChannelInfos.get(position);
        if (info != null) {
            TextView gameName = (TextView) convertView.findViewById(R.id.game_name);
            gameName.setText(info.getGameName());

            TextView channelTitle = (TextView) convertView.findViewById(R.id.channel_title);
            channelTitle.setText(info.getRoomName());

            TextView channelOwner = (TextView) convertView.findViewById(R.id.channel_owner);
            channelOwner.setText(info.getOwnerNickName());

            TextView online = (TextView) convertView.findViewById(R.id.online);
            online.setText("" + info.getOnline());

            ImageLoader.getInstance()
                    .displayImage(Constants.buildImageUrl(info.getRoomCover()),
                            liveChannelCover,
                            mOptions);
        }

        return convertView;
    }
}
