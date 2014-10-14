package com.wanke.ui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HotDamankuAdapter extends BaseAdapter {

    private ArrayList<String> mHotDanmakus = new ArrayList<String>();

    private Context mContext;

    public HotDamankuAdapter(Context context) {
        mContext = context;
    }

    public void add(String danmaku) {
        mHotDanmakus.add(danmaku);
    }

    @Override
    public int getCount() {
        return mHotDanmakus.size();
    }

    @Override
    public Object getItem(int position) {
        return mHotDanmakus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new TextView(mContext);
        }

        TextView textView = (TextView) convertView;

        textView.setPadding(10, 10, 0, 10);
        textView.setSingleLine(true);
        textView.setEllipsize(TruncateAt.END);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textView.setTextSize(16);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(Color.TRANSPARENT);
        textView.setText(mHotDanmakus.get(position));

        return textView;
    }

}
