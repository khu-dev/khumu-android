package com.khumu.android.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.khumu.android.R;
import com.khumu.android.data.Article;

import java.util.List;

public class RecentArticleAdapter extends ArrayAdapter<Article> {
    private final static String TAG = "RecentArticleAdapter";
    private List<Article> articles;

    public RecentArticleAdapter(@NonNull Context context, int resource, List<Article> articles) {
        // 세 번째 인자가 이 adpater의 collection을 의미
        super(context, resource, articles);
        this.articles = articles;
    }

    @Override
    public long getItemId(int position) {
        return articles.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_recent_article_item, parent, false);
        }
        Article article = articles.get(position);
        ((TextView)view.findViewById(R.id.home_recent_article_board_name_tv)).setText(article.getBoardDisplayName());
        ((TextView)view.findViewById(R.id.home_recent_article_title_tv)).setText(article.getTitle());
        ((TextView)view.findViewById(R.id.home_recent_article_created_at_tv)).setText(article.getCreatedAt());
        return view;
    }
// 없어도 될 듯
//    public class RecentArticleViewHolder {
//        public TextView boardNameTV;
//        public TextView articleTitleTV;
//        public TextView articleCreatedAtTV;
//        // 이 view는 아마도 recycler view?
//        public RecentArticleViewHolder(View convertView) {
//            this.boardNameTV = boardNameTV;
//            this.articleTitleTV = articleTitleTV;
//            this.articleCreatedAtTV = articleCreatedAtTV;
//        }
//    }
}
