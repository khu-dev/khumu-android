// BaseFeedFragment를 상속받아 몇 가지 추가 기능을 수행한다.
// - Boards List
// - Article Create
package com.khumu.android.feed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.articleWrite.ArticleWriteActivity;
import com.khumu.android.data.Board;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SingleBoardFeedFragment extends BaseFeedFragment {
    private final static String TAG = "SingleBoardFeedFragment";
    @Inject
    public ArticleRepository articleRepository;

    private Board board;

    public SingleBoardFeedFragment(Board board){
        KhumuApplication.container.inject(this);
        this.board = board;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        KhumuApplication.container.inject(this);
        generateFeedViewModel();
    }

    @Override
    protected void generateFeedViewModel() {
        feedViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory(){
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new FeedViewModel(articleRepository, board);
            }
        }).get(FeedViewModel.class);
        feedViewModel.ListArticles();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // 아직 ViewModel은 안 다룸.
        // homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // 나의 부모인 컨테이너에서 내가 그리고자 하는 녀석을 얻어옴. 사실상 루트로 사용할 애를 객체와.
        // inflate란 xml => java 객체
        View root = inflater.inflate(R.layout.layout_feed, container, false);
        return root;
    }
}