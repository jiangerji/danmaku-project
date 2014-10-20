package com.wanke.ui.activity;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.wanke.tv.R;

public class BaseActivity extends SherlockFragmentActivity {
    protected final static String TAG = "activity";

    /**
     * 不显示搜索view
     */
    protected final static int FLAG_NO_SEARCH_VIEW = 0x01;

    protected int getFlag() {
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if ((getFlag() & FLAG_NO_SEARCH_VIEW) != FLAG_NO_SEARCH_VIEW) {
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
                    .setIcon(R.drawable.abs__ic_search)
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
