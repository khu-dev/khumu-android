package com.khumu.android.articleDetail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.khumu.android.data.Article;
import com.khumu.android.repository.CommentRepository;

public class CommentViewFactory implements ViewModelProvider.Factory{
    private CommentRepository cr;
    public String articleID;
    public Article article;
    public CommentViewFactory(CommentRepository cr, Article article, String articleID) {
        this.cr = cr;
        this.article = article;
        this.articleID = articleID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) { return (T) new CommentViewModel(cr, article, articleID); }
}

