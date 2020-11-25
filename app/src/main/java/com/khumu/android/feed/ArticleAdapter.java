package com.khumu.android.feed;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.articleDetail.ArticleDetailActivity;
import com.khumu.android.data.Article;
import com.khumu.android.data.LikeArticle;
import com.khumu.android.repository.LikeArticleRepository;
import com.khumu.android.util.Util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>{
    public List<Article> articleList;
    @Inject
    public LikeArticleRepository likeArticleRepository;
    // Adapter는 바깥 UI 상황을 최대한 모르고싶지만, Toast를 위해 context를 주입함.
    private Context context;
    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        public ViewGroup articleBodyLayout;
        public TextView articleTitleTV;
        public TextView articleContentTV;
        public TextView articleAuthorNicknameTV;
        public TextView articleCommentCountTV;
        public TextView articleLikeCountTV;
        public ImageView articleLikeIcon;
        public TextView articleCreatedAtTV;
        // 이 view는 아마도 recycler view?
        public ArticleViewHolder(View view) {
            super(view);
            this.articleBodyLayout = (ViewGroup) view.findViewById(R.id.article_body_layout);
            this.articleTitleTV = view.findViewById(R.id.article_item_title_tv);
            this.articleContentTV = view.findViewById(R.id.article_item_content_tv);
            this.articleAuthorNicknameTV = view.findViewById(R.id.article_item_author_nickname_tv);
            this.articleCommentCountTV = view.findViewById(R.id.article_item_comment_count_tv);
            this.articleLikeCountTV = view.findViewById(R.id.article_item_like_article_count_tv);
            this.articleLikeIcon = view.findViewById(R.id.article_item_like_icon);
            this.articleCreatedAtTV = view.findViewById(R.id.article_item_created_at_tv);
        }
    }

    public ArticleAdapter(List<Article> articleList, Context context) {
        KhumuApplication.container.inject(this);
        this.context = context;
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
        Article article = articleList.get(position);
        holder.articleTitleTV.setText(article.getTitle());
        holder.articleContentTV.setText(article.getContent());
        holder.articleAuthorNicknameTV.setText(article.getAuthor().getNickname());
        holder.articleLikeIcon.setImageResource(getArticleLikedImage(article));
        holder.articleLikeCountTV.setText(String.valueOf(article.getLikeArticleCount()));
        holder.articleCommentCountTV.setText(String.valueOf(article.getCommentCount()));
        holder.articleCreatedAtTV.setText(String.valueOf(article.getArticleCreatedAt()));

        holder.itemView.setTag(position);
        holder.articleBodyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ArticleDetailActivity.class);
                // intent에서 해당 article에 대한 정보들을 저장
                intent.putExtra("articleID", article.getID());
                intent.putExtra("articleTitle", article.getTitle());
                intent.putExtra("articleContent", article.getContent());
                intent.putExtra("articleCommentCount", article.getCommentCount());
                intent.putExtra("articleAuthorNickname", article.getAuthor().getNickname());
                intent.putExtra("articleCreatedAt", article.getArticleCreatedAt());
                intent.putExtra("articleLikeCount", article.getLikeArticleCount());
                intent.putExtra("articleIsLiked", article.isLiked());

                v.getContext().startActivity(intent);
            }
        });

        holder.articleLikeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.articleLikeIcon.setImageResource(getArticleLikedImage(article));
                holder.articleLikeCountTV.setText(String.valueOf(article.getLikeArticleCount()));
                new Thread(){
                    @Override
                    public void run() {
                        try{
                            likeArticleRepository.toggleLikeArticle(new LikeArticle(article.getID()));

                            boolean liked = article.isLiked();
                            if(liked){
                                article.setLiked(false);
                                article.setLikeArticleCount(article.getLikeArticleCount() - 1);
                            } else{
                                article.setLiked(true);
                                article.setLikeArticleCount(article.getLikeArticleCount() + 1);
                            }
                        } catch (LikeArticleRepository.BadRequestException e){
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        // 그냥 테스트용
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

    private int getArticleLikedImage(Article article){
        if(article.isLiked()){
            return R.drawable.ic_filled_heart;
        }
        return R.drawable.ic_empty_heart;
    }
}
