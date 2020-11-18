package com.khumu.android.feed;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.khumu.android.articleDetail.CommentViewModel;
import com.khumu.android.repository.ArticleRepository;

public class FeedViewModelFactory implements ViewModelProvider.Factory {
    private ArticleRepository ar;

    public FeedViewModelFactory(ArticleRepository ar) {
        this.ar = ar;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new FeedViewModel(ar);
    }
}