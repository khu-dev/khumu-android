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
import com.khumu.android.data.ArticleTag;
import com.khumu.android.data.Board;
import com.khumu.android.databinding.FragmentMyPageBinding;
import com.khumu.android.feed.SingleBoardFeedActivity;
import com.khumu.android.R;

import java.util.ArrayList;
import java.util.List;

public class MyPageFragment extends Fragment {
    private final static String TAG = "MyPageFragment";
    private FragmentMyPageBinding fragmentMyPageBinding;
    private MyPageViewModel myPageViewModel;
    private FlexboxLayoutManager followingArticleTagsLayoutManager;
    private ArticleTagAdapter followingArticleTagAdapter;

    // namespace는 안 적어줘도 되는건가? 안 적어야 동작하네
    @BindingAdapter("article_tag_list")
    public static void bindItem(RecyclerView recyclerView, LiveData articleTagList){
        ArticleTagAdapter adapter = new ArticleTagAdapter((List<ArticleTag>) articleTagList.getValue());
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
        fragmentMyPageBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false);
        View root = fragmentMyPageBinding.getRoot();
        // binding하며 사용할 Fragment가 사용하는 변수들을 주입해준다.
        fragmentMyPageBinding.setViewModel(myPageViewModel);
        fragmentMyPageBinding.setFragment(this);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //view는 root 즉 fragment
        findViews(view);
    }

    private void findViews(View root){
        followingArticleTagsLayoutManager = new FlexboxLayoutManager(this.getContext());
        followingArticleTagsLayoutManager.setFlexDirection(FlexDirection.ROW); // 기본값임
        followingArticleTagsLayoutManager.setJustifyContent(JustifyContent.FLEX_START); // 기본값임
        fragmentMyPageBinding.myPageFollowingArticleTagsRecyclerView.setLayoutManager(followingArticleTagsLayoutManager);

        // dummy tags
        List<ArticleTag> l = new ArrayList<>();
        l.add(new ArticleTag("hello", true));
        l.add(new ArticleTag("world", true));
        l.add(new ArticleTag("내 이름은 진격의", true));
        l.add(new ArticleTag("거인", true));
        l.add(new ArticleTag("캬하하", true));
        l.add(new ArticleTag("현", true));
        l.add(new ArticleTag("일본여행", true));
        l.add(new ArticleTag("미국여행", true));
        l.add(new ArticleTag("연탄재", true));
        l.add(new ArticleTag("함부로", true));
        l.add(new ArticleTag("밟지마라", true));
        l.add(new ArticleTag("난", true));
        l.add(new ArticleTag("뜨거웡!", true));
        l.add(new ArticleTag("world", true));
        l.add(new ArticleTag("hello", true));
        l.add(new ArticleTag("world", true));

        followingArticleTagAdapter = new ArticleTagAdapter(l);
        fragmentMyPageBinding.myPageFollowingArticleTagsRecyclerView.setAdapter(followingArticleTagAdapter);
    }

    public void onClickArticlesWrittenByMeTV(View v){
        startSingleBoardFeedActivity("my", null, "작성한 게시물", "본인이 작성한 게시물들입니다.");
    }

    public void onClickArticlesLikedTV(View v){
        startSingleBoardFeedActivity("liked", null,"좋아요한 게시물", "좋아요를 누른 게시물들입니다.");
    }
    public void onClickArticlesBookmarkedTV(View v){
        startSingleBoardFeedActivity("bookmarked", null, "북마크한 게시물", "북마크해놓은 게시물들입니다.");
    }
    public void onClickArticlesCommentedTV(View v){
        startSingleBoardFeedActivity("commented", null, "댓글단 게시물", "댓글을 작성했던 게시물들입니다.");
    }

    /**
     * SingleBoard에 대한 Feed를 보여주는 Activity인 SingleBoardFeedActivity를 시작한다.
     * 주로 MyPage에서 내가 작성한 게시물이나 좋아요한 게시물 등 논리적 Board에 대한 Feed를 보고자 할 때 사용된다.
     */
    protected void startSingleBoardFeedActivity(String boardName, String boardCategory, String boardDisplayName, String boardDescription) {
        Intent intent = new Intent(MyPageFragment.this.getActivity(), SingleBoardFeedActivity.class);
        Board board = new Board(boardName, boardCategory, boardDisplayName, boardDescription, false, null, null);
        intent.putExtra("board", board);
        startActivity(intent);
    }

    public void onClickLogoutTV(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
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
}