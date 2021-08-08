package com.khumu.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.khumu.android.R;
import com.khumu.android.data.Article;
import com.khumu.android.databinding.LayoutArticleItemBinding;

import java.util.List;

public class HotBoardArticleAdapter extends ArticleAdapter{
    private final static String TAG = "GrayBackgroundArticleAdapter";

    public HotBoardArticleAdapter(String toolbarTitle, List<Article> articleList, Context context) {
        super(toolbarTitle, articleList, context);
    }

    @NonNull
    @Override
    public HotBoardArticleAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutArticleItemBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_article_item, parent, false);
        return new HotBoardArticleAdapter.HotBoardArticleAdapterViewHolder(binding, this.context);
    }

    public class HotBoardArticleAdapterViewHolder extends ArticleViewHolder {
        public HotBoardArticleAdapterViewHolder(LayoutArticleItemBinding binding, Context context) {
            super(binding, context);
            // 이렇게 배경색을 오버라이딩하려고 상속함.
            binding.articleItemFooterLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.background_feed_article_meta_bordered_white_red_500));
            binding.articleLeftLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.background_feed_article_white));
        }
    }
}
