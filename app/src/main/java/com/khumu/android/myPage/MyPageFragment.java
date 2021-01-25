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
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.khumu.android.KhumuApplication;
import com.khumu.android.data.article.Tag;
import com.khumu.android.data.Board;
import com.khumu.android.databinding.FragmentMyPageBinding;
import com.khumu.android.feed.SingleBoardFeedActivity;
import com.khumu.android.R;

import java.util.ArrayList;
import java.util.List;

public class MyPageFragment extends Fragment {
    private final static String TAG = "MyPageFragment";
    private MyPageViewModel myPageViewModel;

    private TextView usernameTV;
    private TextView nicknameTV;
    private TextView articlesWrittenTV;
    private TextView articlesLikedTV;
    private TextView articlesBookmarkedTV;
    private TextView articlesCommentedTV;
    private TextView logoutTV;
    private RecyclerView followingArticleTagsRecyclerView;
    private FlexboxLayoutManager followingArticleTagsLayoutManager;
    private ArticleTagAdapter followingArticleTagAdapter;

    // namespace는 안 적어줘도 되는건가? 안 적어야 동작하네
    @BindingAdapter("article_tag_list")
    public static void bindItem(RecyclerView recyclerView, LiveData articleTagList){
        ArticleTagAdapter adapter = new ArticleTagAdapter((List<Tag>) articleTagList.getValue());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
        if(KhumuApplication.isAuthenticated()){
            KhumuApplication.container.inject(this);
        }

        myPageViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new MyPageViewModel();
            }
        }).get(MyPageViewModel.class);
        myPageViewModel.ListFollowingArticleTags();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // 아직 ViewModel은 안 다룸.
        // homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // 나의 부모인 컨테이너에서 내가 그리고자 하는 녀석을 얻어옴. 사실상 루트로 사용할 애를 객체와.
        // inflate란 xml => java 객체
//        View root = inflater.inflate(R.layout.fragment_my_page, container, false);

        FragmentMyPageBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false);
        View root = binding.getRoot();
        // binding하며 사용할 Fragment가 사용하는 변수인 viewModel을 설정해줌.
        binding.setViewModel(myPageViewModel);
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

//        usernameTV.setText(KhumuApplication.getUsername());
//        nicknameTV.setText(KhumuApplication.getNickname());

        followingArticleTagsRecyclerView = root.findViewById(R.id.my_page_following_article_tags_recycler_view);
        followingArticleTagsLayoutManager = new FlexboxLayoutManager(this.getContext());
        followingArticleTagsLayoutManager.setFlexDirection(FlexDirection.ROW); // 기본값임
        followingArticleTagsLayoutManager.setJustifyContent(JustifyContent.FLEX_START); // 기본값임
        followingArticleTagsRecyclerView.setLayoutManager(followingArticleTagsLayoutManager);
        List<Tag> l = new ArrayList<>();
        l.add(new Tag("hello", true));
        l.add(new Tag("world", true));
        l.add(new Tag("내 이름은 진격의", true));
        l.add(new Tag("거인", true));
        l.add(new Tag("캬하하", true));
        l.add(new Tag("현", true));
        l.add(new Tag("일본여행", true));
        l.add(new Tag("미국여행", true));
        l.add(new Tag("연탄재", true));
        l.add(new Tag("함부로", true));
        l.add(new Tag("밟지마라", true));
        l.add(new Tag("난", true));
        l.add(new Tag("뜨거웡!", true));
        l.add(new Tag("world", true));
        l.add(new Tag("hello", true));
        l.add(new Tag("world", true));


        followingArticleTagAdapter = new ArticleTagAdapter(l);
        followingArticleTagsRecyclerView.setAdapter(followingArticleTagAdapter);

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
                        KhumuApplication.loadKhumuConfig(); // refresh 과정
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