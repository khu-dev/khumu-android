package com.khumu.android.feed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.articleDetail.ArticleDetailFragment;
import com.khumu.android.articleWrite.ArticleWriteActivity;
import com.khumu.android.data.Board;
import com.khumu.android.repository.ArticleService;

import javax.inject.Inject;

public class SingleBoardFeedActivity extends AppCompatActivity {
    private final static String TAG = "SingleBoardActivity";
    private FeedViewModel feedViewModel;
    public final static int NEW_ARTICLE_WRITTEN = 2;
    @Inject
    public ArticleService articleService;
    // 이건 그냥 간단하게 의존성 주입 안 씀.
    // Module + Provide 말고는 어떻게 주입하지..?
    public WritableBoardPolicy writableBoardPolicy = new WithLogicalBoardWritableBoardPolicy();
    private NestedScrollView nestedScrollView;
    private SingleBoardFeedFragment feedFragment;
    private MaterialToolbar toolbar;
    private ImageView backButton;
    private Button articleWriteBTN;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView boardDescriptionTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        KhumuApplication.applicationComponent.inject(this);

        // board를 선언하고 싶은데 viewModel factory에서 inner class에서 변수 사용으로 final을 강제하기때문에
        // tmpBoard 이용.
        Board tmpBoard = (Board) this.getIntent().getSerializableExtra("board");
        if(tmpBoard == null){
            tmpBoard = getDefaultBoard();
        }
        final Board currentBoard = tmpBoard;
        // setContentView 이전에 ViewModel을 Activity에서 생성해주어야한다.
        feedViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory(){
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new FeedViewModel(articleService, currentBoard);
            }
        }).get(FeedViewModel.class);
        feedViewModel.getCurrentBoard().postValue(currentBoard);
        feedViewModel.listArticles();

        setContentView(R.layout.activity_single_board_feed);

        feedFragment = (SingleBoardFeedFragment) getSupportFragmentManager().findFragmentById(R.id.single_board_articles_fragment);
        toolbar = findViewById(R.id.toolbar);
        toolbar = findViewById(R.id.toolbar);
        ((TextView)toolbar.findViewById(R.id.toolbar_before_title))
                .setText(currentBoard.getDisplayName());
        backButton = findViewById(R.id.back_iv);
        backButton.setOnClickListener(v -> {
            SingleBoardFeedActivity.this.finish();
        });
        articleWriteBTN = findViewById(R.id.single_board_feed_article_write_btn);
        if (!writableBoardPolicy.isWritableBoard(currentBoard)) {
            articleWriteBTN.setVisibility(View.GONE);
        } else {
            articleWriteBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent writeIntent = new Intent(SingleBoardFeedActivity.this, ArticleWriteActivity.class);
                    writeIntent.putExtra("board", feedViewModel.getCurrentBoard().getValue());
                    startActivityForResult(writeIntent, NEW_ARTICLE_WRITTEN);
                }
            });
        }
        swipeRefreshLayout = findViewById(R.id.feed_body_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(()->{
            feedViewModel.listArticles();
            swipeRefreshLayout.setRefreshing(false);
        });

        boardDescriptionTV = findViewById(R.id.feed_board_description_tv);
        if (currentBoard != null && currentBoard.getDescription() != null) {
            boardDescriptionTV.setText(currentBoard.getDescription());
        } else{
            boardDescriptionTV.setVisibility(View.GONE);
        }

        nestedScrollView = findViewById(R.id.single_board_nested_scroll_view);
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) ->  {
            if (v.getChildAt(v.getChildCount() - 1) != null)
            {
                if (scrollY > oldScrollY)
                {
                    if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight()))
                    {
                        feedViewModel.loadMoreArticles();
                    }
                }
            }
        });
    }

    // 혹시 모를 Null pointer exception 방지용의 기본 게시판.
    private Board getDefaultBoard() {
        Board b = new Board();
        b.setDisplayName("자유게시판");
        b.setName("free");

        return b;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            // article modify 후에 성공적이었다면.
            case NEW_ARTICLE_WRITTEN:{
                if(resultCode == Activity.RESULT_OK){
                    this.feedViewModel.listArticles();
                }
            }
        }
    }
}
