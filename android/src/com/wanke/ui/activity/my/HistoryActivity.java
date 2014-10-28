package com.wanke.ui.activity.my;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wanke.db.dao.HistoryDao;
import com.wanke.model.HistoryInfo;
import com.wanke.tv.R;
import com.wanke.ui.UiUtils;
import com.wanke.util.MyAsyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.wanke.db.dao.HistoryDao;
import com.wanke.model.HistoryInfo;
import com.wanke.tv.R;
import com.wanke.ui.UiUtils;
import com.wanke.ui.activity.BaseActivity;
import com.wanke.util.MyAsyncTask;

public class HistoryActivity extends BaseActivity {
    public static final String TAG = "HistoryActivity";
    private DisplayImageOptions mOptions = UiUtils.getOptionsFadeIn(250);

	private ListView mListView;
	private HistoryDao mDao;
	private List<HistoryInfo> mHistoryInfo;
	private HistoryAdapter mAdapter;
	// 设置每一页显示的最多的条目.
	private static final int mPageSize = 20;
	// 总的页码
	private int mTotalPage = 0;
	// 当前页 默认第一页
	private int mCurrentPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_my_history);
		mListView = (ListView) findViewById(R.id.my_history_listview);
		mDao = new HistoryDao(this);
		mTotalPage = mDao.getTotalPageNumber(mPageSize);
		fillData();
		// mHistoryInfo = mDao.findPartHistoryInfos(1, 1000);
	}

	private void fillData() {

		new MyAsyncTask() {
 
			@Override
			protected void onPreExectue() {
			}

			@Override
			protected void onPostExecute() {
				mAdapter = new HistoryAdapter();
				mListView.setAdapter(mAdapter);
			}

			@Override
			protected void doInbackgroud() {
				mHistoryInfo = mDao.findHistoryInfosByPage(mCurrentPage,
						mPageSize);
			}
		}.execute();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add("delate")
                .setIcon(R.drawable.history_delete)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                        | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case 0:
            dialog();
            break;
        case android.R.id.home:
            finish();
        default:
            break;
        }
        return true;
    }

    protected void dialog() {
        AlertDialog.Builder builder = new Builder(this);
        builder.setMessage(R.string.history_dialog_title);
        builder.setPositiveButton(R.string.history_dialog_bt_ok,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // TODO Auto-generated method stub
                        mDao.delateAll();
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(R.string.history_dialog_bt_cancel,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private class HistoryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mHistoryInfo.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

        @Override
        public View getView(final int position, View convertView,
                ViewGroup parent) {
            View view;
            ViewHolder holder;
            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(),
                        R.layout.my_history_list_item, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) view
                        .findViewById(R.id.my_history_list_item_name);
                holder.tv_number = (TextView) view
                        .findViewById(R.id.my_history_list_item_fans);
                holder.tv_gamename = (TextView) view.findViewById(R.id.my_history_list_item_gamename);
                holder.tv_videoname = (TextView) view.findViewById(R.id.my_history_list_item_videoname);
                view.setTag(holder);
            }
            HistoryInfo info = mHistoryInfo.get(position);
            String number = info.getNumber();
            holder.tv_number.setText(number);
            String name = info.getName();
            holder.tv_name.setText(name);
            String gamename = info.getGamename();
            holder.tv_gamename.setText(gamename);
            String videoname = info.getVideoname();
            holder.tv_videoname.setText(videoname);

            return view;
        }

    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_number;
        TextView tv_gamename;
        TextView tv_videoname;
    }
}
