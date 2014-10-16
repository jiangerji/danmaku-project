package com.wanke.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.wanke.tv.R;
import com.wanke.ui.adapter.HotDamankuAdapter;
import com.wanke.ui.widget.ViewpagerLayout;

public class Recommend extends Fragment {
    private ListView mListView;
    private BaseAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recommend,
                container,
                false);// 关联布局文件
        ViewpagerLayout viewpagerLayout = new ViewpagerLayout(getActivity());

        mListView = (ListView) view.findViewById(R.id.recommend_list);
        mListView.setDivider(null);
        //		mAdapter = new List_Gallery_adapter(listinfo, getActivity());
        //		mListView.addHeaderView(viewpagerLayout);
        //		mListView.setAdapter(mAdapter);
        //		mListView.setOnItemClickListener(new OnItemClickListener() {
        //
        //			@Override
        //			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        //					long arg3) {
        //				// TODO Auto-generated method stub
        //				Intent intent;
        //				intent = new Intent(getActivity(), VideoDetailPages.class);
        //				getActivity().startActivity(intent);
        //			}
        //
        //		});
        mListView.addHeaderView(viewpagerLayout.getView());

        HotDamankuAdapter adapter = new HotDamankuAdapter(getActivity());
        adapter.add("fadfa");
        mListView.setAdapter(adapter);
        return view;
    }

}
