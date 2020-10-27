package com.khumu.android.ui.article.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.khumu.android.R;
import com.khumu.android.ui.home.HomeViewModel;

public class ArticleDetailFragment extends Fragment {

    private TextView articleDetailTitleTV;
    private TextView articleDetailContentTV;
    private TextView articleCommentCountTV;
    private TextView articleAuthorUsernameTV;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
//        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 나의 부모인 컨테이너에서 내가 그리고자 하는 녀석을 얻어옴. 사실상 루트로 사용할 애를 객체와.
        // inflate란 xml => java 객체
        View root = inflater.inflate(R.layout.fragment_article_detail, container, false);

        articleDetailTitleTV = root.findViewById(R.id.article_detail_title_tv);
        articleDetailContentTV = root.findViewById(R.id.article_detail_content_tv);
        articleCommentCountTV = root.findViewById(R.id.article_comment_count_tv);
        articleAuthorUsernameTV = root.findViewById(R.id.article_author_username_tv);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initWithIntentExtra();
    }

    private void initWithIntentExtra(){
        Intent intent = getActivity().getIntent();
        String titleString = intent.getStringExtra("articleTitle");
        String contentString = intent.getStringExtra("articleContent");
        String commentCountString = intent.getStringExtra("articleCommentCount");
        String authorUsernameString = intent.getStringExtra("articleAuthorUsername");

        articleDetailTitleTV.setText(titleString);
        articleDetailContentTV.setText(contentString);
        articleCommentCountTV.setText("댓글 " + commentCountString);
        articleAuthorUsernameTV.setText(authorUsernameString);
    }
}
