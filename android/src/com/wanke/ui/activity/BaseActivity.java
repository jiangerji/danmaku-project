package com.wanke.ui.activity;

import android.app.Dialog;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.wanke.tv.R;
import com.wanke.ui.ToastUtil;
import com.wanke.ui.UiUtils;

public class BaseActivity extends SherlockFragmentActivity {
    protected final static String TAG = "activity";

    protected Dialog mWaitingDialog = null;

    protected void showWaitingDialog() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mWaitingDialog = UiUtils.showWaitingDialog(BaseActivity.this);
            }
        });
    }

    protected void dismissWaitingDialog() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    if (mWaitingDialog != null) {
                        mWaitingDialog.dismiss();
                    }
                } catch (Exception e) {
                }
            }
        });

    }

    protected void showToast(final String msg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                ToastUtil.showToast(BaseActivity.this, msg);
            }
        });
    }

    protected void showToast(final int resId) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                ToastUtil.showToast(BaseActivity.this, resId);
            }
        });
    }

    /**
     * 不显示搜索view
     */
    protected final static int FLAG_SEARCH_VIEW = 0x01;
    protected final static int FLAG_DISABLE_HOME_AS_UP = 0x02;

    protected int getFlag() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if ((getFlag() & FLAG_DISABLE_HOME_AS_UP) != FLAG_DISABLE_HOME_AS_UP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if ((getFlag() & FLAG_SEARCH_VIEW) == FLAG_SEARCH_VIEW) {
            SearchView searchView = new SearchView(this);
            searchView.setQueryHint("Search for Live Room...");
            //        searchView.setOnQueryTextListener(this);
            //        searchView.setOnSuggestionListener(this);

            //        if (mSuggestionsAdapter == null) {
            //            MatrixCursor cursor = new MatrixCursor(COLUMNS);
            //            cursor.addRow(new String[] { "1", "'Murica" });
            //            cursor.addRow(new String[] { "2", "Canada" });
            //            cursor.addRow(new String[] { "3", "Denmark" });
            //            mSuggestionsAdapter = new SuggestionsAdapter(getSupportActionBar().getThemedContext(),
            //                    cursor);
            //        }

            //        searchView.setSuggestionsAdapter(mSuggestionsAdapter);

            menu.add("Search")
                    .setIcon(R.drawable.action_bar_search_btn_bg)
                    .setActionView(searchView)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                            | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            break;

        default:
            break;
        }
        return true;
    }
}
