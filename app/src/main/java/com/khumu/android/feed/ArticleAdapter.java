package com.khumu.android.feed;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khumu.android.R;
import com.khumu.android.articleDetail.ArticleDetailActivity;
import com.khumu.android.data.Article;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>{
    private ArrayList<Article> articleList;
    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        public TextView articleTitleTV;
        public TextView articleContentTV;
        public TextView articleAuthorUsernameTV;
        public TextView articleCommentCountTV;
        // 이 view는 아마도 recycler view?
        public ArticleViewHolder(View view) {
            super(view);
            System.out.println(view);
            this.articleTitleTV = view.findViewById(R.id.article_item_title_tv);
            this.articleContentTV = view.findViewById(R.id.article_item_content_tv);
            this.articleAuthorUsernameTV = view.findViewById(R.id.article_item_author_username_tv);
            this.articleCommentCountTV = view.findViewById(R.id.article_item_comment_count_tv);
        }
    }

    public ArticleAdapter(ArrayList<Article> articleList) {
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // attach to root를 하지 않을 것이기 때문에 parent인 recyclerview를 전달하지 않아도 된다(null).
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_article_item, parent, false);
        ArticleViewHolder holder = new ArticleViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        holder.articleTitleTV.setText(articleList.get(position).getTitle());
        holder.articleContentTV.setText(articleList.get(position).getContent());
        holder.articleAuthorUsernameTV.setText(articleList.get(position).getAuthor().getUsername());
        holder.articleCommentCountTV.setText("댓글 " + articleList.get(position).getCommentCount());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ArticleDetailActivity.class);
                // intent에서 해당 article에 대한 정보들을 저장
                intent.putExtra("articleID", articleList.get(position).getID());
                intent.putExtra("articleTitle", articleList.get(position).getTitle());
                intent.putExtra("articleContent", articleList.get(position).getContent());
                intent.putExtra("articleCommentCount", articleList.get(position).getCommentCount());
                intent.putExtra("articleAuthorUsername", articleList.get(position).getAuthor().getUsername());

                v.getContext().startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                remove(holder.getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList == null ? 0 : articleList.size();
    }

    public void remove(int position){
        try{
            articleList.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }
}
