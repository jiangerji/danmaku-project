package com.wanke.ui.fragment;

import java.util.ArrayList;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wanke.model.ChannelInfo;
import com.wanke.model.ParserUtil;
import com.wanke.network.http.CommonHttpUtils;
import com.wanke.tv.R;
import com.wanke.ui.adapter.LiveChannelAdapter;

public class FragmentLive extends Fragment {
    private final static String TAG = "live";

    private PullToRefreshGridView mChannelList;
    private LiveChannelAdapter mAdapter;
    protected ImageLoader mImageLoader = ImageLoader.getInstance();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mChannelList = (PullToRefreshGridView) inflater.inflate(R.layout.fragment_live,
                container,
                false);
        //        mChannelList = (PullToRefreshGridView) view
        //                .findViewById(R.id.channel_list);
        mChannelList.setMode(Mode.PULL_FROM_END);

        mChannelList.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                if (mHasMore) {
                    getNextPage();
                }
            }
        });

        mChannelList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // TODO Auto-generated method stub
                //                Intent intent;
                //                intent = new Intent(getActivity(), VideoDetailPages.class);
                //                intent.putExtra("gameurl", "rtmp://192.168.41.234/live/hello");
                //                startActivity(intent);
            }

        });

        mAdapter = new LiveChannelAdapter();
        mChannelList.setAdapter(mAdapter);

        getNextPage();
        return mChannelList;
    }

    private int mCurrentPage = 0;
    private boolean mHasMore = true;
    private boolean mInGetNextPage = false;

    private void getNextPage() {
        if (mInGetNextPage) {
            // 正在进行更新
            return;
        }

        mInGetNextPage = true;

        final int thisPage = mCurrentPage;
        String action = "recommend?offset=" + mCurrentPage + "&limit=20";
        CommonHttpUtils.get(action, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo.statusCode == 200) {
                    try {
                        JSONObject object = new JSONObject(responseInfo.result);
                        ArrayList<ChannelInfo> channelInfos = ParserUtil.parseChannelsInfo(object.getJSONArray("data"));
                        if (channelInfos.size() >= 20) {
                            mCurrentPage++;
                        } else {
                            mHasMore = false;
                        }

                        mAdapter.addChannels(channelInfos);
                    } catch (Exception e) {
                        Log.d(TAG, "Get Next Page Exception:" + e.toString());
                    }

                    mInGetNextPage = false;
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                if (thisPage == 0) {
                    // 初始化
                }

                mInGetNextPage = false;
            }
        });
    }
}
