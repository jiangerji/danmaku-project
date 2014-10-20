package com.wanke.ui.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wanke.model.GameInfo;
import com.wanke.network.http.Constants;
import com.wanke.tv.R;
import com.wanke.ui.UiUtils;

public class GameAdapter extends BaseAdapter {

    private ArrayList<GameInfo> mGameInfos = new ArrayList<GameInfo>();
    private DisplayImageOptions mOptions = UiUtils.getOptionsFadeIn();

    public void setGameInfos(ArrayList<GameInfo> gameInfos) {
        mGameInfos.clear();
        mGameInfos.addAll(gameInfos);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mGameInfos.size();
    }

    @Override
    public GameInfo getItem(int position) {
        return mGameInfos.get(position);
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
            convertView = inflater.inflate(R.layout.game_adapter_item, null);
            init = true;
        }

        final ImageView icon = (ImageView) convertView.findViewById(R.id.game_adapter_item_icon);
        TextView name = (TextView) convertView.findViewById(R.id.game_adapter_item_name);

        if (init) {
            icon.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

                        @SuppressWarnings("deprecation")
                        @Override
                        public void onGlobalLayout() {
                            int width = icon.getWidth();
                            if (width > 0) {
                                int height = 195 * width / 140;
                                LayoutParams layoutParams = icon.getLayoutParams();
                                layoutParams.height = height;
                                icon.setLayoutParams(layoutParams);

                                icon.getViewTreeObserver()
                                        .removeGlobalOnLayoutListener(this);
                            }
                        }
                    });
        }

        GameInfo gameInfo = getItem(position);
        name.setText(gameInfo.getGameName());

        ImageLoader.getInstance()
                .displayImage(Constants.buildImageUrl(gameInfo.getGameCover()),
                        icon,
                        mOptions);

        return convertView;
    }

}
