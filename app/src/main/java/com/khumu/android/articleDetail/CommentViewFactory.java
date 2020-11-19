package com.khumu.android.articleDetail;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.khumu.android.repository.CommentRepository;

public class CommentViewFactory implements ViewModelProvider.Factory{
    private CommentRepository cr;

    public CommentViewFactory(CommentRepository cr) { this.cr = cr;}

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) { return (T) new CommentViewModel(cr); }
}
