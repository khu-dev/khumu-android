package com.khumu.android.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.feed.ArticleAdapter;
import com.khumu.android.data.Article;
import com.khumu.android.repository.BoardRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class HomeFragment extends Fragment {
    @Inject
    public BoardRepository br;
    private RecentArticleAdapter recentArticleAdapter;
    private ListView recentArticlesListView;
    private HomeViewModel homeViewModel;
    private Toolbar toolbar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        KhumuApplication.container.inject(this);
        super.onCreate(savedInstanceState);

        homeViewModel = new ViewModelProvider(this, new HomeViewModelFactory(br)).get(HomeViewModel.class);

        recentArticleAdapter = new RecentArticleAdapter(
                getContext(),
                R.layout.layout_home_recent_article_item,
                new ArrayList<Article>());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 나의 부모인 컨테이너에서 내가 그리고자 하는 녀석을 얻어옴. 사실상 루트로 사용할 애를 객체와.
        // inflate란 xml => java 객체
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        recentArticlesListView = view.findViewById(R.id.recent_articles_list);
        recentArticlesListView.setAdapter(recentArticleAdapter);
        homeViewModel.getLiveDataRecentArticles().observe(getViewLifecycleOwner(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                for(Article a : articles){

                    recentArticleAdapter.add(a);
                }
                recentArticleAdapter.notifyDataSetChanged();
            }
        });
    }
}