package com.khumu.android;

import com.khumu.android.articleDetail.ArticleDetailFragment;
import com.khumu.android.data.LikeArticle;
import com.khumu.android.feed.ArticleAdapter;
import com.khumu.android.feed.FeedFragment;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.CommentRepository;
import com.khumu.android.repository.LikeArticleRepository;
import com.khumu.android.repository.TokenRepository;

import dagger.Component;

@Component
public interface Container {
    ArticleRepository getArticleRepository();
    LikeArticleRepository getLikeArticleRepository();
    CommentRepository getCommentRepository();
    TokenRepository getTokenRepository();

    void inject(ArticleRepository articleRepository);
    void inject(LikeArticleRepository likeArticleRepository);
    void inject(CommentRepository commentRepository);
    void inject(TokenRepository tokenRepository);
    void inject(FeedFragment feedFragment);
    void inject(ArticleDetailFragment articleDetailFragment);
    void inject(ArticleAdapter articleAdapter);
}

