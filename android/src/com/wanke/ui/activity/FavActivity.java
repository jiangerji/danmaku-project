package com.wanke.ui.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;

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
    private View mBottomPanel;
    private View mSelectAll;
    private View mConfirmDelete;

    private int mBottomPanelHeight;
    private View mFooterView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_fav);

        mBottomPanelHeight = getResources().getDimensionPixelSize(R.dimen.bottom_panel_height);

        mFooterView = new View(this);
        mFooterView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                mBottomPanelHeight));

        mFavList = (PullToRefreshListView) findViewById(R.id.fav_list);
        mFavList.setMode(Mode.DISABLED);

        mFavAdapter = new FavAdapter();
        mFavList.setAdapter(mFavAdapter);

        mBottomPanel = findViewById(R.id.bottom_panel);

        mSelectAll = findViewById(R.id.select_all);
        mSelectAll.setOnClickListener(mSelectAllClickListener);
        mConfirmDelete = findViewById(R.id.confirm_delete);
        mConfirmDelete.setOnClickListener(mConfirmDeleteClickListener);

        getFav();
    }

    private boolean mIsSelectAll = false;

    private OnClickListener mSelectAllClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            mFavAdapter.setSelectAll(!mIsSelectAll);
            mIsSelectAll = !mIsSelectAll;
        }
    };

    private OnClickListener mConfirmDeleteClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mFavAdapter.confirmDelete(new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    dismissWaitingDialog();
                }

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    dismissWaitingDialog();
                }
            })) {
                showWaitingDialog();
            }
        }
    };

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
                if (mInDeleteMode) {
                    showBottomPanel();
                } else {
                    hideBottomPanel();
                }
                return true;
            }
        });
        return true;
    }

    private void showBottomPanel() {
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.slide_in_from_bottom);
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBottomPanel.setVisibility(View.VISIBLE);
            }
        });
        mBottomPanel.startAnimation(animation);
        mFavList.getRefreshableView().addFooterView(mFooterView);
    }

    private void hideBottomPanel() {
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.slide_out_to_bottom);
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBottomPanel.setVisibility(View.INVISIBLE);
            }
        });
        mBottomPanel.startAnimation(animation);
        mFavList.getRefreshableView().removeFooterView(mFooterView);
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
        }, null, 0);
    }
}
