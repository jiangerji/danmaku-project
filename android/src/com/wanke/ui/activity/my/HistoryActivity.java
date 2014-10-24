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
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HistoryActivity extends Activity {
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
				holder.iv_photo = (ImageView) view
						.findViewById(R.id.my_history_list_item_photo);
				view.setTag(holder);
			}
			HistoryInfo info = mHistoryInfo.get(position);
			String number = info.getNumber();
			holder.tv_number.setText(number);
			String name = info.getName();
			holder.tv_name.setText(name);
			String path = info.getPath();
			ImageLoader.getInstance().displayImage(path, holder.iv_photo,
					mOptions);
			return view;
		}

	}

	static class ViewHolder {
		TextView tv_name;
		TextView tv_number;
		ImageView iv_photo;
	}
}
