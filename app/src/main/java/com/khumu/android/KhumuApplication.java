package com.khumu.android;

import android.app.Application;

import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.CommentRepository;
import com.khumu.android.repository.LikeArticleRepository;
import com.khumu.android.util.Util;

import java.util.ArrayList;

import javax.inject.Inject;

public class KhumuApplication extends Application {
    public String username;
    public static Container container;
    @Inject
    public ArticleRepository articleRepository = new ArticleRepository();
    @Inject public LikeArticleRepository likeArticleRepository = new LikeArticleRepository();
    @Inject public CommentRepository commentRepository = new CommentRepository();

    @Override
    public void onCreate() {
        super.onCreate();
        this.username = Util.DEFAULT_USERNAME;
        container = DaggerContainer.create();
    }
}
