package com.khumu.android.myPage;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.KhumuApplication;
import com.khumu.android.articleWrite.ArticleWriteActivity;
import com.khumu.android.data.Board;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;
import com.khumu.android.R;
import com.khumu.android.data.Article;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MyPageFragment extends Fragment {
//    private final static String TAG = "MyPageFragment";
//    @Inject public BoardRepository boardRepository;
//    @Inject public ArticleRepository articleRepository;
//    private FeedViewModel feedViewModel;
//
//    private BoardAdapter boardAdapter;
//    private ArticleAdapter articleAdapter;
//
//    private RecyclerView articlesView;
//    private ListView boardsView;
//
//    private LinearLayoutManager linearLayoutManager;
//    private ImageView likeIcon;
//    private ImageView commentIcon;
//    private ImageView bookmarkIcon;
//
//    private ViewGroup toggleBoardsWrapperVG;
//    private BoardsToggler boardsToggler;
//
//    private ImageView articleWriteBTN;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
        KhumuApplication.container.inject(this);
//        feedViewModel = new ViewModelProvider(this, new FeedViewModelFactory(boardRepository, articleRepository)).get(FeedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // 아직 ViewModel은 안 다룸.
        // homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // 나의 부모인 컨테이너에서 내가 그리고자 하는 녀석을 얻어옴. 사실상 루트로 사용할 애를 객체와.
        // inflate란 xml => java 객체
        View root = inflater.inflate(R.layout.fragment_my_page, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //view는 root 즉 fragment

        // root.findViewById(R.id.action_navigation_board_to_navigation_home).setOnClickListener(Navigation);
        // xml 상에 recyclerview는 실질적으로 아이템이 어떻게 구현되어있는지 정의되어있지 않다.
        // linearlayout의 형태를 이용하겠다면 linearlayoutmanager을 이용한다.
//        findViews(view);
//        setAdapters();
//        setEventListeners(view);
    }
}