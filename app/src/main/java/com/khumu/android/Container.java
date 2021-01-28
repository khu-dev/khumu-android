package com.khumu.android;

import com.khumu.android.articleDetail.ArticleDetailFragment;
import com.khumu.android.articleDetail.CommentAdapter;
import com.khumu.android.articleDetail.ReplyAdapter;
import com.khumu.android.articleWrite.ArticleWriteActivity;
import com.khumu.android.feed.ArticleAdapter;
import com.khumu.android.feed.BaseFeedFragment;
import com.khumu.android.feed.SingleBoardFeedActivity;
import com.khumu.android.feed.SingleBoardFeedFragment;
import com.khumu.android.feed.TabFeedFragment;
import com.khumu.android.home.HomeFragment;
import com.khumu.android.login.LoginActivity;
import com.khumu.android.myPage.MyPageFragment;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;
import com.khumu.android.repository.CommentRepository;
import com.khumu.android.repository.LikeArticleRepository;
import com.khumu.android.repository.TokenRepository;
import com.khumu.android.retrofitInterface.ArticleService;
import com.khumu.android.usecase.ArticleUseCase;
import com.khumu.android.util.RetrofitClient;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules=RetrofitClient.class)
public interface Container {
    // 여기에 의존성으로 주입될 수 있는 주입할 클래스들 명시
    // ??? 모듈로 생성되는 애들은 여기에 적지 않는 것임?
    TokenRepository getTokenRepository();
    BoardRepository getBoardRepository();
    ArticleRepository getArticleRepository();
    LikeArticleRepository getLikeArticleRepository();
    CommentRepository getCommentRepository();
    ArticleUseCase getArticleUseCase();
    Retrofit getRetrofit();


    // 여기에 의존성 주입을 원하는 클래스들을 명시
    void inject(ArticleRepository articleRepository);
    void inject(LikeArticleRepository likeArticleRepository);
    void inject(CommentRepository commentRepository);
    void inject(TokenRepository tokenRepository);
    void inject(BoardRepository boardRepository);

    void inject(ArticleUseCase articleUseCase);

    void inject(ReplyAdapter replyAdapter);
    void inject(CommentAdapter commentAdapter);
    void inject(ArticleAdapter articleAdapter);

    void inject(BaseFeedFragment feedFragment);
    void inject(TabFeedFragment feedFragment);
    void inject(SingleBoardFeedFragment feedFragment);
    void inject(ArticleDetailFragment articleDetailFragment);
    void inject(HomeFragment homeFragment);
    void inject(MyPageFragment myPageFragment);

    void inject(LoginActivity loginActivity);
    void inject(ArticleWriteActivity articleWriteActivity);
    void inject(SingleBoardFeedActivity singleBoardFeedActivity);

}

