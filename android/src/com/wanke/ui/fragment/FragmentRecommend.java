package com.wanke.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wanke.model.GameInfo;
import com.wanke.tv.R;
import com.wanke.ui.adapter.RecommendAdapter;
import com.wanke.ui.widget.RecommendAdsLayout;

public class FragmentRecommend extends Fragment {
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recommend,
                container,
                false);// 关联布局文件
        RecommendAdsLayout viewpagerLayout = new RecommendAdsLayout(getActivity());

        mListView = (ListView) view.findViewById(R.id.recommend_list);
        mListView.setDivider(null);
        mListView.addHeaderView(viewpagerLayout.getView());

        initRecommend();
        return view;
    }

    private RecommendAdapter mRecommendAdapter;

    private void initRecommend() {
        mRecommendAdapter = new RecommendAdapter();

        GameInfo yxlm = new GameInfo();
        yxlm.setGameId(1);
        yxlm.setGameName(getResources().getString(R.string.fragment_recommend_game_title_yxlm));
        mRecommendAdapter.addGame(yxlm);

        GameInfo lscs = new GameInfo();
        lscs.setGameId(2);
        lscs.setGameName(getResources().getString(R.string.fragment_recommend_game_title_lscs));
        mRecommendAdapter.addGame(lscs);

        GameInfo dota2 = new GameInfo();
        dota2.setGameId(3);
        dota2.setGameName(getResources().getString(R.string.fragment_recommend_game_title_dota2));
        mRecommendAdapter.addGame(dota2);

        GameInfo zjyx = new GameInfo();
        zjyx.setGameId(19);
        zjyx.setGameName(getResources().getString(R.string.fragment_recommend_game_title_zjyx));
        mRecommendAdapter.addGame(zjyx);

        mListView.setAdapter(mRecommendAdapter);
    }
}
