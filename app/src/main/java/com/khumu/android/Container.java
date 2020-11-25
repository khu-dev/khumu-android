package com.khumu.android;

import com.khumu.android.articleDetail.ArticleDetailFragment;
import com.khumu.android.articleWrite.ArticleWriteActivity;
import com.khumu.android.data.LikeArticle;
import com.khumu.android.feed.ArticleAdapter;
import com.khumu.android.feed.BoardAdapter;
import com.khumu.android.feed.FeedFragment;
import com.khumu.android.feed.FeedViewModel;
import com.khumu.android.home.HomeFragment;
import com.khumu.android.login.LoginActivity;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;
import com.khumu.android.repository.CommentRepository;
import com.khumu.android.repository.LikeArticleRepository;
import com.khumu.android.repository.TokenRepository;

import dagger.Component;

@Component
public interface Container {
    // 여기에 의존성 주입할 클래스들 명시
    TokenRepository getTokenRepository();
    BoardRepository getBoardRepository();
    ArticleRepository getArticleRepository();
    LikeArticleRepository getLikeArticleRepository();
    CommentRepository getCommentRepository();

    // 여기에 의존성 주입을 원하는 클래스들을 명시
    void inject(ArticleRepository articleRepository);
    void inject(LikeArticleRepository likeArticleRepository);
    void inject(CommentRepository commentRepository);
    void inject(TokenRepository tokenRepository);

    void inject(ArticleAdapter articleAdapter);
    void inject(BoardAdapter boardAdapter);

    void inject(FeedFragment feedFragment);
    void inject(ArticleDetailFragment articleDetailFragment);
    void inject(HomeFragment articleAdapter);

    void inject(LoginActivity loginActivity);
    void inject(ArticleWriteActivity articleWriteActivity);

}

