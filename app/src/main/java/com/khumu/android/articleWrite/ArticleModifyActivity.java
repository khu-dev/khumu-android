package com.khumu.android.articleWrite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.myPage.ArticleTagAdapter;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;

import java.util.ArrayList;

public class ArticleModifyActivity extends ArticleWriteActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Article original = (Article) this.getIntent().getSerializableExtra("article");
        viewModel.setArticle(original);
    }
}
