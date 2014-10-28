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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wanke.network.http.CommonHttpUtils;
import com.wanke.network.http.Constants;
import com.wanke.tv.R;
import com.wanke.ui.UiUtils;
import com.wanke.ui.activity.LiveChannelDetailActivity;
import com.wanke.util.PreferenceUtil;

public class FavAdapter extends BaseAdapter {

    private ArrayList<FavInfo> mFavInfos = new ArrayList<FavInfo>();

    private ArrayList<Boolean> mSelected = new ArrayList<Boolean>();

    public void setFavInfos(ArrayList<FavInfo> favInfos) {
        mFavInfos.clear();
        mFavInfos.addAll(favInfos);

        mSelected = new ArrayList<Boolean>();
        for (int i = 0; i < mFavInfos.size(); i++) {
            mSelected.add(false);
        }

        notifyDataSetChanged();
    }

    private boolean mEnableMultiChoiceMode = false;

    public void setMultiChoiceMode(boolean enable) {
        mEnableMultiChoiceMode = enable;
        notifyDataSetChanged();
    }

    private boolean mIsSelectAll = false;
    private int mSelectedCount = 0;

    public void setSelectAll(boolean state) {
        if (mIsSelectAll != state) {
            mIsSelectAll = state;
            for (int i = 0; i < mSelected.size(); i++) {
                mSelected.set(i, state);
            }

            if (mIsSelectAll) {
                mSelectedCount = getCount();
            } else {
                mSelectedCount = 0;
            }

            notifyDataSetChanged();
        }
    }

    public boolean confirmDelete(final RequestCallBack<String> callBack) {
        if (mSelectedCount > 0) {
            final ArrayList<FavInfo> tempInfos = new ArrayList<FavInfo>();
            ArrayList<FavInfo> deleteInfos = new ArrayList<FavInfo>();

            for (int i = 0; i < getCount(); i++) {
                if (!mSelected.get(i)) {
                    tempInfos.add(getItem(i));
                } else {
                    deleteInfos.add(getItem(i));
                }
            }

            StringBuilder sb = new StringBuilder();
            for (FavInfo info : deleteInfos) {
                sb.append(info.getRoomId() + ":");
            }

            RequestParams params = new RequestParams();
            params.addQueryStringParameter("uid", PreferenceUtil.getUid());
            params.addQueryStringParameter("roomIds", sb.toString());

            String action = "unsubscribe";
            CommonHttpUtils.get(action, params, new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    setFavInfos(tempInfos);
                    if (callBack != null) {
                        callBack.onSuccess(responseInfo);
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    if (callBack != null) {
                        callBack.onFailure(error, msg);
                    }
                }
            }, null, 0);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getCount() {
        return mFavInfos.size();
    }

    @Override
    public FavInfo getItem(int position) {
        return mFavInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FavInfo info = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fav_adapter_item, null);
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mEnableMultiChoiceMode) {
                        mSelected.set(position, !mSelected.get(position));
                        CheckBox selected = (CheckBox) v.findViewById(R.id.select);
                        selected.setChecked(mSelected.get(position));

                        if (mSelected.get(position)) {
                            mSelectedCount++;
                        } else {
                            mSelectedCount--;
                        }

                    } else {
                        Intent intent = new Intent();
                        intent.setClass(v.getContext(),
                                LiveChannelDetailActivity.class);
                        intent.putExtra(LiveChannelDetailActivity.CHANNEL_ID,
                                Integer.valueOf(info.getRoomId()));
                        intent.putExtra(LiveChannelDetailActivity.CHANNEL_OWNER_NICKNAME,
                                info.getNickname());
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }

        convertView.setTag(position);

        ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
        TextView username = (TextView) convertView.findViewById(R.id.username);
        TextView fans = (TextView) convertView.findViewById(R.id.fans);
        CheckBox selected = (CheckBox) convertView.findViewById(R.id.select);

        if (info != null) {
            ImageLoader.getInstance()
                    .displayImage(Constants.buildImageUrl(info.getAvatar()),
                            avatar,
                            UiUtils.getOptionCircle());

            username.setText(info.getNickname());
            fans.setText(parent.getContext()
                    .getResources()
                    .getString(R.string.information_activity_fans,
                            info.getFans()));
        }

        if (mEnableMultiChoiceMode) {
            selected.setVisibility(View.VISIBLE);
            selected.setChecked(mSelected.get(position));
        } else {
            selected.setVisibility(View.GONE);
        }

        return convertView;
    }

    public static class FavInfo {
        private String roomId;
        private String uid;
        private String avatar;
        private String nickname;
        private int fans;

        /**
         * @return the roomId
         */
        public String getRoomId() {
            return roomId;
        }

        /**
         * @param roomId
         *            the roomId to set
         */
        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        /**
         * @return the uid
         */
        public String getUid() {
            return uid;
        }

        /**
         * @param uid
         *            the uid to set
         */
        public void setUid(String uid) {
            this.uid = uid;
        }

        /**
         * @return the userAvatar
         */
        public String getAvatar() {
            return avatar;
        }

        /**
         * @param userAvatar
         *            the userAvatar to set
         */
        public void setAvatar(String userAvatar) {
            this.avatar = userAvatar;
        }

        /**
         * @return the nickname
         */
        public String getNickname() {
            return nickname;
        }

        /**
         * @param nickname
         *            the nickname to set
         */
        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        /**
         * @return the fans
         */
        public int getFans() {
            return fans;
        }

        /**
         * @param fans
         *            the fans to set
         */
        public void setFans(int fans) {
            this.fans = fans;
        }
    }
}
