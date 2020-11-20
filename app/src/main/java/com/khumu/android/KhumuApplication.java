package com.khumu.android;

import android.app.Application;

import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.CommentRepository;
import com.khumu.android.repository.LikeArticleRepository;
import com.khumu.android.util.Util;

import java.util.ArrayList;

import javax.inject.Inject;

public class KhumuApplication extends Application {
    public static String username;
    public static Container container;
    @Inject public ArticleRepository articleRepository = new ArticleRepository();
    @Inject public LikeArticleRepository likeArticleRepository = new LikeArticleRepository();
    @Inject public CommentRepository commentRepository = new CommentRepository();

    @Override
    public void onCreate() {
        super.onCreate();
        this.username = Util.DEFAULT_USERNAME;
        // 우리의 필요한 의존성들을 이 container에 Singleton으로 관리
        container = DaggerContainer.create();
    }
}
