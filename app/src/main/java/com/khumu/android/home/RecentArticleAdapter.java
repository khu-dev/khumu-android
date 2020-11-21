package com.khumu.android.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.articleDetail.ArticleDetailActivity;
import com.khumu.android.data.Article;
import com.khumu.android.data.LikeArticle;
import com.khumu.android.repository.LikeArticleRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecentArticleAdapter extends ArrayAdapter<Article> {
    private final static String TAG = "RecentArticleAdapter";
    private List<Article> articles;
    @Inject
    public LikeArticleRepository likeArticleRepository;

    public RecentArticleAdapter(@NonNull Context context, int resource, List<Article> articles) {
        // 세 번째 인자가 이 adpater의 collection을 의미
        super(context, resource, articles);
        this.articles = articles;
    }

    @Override
    public long getItemId(int position) {
        return articles.get(position).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView: here");
        View view = convertView;
        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_recent_article_item, parent, false);
        }
        Article article = articles.get(position);
        ((TextView)view.findViewById(R.id.home_recent_article_board_name_tv)).setText(article.getBoard());
        ((TextView)view.findViewById(R.id.home_recent_article_title_tv)).setText(article.getTitle());
        return view;
    }

    public class RecentArticleViewHolder {
        public TextView boardNameTV;
        public TextView articleTitleTV;
        public TextView articleCreatedAtTV;
        // 이 view는 아마도 recycler view?
        public RecentArticleViewHolder(View convertView) {
            this.boardNameTV = boardNameTV;
            this.articleTitleTV = articleTitleTV;
            this.articleCreatedAtTV = articleCreatedAtTV;
        }
    }

}
