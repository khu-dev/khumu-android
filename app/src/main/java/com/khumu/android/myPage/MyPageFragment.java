package com.khumu.android.myPage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Board;
import com.khumu.android.feed.SingleBoardFeedActivity;
import com.khumu.android.R;

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

    private TextView usernameTV;
    private TextView nicknameTV;
    private TextView articlesWrittenTV;
    private TextView articlesLikedTV;
    private TextView articlesBookmarkedTV;
    private TextView articlesCommentedTV;
    private TextView logoutTV;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
        if(KhumuApplication.isAuthenticated()){
            KhumuApplication.container.inject(this);
        }
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
        findViews(view);
//        setAdapters();
//        setEventListeners(view);
    }

    private void findViews(View root){
        usernameTV = root.findViewById(R.id.my_page_profile_username_tv);
        nicknameTV = root.findViewById(R.id.my_page_profile_nickname_tv);
        articlesWrittenTV = root.findViewById(R.id.my_page_articles_written_tv);
        articlesLikedTV = root.findViewById(R.id.my_page_articles_liked_tv);
        articlesBookmarkedTV = root.findViewById(R.id.my_page_articles_bookmarked_tv);
        articlesCommentedTV = root.findViewById(R.id.my_page_articles_commented_tv);
        logoutTV = root.findViewById(R.id.my_page_logout_tv);

        usernameTV.setText(KhumuApplication.getUsername());
        nicknameTV.setText(KhumuApplication.getNickname());

        articlesWrittenTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageFragment.this.getActivity(), SingleBoardFeedActivity.class);
                Board board = new Board("my", null, "작성한 게시물", "본인이 작성한 게시물들입니다.", false, null, null);
                intent.putExtra("board", board);
                startActivity(intent);
            }
        });

        articlesLikedTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageFragment.this.getActivity(), SingleBoardFeedActivity.class);
                Board board = new Board("liked", null, "좋아요한 게시물", "좋아요를 누른 게시물들입니다.", false, null, null);
                intent.putExtra("board", board);
                startActivity(intent);
            }
        });

        articlesBookmarkedTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageFragment.this.getActivity(), SingleBoardFeedActivity.class);
                Board board = new Board("bookmarked", null, "북마크한 게시물", "북마크해놓은 게시물들입니다.", false, null, null);
                intent.putExtra("board", board);
                startActivity(intent);
            }
        });

        articlesCommentedTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageFragment.this.getActivity(), SingleBoardFeedActivity.class);
                Board board = new Board("commented", null, "댓글단 게시물", "댓글을 작성했던 게시물들입니다.", false, null, null);
                intent.putExtra("board", board);
                startActivity(intent);
            }
        });
        logoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                builder.setTitle("로그아웃하시겠습니까?").setMessage("로그아웃 후 쿠뮤가 종료됩니다.").setCancelable(true).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        KhumuApplication.clearKhumuAuthenticationConfig();
                        MyPageFragment.this.getActivity().finishAffinity();
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}