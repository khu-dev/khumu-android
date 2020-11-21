package com.khumu.android.home;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.khumu.android.feed.FeedViewModel;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;

public class HomeViewModelFactory implements ViewModelProvider.Factory {
    private BoardRepository br;

    public HomeViewModelFactory(BoardRepository br) {
        this.br = br;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new HomeViewModel(br);
    }

}