package com.wanke.ui.activity;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.wanke.tv.R;

public class BaseActivity extends SherlockFragmentActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Used to put dark icons on light action bar
        //        boolean isLight = SampleList.THEME == R.style.Theme_Sherlock_Light;

        //Create the search view
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

        return true;
    }
}
