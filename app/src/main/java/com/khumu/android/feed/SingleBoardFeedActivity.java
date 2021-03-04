package com.khumu.android.feed;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.Board;
import com.khumu.android.databinding.LayoutFeedBinding;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.retrofitInterface.ArticleService;

import javax.inject.Inject;

public class SingleBoardFeedActivity extends AppCompatActivity {

    private final static String TAG = "SingleBoardActivity";
    private SingleBoardFeedFragment feedFragment;
    private FeedViewModel feedViewModel;
    private MaterialToolbar toolbar;
    private ImageView backButton;
    @Inject
    public ArticleService articleService;
    private LayoutFeedBinding layoutFeedBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        KhumuApplication.container.inject(this);

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
        ((TextView)toolbar.findViewById(R.id.toolbar_title))
                .setText(currentBoard.getDisplayName());
        backButton = findViewById(R.id.back_iv);
        backButton.setOnClickListener(v -> {
            SingleBoardFeedActivity.this.finish();
        });
    }

    private Board getDefaultBoard() {
        Board b = new Board();
        b.setDisplayName("자유게시판");
        b.setName("free");

        return b;
    }
}
