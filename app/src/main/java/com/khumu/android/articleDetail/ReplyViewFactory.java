package com.khumu.android.articleDetail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.khumu.android.repository.ReplyRepository;

public class ReplyViewFactory implements ViewModelProvider.Factory{
    private ReplyRepository rr;
    public String commentID;
    public ReplyViewFactory(ReplyRepository rr, String commentID) { this.rr = rr; this.commentID = commentID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) { return (T) new ReplyViewModel(rr, commentID); }
}