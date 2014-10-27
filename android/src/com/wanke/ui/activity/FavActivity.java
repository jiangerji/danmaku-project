package com.wanke.ui.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wanke.network.http.CommonHttpUtils;
import com.wanke.tv.R;
import com.wanke.ui.adapter.FavAdapter;
import com.wanke.ui.adapter.FavAdapter.FavInfo;
import com.wanke.util.PreferenceUtil;

public class FavActivity extends BaseActivity {

    private PullToRefreshListView mFavList;
    private FavAdapter mFavAdapter;
    private MenuItem mDeleteMenuItem;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_fav);

        mFavList = (PullToRefreshListView) findViewById(R.id.fav_list);
        mFavList.setMode(Mode.DISABLED);

        mFavAdapter = new FavAdapter();
        mFavList.setAdapter(mFavAdapter);

        getFav();
    }

    private boolean mInDeleteMode = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mDeleteMenuItem = menu.add(R.string.delete)
                .setIcon(R.drawable.action_bar_delete_btn_bg)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mDeleteMenuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mFavAdapter.setMultiChoiceMode(!mInDeleteMode);
                mInDeleteMode = !mInDeleteMode;
                return true;
            }
        });
        return true;
    }

    private void getFav() {
        String uid = PreferenceUtil.getUid();
        if (TextUtils.isEmpty(uid)) {
            finish();
            return;
        }

        showWaitingDialog();

        String action = "fav";
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", uid);

        CommonHttpUtils.get(action, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dismissWaitingDialog();

                ArrayList<FavInfo> favArray = new ArrayList<FavInfo>();

                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    int error = object.getInt("error");
                    if (error != 0) {
                        showToast(object.getString("msg"));
                    } else {
                        JSONArray favs = object.getJSONArray("data");
                        JSONObject fav = null;
                        for (int i = 0; i < favs.length(); i++) {
                            fav = favs.getJSONObject(i);

                            FavInfo favInfo = new FavInfo();
                            favInfo.setUid("" + fav.getInt("uid"));
                            favInfo.setAvatar(fav.getString("avatar"));
                            favInfo.setRoomId(fav.getString("roomId"));
                            favInfo.setFans(fav.getInt("fans"));
                            favInfo.setNickname(fav.getString("username"));
                            favArray.add(favInfo);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mFavAdapter.setFavInfos(favArray);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dismissWaitingDialog();
                showToast(msg);
            }
        }, null, 10000);
    }
}
