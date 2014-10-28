package com.wanke.ui.activity.my;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.wanke.db.dao.HistoryDao;
import com.wanke.model.HistoryInfo;
import com.wanke.tv.R;
import com.wanke.ui.activity.BaseActivity;
import com.wanke.ui.activity.LiveChannelDetailActivity;
import com.wanke.util.MyAsyncTask;

public class HistoryActivity extends BaseActivity {
    public static final String TAG = "HistoryActivity";

    private ListView mListView;
    private HistoryDao mDao;
    private List<HistoryInfo> mHistoryInfo;
    private HistoryAdapter mAdapter;

    // 设置每一页显示的最多的条目.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_history);
        mListView = (ListView) findViewById(R.id.my_history_listview);
        mDao = new HistoryDao(this);
        fillData();
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
                mHistoryInfo = mDao.findAll();
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add("delete");
        menuItem.setIcon(R.drawable.action_bar_delete_btn_bg)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                        | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dialog();
                return true;
            }
        });
        return true;
    }

    protected void dialog() {
        AlertDialog.Builder builder = new Builder(this);
        builder.setMessage(R.string.history_dialog_title);
        builder.setPositiveButton(R.string.history_dialog_bt_ok,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        mDao.deleteAll();
                        mHistoryInfo.clear();
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(R.string.history_dialog_bt_cancel,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
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
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                ViewGroup parent) {
            View view;
            ViewHolder holder;
            if (convertView != null) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(),
                        R.layout.my_history_list_item, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) view
                        .findViewById(R.id.history_list_item_owner_nickname);
                holder.tv_number = (TextView) view
                        .findViewById(R.id.history_list_item_fans);
                holder.tv_gamename = (TextView) view.findViewById(R.id.history_list_item_game_name);
                holder.tv_videoname = (TextView) view.findViewById(R.id.history_list_item_room_name);

                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewHolder holder = (ViewHolder) v.getTag();

                        HistoryInfo info = mHistoryInfo.get(holder.position);

                        Intent intent = new Intent();
                        intent.setClass(HistoryActivity.this,
                                LiveChannelDetailActivity.class);
                        intent.putExtra(LiveChannelDetailActivity.CHANNEL_ID,
                                Integer.valueOf(info.getRoomId()));
                        intent.putExtra(LiveChannelDetailActivity.CHANNEL_OWNER_NICKNAME,
                                info.getOwnerNickname());
                        startActivity(intent);
                    }
                });
            }

            holder.position = position;
            view.setTag(holder);

            HistoryInfo info = mHistoryInfo.get(position);
            String number = "" + info.getFans();
            holder.tv_number.setText(number);
            String name = info.getOwnerNickname();
            holder.tv_name.setText(name);
            String gamename = info.getGameName();
            holder.tv_gamename.setText(gamename);
            String videoname = info.getRoomName();
            holder.tv_videoname.setText(videoname);

            return view;
        }

    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_number;
        TextView tv_gamename;
        TextView tv_videoname;
        int position;
    }
}
