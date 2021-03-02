package com.khumu.android.feed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khumu.android.KhumuApplication;
import com.khumu.android.MainActivity;
import com.khumu.android.R;
import com.khumu.android.data.Board;
import com.khumu.android.databinding.LayoutFeedBinding;
import com.khumu.android.login.LoginActivity;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;

import javax.inject.Inject;

public class SingleBoardFeedActivity extends AppCompatActivity {

    private final static String TAG = "SingleBoardActivity";
    private SingleBoardFeedFragment feedFragment;
    private FeedViewModel feedViewModel;
    private Toolbar toolbar;
    private ImageView backButton;
    @Inject
    public ArticleRepository articleRepository;
    private LayoutFeedBinding layoutFeedBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        KhumuApplication.container.inject(this);
        Board currentBoard = (Board) this.getIntent().getSerializableExtra("board");

        // setContentView 이전에 ViewModel을 Activity에서 생성해주어야한다.
        feedViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory(){
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new FeedViewModel(articleRepository, currentBoard);
            }
        }).get(FeedViewModel.class);
        feedViewModel.setCurrentBoard(currentBoard);
        feedViewModel.ListArticles();

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
}
