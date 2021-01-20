/**
 * BaseFeedFragment를 상속받아 몇 가지 추가 기능을 수행한다.
 *
 * 1. fragment_feed.xml이 아닌 fragment_tab_feed.xml이용
 *    fragment_feed.xml을 include하는 fragment_tab_feed.xml을 바탕으로 inflate
 *
 * 2. ListBoards
 *    Tab layout이 추가되면서 한 Board에 대한 Article을 불러오는 것 뿐만 아니라 Board의 List도 필요하기에
 *
 * 3. ArticleCreate
 *    Article을 생성하는 버튼을 통해 ArticleWriteActivity를 띄움.
 */
package com.khumu.android.feed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.khumu.android.KhumuApplication;
import com.khumu.android.articleWrite.ArticleWriteActivity;
import com.khumu.android.data.Board;
import com.khumu.android.databinding.FragmentMyPageBinding;
import com.khumu.android.databinding.FragmentTabFeedBinding;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;
import com.khumu.android.R;
import com.khumu.android.data.Article;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TabFeedFragment extends BaseFeedFragment {
    private final static String TAG = "TabFeedFragment";
    @Inject public BoardRepository boardRepository;

    protected TabLayout tabLayout;
    protected Button articleWriteBTN;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        KhumuApplication.container.inject(this);
        generateFeedViewModel();
        this.feedViewModel.ListBoards(null, true);
    }

    @Override
    protected void generateFeedViewModel() {
        this.feedViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.Factory(){
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new FeedViewModel(boardRepository, articleRepository);
            }
        }).get(FeedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // 아직 ViewModel은 안 다룸.
        // homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // 나의 부모인 컨테이너에서 내가 그리고자 하는 녀석을 얻어옴. 사실상 루트로 사용할 애를 객체와.
        // inflate란 xml => java 객체
        FragmentTabFeedBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_feed, container, false);
        View root = binding.getRoot();
        // binding하며 사용할 Fragment가 사용하는 변수인 viewModel을 설정해줌.
        binding.setFeedViewModel(this.feedViewModel);
        binding.setLifecycleOwner(this);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        feedViewModel.clearArticles();
//        feedViewModel.ListArticles();
    }

    @Override
    protected void findViews(View root){
        super.findViews(root);
        tabLayout = root.findViewById(R.id.tab_feed_tab_layout);
        articleWriteBTN = root.findViewById(R.id.tab_feed_article_write_btn);
    }

    @Override
    protected void setEventListeners(View root){
        super.setEventListeners(root);
        feedViewModel.getLiveDataBoards().observe(getViewLifecycleOwner(), new Observer<List<Board>>() {
            @Override
            public void onChanged(List<Board> changedSet) {
                // 처음엔 나의 피드만 있다가 내가 follow한 게시물들이 추가됨.
                // changedSet과 tab list를 동일하게 하려면 한 번 remove해줘야함.
                tabLayout.removeAllTabs();
                for(Board b: changedSet){
                    tabLayout.addTab(
                            tabLayout.newTab().setText(b.getDisplayName())
                    );
                }
            }
        });

        // 지금은 초기에 Following 탭이 여러번 선택되어서 깜빡깜빡거리는 게 심하고 그게 아니어도 깜빡거리기는 하는 듯...?
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // TODO : tab의 상태가 선택 상태로 변경.
                Log.d(TAG, "onTabSelected: " + tab.getText());
                articleRecyclerView.smoothScrollToPosition(0);
                feedViewModel.setCurrentBoard(String.valueOf(tab.getText()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // TODO : tab의 상태가 선택되지 않음으로 변경.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // TODO : 이미 선택된 tab이 다시
            }
        });
        articleWriteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent writeIntent = new Intent(getContext(), ArticleWriteActivity.class);
                v.getContext().startActivity(writeIntent);
            }
        });
    }
}