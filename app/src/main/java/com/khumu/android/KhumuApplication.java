package com.khumu.android;

import android.app.Application;

import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.CommentRepository;
import com.khumu.android.repository.LikeArticleRepository;
import com.khumu.android.util.Util;

import java.util.ArrayList;

public class KhumuApplication extends Application {
    private static KhumuApplication khumu;
    public String username;
    public ArticleRepository articleRepository = new ArticleRepository();
    public LikeArticleRepository likeArticleRepository = new LikeArticleRepository();
    public CommentRepository commentRepository = new CommentRepository();

    @Override
    public void onCreate() {
        super.onCreate();
        this.username = Util.DEFAULT_USERNAME;
        khumu = this;
    }

    public static KhumuApplication getInstance() { return khumu; }
}
