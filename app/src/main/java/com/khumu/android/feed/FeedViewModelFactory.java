package com.khumu.android.feed;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.khumu.android.articleDetail.CommentViewModel;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;

public class FeedViewModelFactory implements ViewModelProvider.Factory {
    private BoardRepository br;
    private ArticleRepository ar;


    public FeedViewModelFactory(BoardRepository br, ArticleRepository ar) {
        this.br = br;
        this.ar = ar;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new FeedViewModel(br, ar);
    }
}