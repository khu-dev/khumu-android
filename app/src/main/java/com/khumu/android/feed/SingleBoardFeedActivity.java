package com.khumu.android.feed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.khumu.android.BaseKhumuActivity;
import com.khumu.android.KhumuApplication;
import com.khumu.android.MainActivity;
import com.khumu.android.R;
import com.khumu.android.data.Board;
import com.khumu.android.login.LoginActivity;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;

import javax.inject.Inject;

public class SingleBoardFeedActivity extends BaseKhumuActivity {

    private final static String TAG = "SingleBoardActivity";
    private SingleBoardFeedFragment feedFragment;
    private Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KhumuApplication.container.inject(this);
        Log.d(TAG, "onCreate: ");
        toolbar = findViewById(R.id.toolbar);
        this.board = (Board) this.getIntent().getSerializableExtra("board");
        setContentView(R.layout.activity_single_board_feed);
//
//        // setContentView로 바로 생성되는 Fragment에는 인자를 전달할 수 없기 때문에
//        // FragmentContainerView만 xml에서 만들고, Fragment는 programmatic하게 생성
        FragmentContainerView f = findViewById(R.id.single_board_articles_fragment_container);
        this.feedFragment = new SingleBoardFeedFragment(this.board);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.single_board_articles_fragment_container, this.feedFragment);
        fragmentTransaction.commit();

        toolbar = findViewById(R.id.toolbar);
        ((TextView)toolbar.findViewById(R.id.toolbar_subtitle))
                .setText(board.getDisplayName());
    }
}
