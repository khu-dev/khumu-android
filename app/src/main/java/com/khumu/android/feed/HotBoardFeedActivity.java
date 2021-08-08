package com.khumu.android.feed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.articleWrite.ArticleWriteActivity;
import com.khumu.android.data.Board;
import com.khumu.android.repository.ArticleService;

import javax.inject.Inject;

public class HotBoardFeedActivity extends AppCompatActivity {
    private final static String TAG = "SingleBoardActivity";
    private FeedViewModel feedViewModel;

    @Inject
    public ArticleService articleService;
    // 이건 그냥 간단하게 의존성 주입 안 씀.
    // Module + Provide 말고는 어떻게 주입하지..?
    public WritableBoardPolicy writableBoardPolicy = new WithLogicalBoardWritableBoardPolicy();
    private HotBoardFeedFragment feedFragment;
    private MaterialToolbar toolbar;
    private TextView toolbarTitle;
    private ImageView backButton;
    private Button articleWriteBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        KhumuApplication.applicationComponent.inject(this);

        // board를 선언하고 싶은데 viewModel factory에서 inner class에서 변수 사용으로 final을 강제하기때문에
        // tmpBoard 이용.
        final Board currentBoard = Board.builder().name("hot").displayName("인기 게시판").build();
        // setContentView 이전에 ViewModel을 Activity에서 생성해주어야한다.
        feedViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory(){
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new FeedViewModel(articleService, currentBoard);
            }
        }).get(FeedViewModel.class);
        // TODO(umi0410): 이 로직 viewmodel로 옮기기
        feedViewModel.getCurrentBoard().postValue(currentBoard);
        feedViewModel.listArticles();

        setContentView(R.layout.activity_hot_board_feed);
        feedFragment = (HotBoardFeedFragment) getSupportFragmentManager().findFragmentById(R.id.hot_board_feed_fragment);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        backButton = findViewById(R.id.back_iv);

        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        toolbarTitle.setText(currentBoard.getDisplayName());
        backButton.setImageDrawable(ContextCompat.getDrawable(HotBoardFeedActivity.this, R.drawable.ic_left_arrow_white));
        backButton.setOnClickListener(v -> {
            HotBoardFeedActivity.this.finish();
        });
        articleWriteBTN = findViewById(R.id.single_board_feed_article_write_btn);
        articleWriteBTN.setVisibility(View.GONE);

        // status bar 색상
        // refs: https://stackoverflow.com/a/56433156/9471220
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.red_500));
        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark\
    }

}
