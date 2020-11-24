package com.khumu.android.feed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Board;
import com.khumu.android.data.SimpleUser;
import com.khumu.android.home.RecentArticleAdapter;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;
import com.khumu.android.repository.LikeArticleRepository;
import com.khumu.android.util.Util;
import com.khumu.android.R;
import com.khumu.android.data.Article;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FeedFragment extends Fragment {
    private final static String TAG = "FeedFragment";
    @Inject public BoardRepository boardRepository;
    @Inject public ArticleRepository articleRepository;
    private FeedViewModel feedViewModel;

    private BoardAdapter boardAdapter;
    private ArticleAdapter articleAdapter;

    private RecyclerView articlesView;
    private ListView boardsView;

    private LinearLayoutManager linearLayoutManager;
    private ImageView likeIcon;
    private ImageView commentIcon;
    private ImageView bookmarkIcon;

    private ImageView toggleBoardsBTN;
    private BoardsToggler boardsToggler;
//    private EditText writeArticleTitleET;
//    private EditText writeArticleContentET;
//    private ConstraintLayout writeArticleHeaderCL;
//    private LinearLayout writeArticleExpandableLL;
//    private ImageButton writeArticleExpandBTN;
//    private WriteArticleToggler writeArticleToggler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
        KhumuApplication.container.inject(this);
        feedViewModel = new ViewModelProvider(this, new FeedViewModelFactory(boardRepository, articleRepository)).get(FeedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // 아직 ViewModel은 안 다룸.
        // homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // 나의 부모인 컨테이너에서 내가 그리고자 하는 녀석을 얻어옴. 사실상 루트로 사용할 애를 객체와.
        // inflate란 xml => java 객체
        View root = inflater.inflate(R.layout.fragment_feed, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //view는 root 즉 fragment

        // root.findViewById(R.id.action_navigation_board_to_navigation_home).setOnClickListener(Navigation);
        // xml 상에 recyclerview는 실질적으로 아이템이 어떻게 구현되어있는지 정의되어있지 않다.
        // linearlayout의 형태를 이용하겠다면 linearlayoutmanager을 이용한다.
        likeIcon = (ImageView) view.findViewById(R.id.article_item_like_icon);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        articlesView = view.findViewById(R.id.feed_articles_list);

        boardsView = view.findViewById(R.id.feed_boards_list);
        toggleBoardsBTN = view.findViewById(R.id.toggle_boards_btn);
        boardsToggler = new BoardsToggler(boardsView, toggleBoardsBTN);

        boardsView.setVisibility(View.GONE);
        toggleBoardsBTN.setOnClickListener(boardsToggler);

        boardAdapter = new BoardAdapter(
            getContext(),
            R.layout.layout_feed_board_item,
            new ArrayList<Board>(),
            feedViewModel,
            boardsToggler
        );
        articleAdapter = new ArticleAdapter(new ArrayList<>());

        articlesView.setLayoutManager(linearLayoutManager);
        articlesView.setAdapter(articleAdapter);
        boardsView.setAdapter(boardAdapter);

        feedViewModel.getLiveDataBoards().observe(getViewLifecycleOwner(), new Observer<List<Board>>() {
            @Override
            public void onChanged(List<Board> changedSet) {
                boardAdapter.boards.addAll(changedSet);
                boardAdapter.notifyDataSetChanged();
            }
        });

        feedViewModel.getLiveDataCurrentBoard().observe(getViewLifecycleOwner(), new Observer<Board>() {
            @Override
            public void onChanged(Board board) {
                Log.d(TAG, "onChanged: "+board.getDisplayName());
                ((TextView)view.findViewById(R.id.feed_current_board_display_name)).setText(board.getDisplayName());
                // current Board가 변경되었으니 board item들을 색 변경하기위해  다시 그려야함.
                boardAdapter.notifyDataSetChanged();
            }
        });

        feedViewModel.getLiveDataArticles().observe(getViewLifecycleOwner(), new Observer<List<Article>>() {
            // 추가인지 삭제인지를 모르네
            @Override
            public void onChanged(List<Article> added) {
                articleAdapter.articleList.clear();
                articleAdapter.articleList.addAll(added);
                articleAdapter.notifyDataSetChanged();
                // scroll to top
                articlesView.smoothScrollToPosition(0);
            }
        });



//        writeArticleTitleET = view.findViewById(R.id.article_write_title);
//        writeArticleContentET = view.findViewById(R.id.article_write_content);


//        writeArticleHeaderCL = view.findViewById(R.id.wrapper_article_write_header);
//        writeArticleExpandableLL = view.findViewById(R.id.wrapper_article_write_expandable);
//        writeArticleExpandBTN = view.findViewById(R.id.wrapper_article_write_expand_btn);
            //set visibility to GONE

    }

//    public void CreateArticle(){
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    feedViewModel.CreateArticle(new Article(
//                            null, new SimpleUser(Util.DEFAULT_USERNAME, "active"),
//                            writeArticleTitleET.getText().toString(),
//                            writeArticleContentET.getText().toString(),
//                            null
//                    ));
//                    writeArticleTitleET.setText("");
//                    writeArticleContentET.setText("");
//                    feedViewModel.ListArticle();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    public class FetchAfterCreateArticleAsyncTask extends AsyncTask{
//
//        @Override
//        protected Object doInBackground(Object[] objects) {
//            try {
//                feedViewModel.CreateArticle(new Article(
//                        null, new SimpleUser(Util.DEFAULT_USERNAME, "active"),
//                        writeArticleTitleET.getText().toString(),
//                        writeArticleContentET.getText().toString(),
//                        null
//                ));
//                writeArticleTitleET.setText("");
//                writeArticleContentET.setText("");
//                feedViewModel.ListArticle();
//            } catch (Exception e) {
//                e.printStackTrace();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        e.printStackTrace();
//                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//            return null;
//        }
//    }
//

}