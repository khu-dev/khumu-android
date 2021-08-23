package com.khumu.android.di.component;

import com.khumu.android.adapter.GrayBackgroundArticleAdapter;
import com.khumu.android.adapter.SearchedBoardAdapter;
import com.khumu.android.adapter.SimpleAnnouncementAdapter;
import com.khumu.android.boardList.BoardAdapter;
import com.khumu.android.boardList.BoardListFragment;
import com.khumu.android.KhumuApplication;
import com.khumu.android.articleDetail.ArticleDetailFragment;
import com.khumu.android.articleDetail.CommentAdapter;
import com.khumu.android.articleDetail.ReplyAdapter;
import com.khumu.android.articleWrite.ArticleWriteActivity;
import com.khumu.android.adapter.ArticleAdapter;
import com.khumu.android.feed.BaseFeedFragment;
import com.khumu.android.feed.FollowingBoardAdapter;
import com.khumu.android.feed.HotBoardFeedActivity;
import com.khumu.android.feed.HotBoardFeedFragment;
import com.khumu.android.feed.MyFeedFragment;
import com.khumu.android.feed.SingleBoardFeedActivity;
import com.khumu.android.feed.SingleBoardFeedFragment;
import com.khumu.android.home.HomeFragment;
import com.khumu.android.login.LoginActivity;
import com.khumu.android.myPage.MyPageFragment;
import com.khumu.android.notifications.NotificationActivity;
import com.khumu.android.qrCode.QrCodeActivity;
import com.khumu.android.repository.BoardRepository;
import com.khumu.android.repository.CommentRepository;
import com.khumu.android.repository.LikeArticleRepository;
import com.khumu.android.di.module.RetrofitModule;
import com.khumu.android.search.CommunitySearchActivity;
import com.khumu.android.signUp.AdditionalFormFragment;
import com.khumu.android.signUp.Info21AuthenticationFragment;
import com.khumu.android.signUp.Info21SignUpActivity;
import com.khumu.android.signUp.SignUpAgreementFragment;
import com.khumu.android.study.StudyAdapter;
import com.khumu.android.study.StudyFragment;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules= {RetrofitModule.class})
public interface ApplicationComponent {
    // 여기에 의존성으로 주입될 수 있는 주입할 클래스들 명시
    // component에는 최소 한개의 abstract 메서드가 있어야 한다.
    // 그 메소드를 통해 의존성 주입을 하므로, injection을 시킬 객체를 리턴(provision method)하거나
    // 멤버 파라미터로 의존성 주입을 시킬 객체를 넘기는(Members-injection methods) 메소드여야함
    // ??? 모듈로 생성되는 애들은 여기에 적지 않는 것임? - 그것이 아니고 의존성을 주입할 때

    // 여기에 의존성 주입을 원하는 클래스들을 명시

    // provision method
    BoardRepository getBoardRepository();
    Retrofit getRetrofit();

    // Members-injection methods
    void inject(LikeArticleRepository likeArticleRepository);
    void inject(CommentRepository commentRepository);
    void inject(BoardRepository boardRepository);

    void inject(StudyAdapter studyAdapter);
    void inject(BoardAdapter boardAdapter);
    void inject(SearchedBoardAdapter searchedBoardAdapter);
    void inject(ReplyAdapter replyAdapter);
    void inject(CommentAdapter commentAdapter);
    void inject(ArticleAdapter articleAdapter);
    void inject(GrayBackgroundArticleAdapter adapter);
    void inject(FollowingBoardAdapter followingBoardAdapter);
    void inject(SimpleAnnouncementAdapter adapter);

    void inject(StudyFragment studyFragment);
    void inject(BaseFeedFragment feedFragment);
    void inject(MyFeedFragment feedFragment);
    void inject(SingleBoardFeedFragment feedFragment);
    void inject(ArticleDetailFragment articleDetailFragment);
    void inject(HomeFragment homeFragment);
    void inject(MyPageFragment myPageFragment);
    void inject(BoardListFragment boardListFragment);
    void inject(HotBoardFeedFragment fragment);
    void inject(SignUpAgreementFragment fragment);
    void inject(Info21AuthenticationFragment fragment);
    void inject(AdditionalFormFragment fragment);

    void inject(KhumuApplication application);
    void inject(LoginActivity loginActivity);
    void inject(ArticleWriteActivity articleWriteActivity);
    void inject(SingleBoardFeedActivity singleBoardFeedActivity);
    void inject(NotificationActivity notificationActivity);
    void inject(QrCodeActivity qrCodeActivity);
    void inject(Info21SignUpActivity info21SignUpActivity);
    void inject(CommunitySearchActivity activity);
    void inject(HotBoardFeedActivity activity);
}

