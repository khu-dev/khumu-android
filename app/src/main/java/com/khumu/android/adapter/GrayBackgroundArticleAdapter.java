package com.khumu.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.articleDetail.ArticleDetailActivity;
import com.khumu.android.data.Article;
import com.khumu.android.data.BookmarkArticle;
import com.khumu.android.data.LikeArticle;
import com.khumu.android.databinding.LayoutArticleItemBinding;
import com.khumu.android.repository.BookmarkArticleRepository;
import com.khumu.android.repository.LikeArticleRepository;

import java.util.List;

import javax.inject.Inject;

public class GrayBackgroundArticleAdapter extends ArticleAdapter{
    private final static String TAG = "GrayBackgroundArticleAdapter";

    public GrayBackgroundArticleAdapter(String toolbarTitle, List<Article> articleList, Context context) {
        super(toolbarTitle, articleList, context);
    }

    @NonNull
    @Override
    public GrayBackgroundArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutArticleItemBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_article_item, parent, false);
        return new GrayBackgroundArticleAdapter.GrayBackgroundArticleViewHolder(binding, this.context);
    }

    public class GrayBackgroundArticleViewHolder extends ArticleAdapter.ArticleViewHolder {
        public GrayBackgroundArticleViewHolder(LayoutArticleItemBinding binding, Context context) {
            super(binding, context);
            // 이렇게 배경색을 오버라이딩하려고 상속함.
            binding.articleLeftLayout.setBackground(context.getDrawable(R.drawable.background_feed_article_white));
        }
    }
}
