package com.khumu.android.feed;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
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
import com.khumu.android.data.BookmarkArticle;
import com.khumu.android.data.LikeArticle;
import com.khumu.android.databinding.LayoutArticleItemBinding;
import com.khumu.android.databinding.LayoutArticleTagItemBinding;
import com.khumu.android.myPage.ArticleTagAdapter;
import com.khumu.android.repository.BookmarkArticleRepository;
import com.khumu.android.repository.LikeArticleRepository;
import com.khumu.android.usecase.ArticleUseCase;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>{
    private final static String TAG = "ArticleAdapter";
    public List<Article> articleList;
    @Inject
    public LikeArticleRepository likeArticleRepository;
    @Inject
    public BookmarkArticleRepository bookmarkArticleRepository;
    @Inject
    public ArticleUseCase articleUseCase;
    // Adapter는 바깥 UI 상황을 최대한 모르고싶지만, Toast를 위해 context를 주입함.
    private Context context;

    public ArticleAdapter(List<Article> articleList, Context context) {
        KhumuApplication.container.inject(this);
        this.context = context;
        this.articleList = articleList;
    }

    @Override
    public long getItemId(int position) {
        return articleList.get(position).getID();
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutArticleItemBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()),R.layout.layout_article_item,  parent, false);
        return new ArticleAdapter.ArticleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.bind(article);
        if(articleUseCase.amIAuthor(article)){
            holder.binding.articleItemAuthorNicknameTv.setTextColor(context.getColor(R.color.red_300));
        } else{
            holder.binding.articleItemAuthorNicknameTv.setTextColor(context.getColor(R.color.gray_500));
        }
//
        holder.binding.articleItemLikeIcon.setImageResource(getArticleLikedImage(article));
        holder.binding.articleItemBookmarkIcon.setImageResource(getArticleBookmarkedImage(article));

        holder.itemView.setTag(position); // ?
        holder.binding.articleItemBodyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ArticleDetailActivity.class);
                // intent에서 해당 article에 대한 정보들을 저장
                intent.putExtra("article", article);
                v.getContext().startActivity(intent);
            }
        });

        holder.binding.articleItemLikeWrapperLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            // Network thread 에서 작업 수행 후 MainThread에 UI 작업을 Post
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    holder.binding.articleItemLikeIcon.setImageResource(getArticleLikedImage(article));
                                }
                            });
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

        holder.binding.articleItemBookmarkWrapperLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        try{
                            bookmarkArticleRepository.toggleBookmarkArticle(new BookmarkArticle(article.getID()));
                            boolean bookmarked = article.isBookmarked();
                            if(bookmarked){
                                article.setBookmarked(false);
                                article.setBookmarkArticleCount(article.getBookmarkArticleCount() - 1);
                            } else{
                                article.setBookmarked(true);
                                article.setBookmarkArticleCount(article.getBookmarkArticleCount() + 1);
                            }
                            // Network thread 에서 작업 수행 후 MainThread에 UI 작업을 Post
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    holder.binding.articleItemBookmarkIcon.setImageResource(getArticleBookmarkedImage(article));
                                }
                            });
                        } catch (BookmarkArticleRepository.BadRequestException e){
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

    private int getArticleBookmarkedImage(Article article){
        if(article.isBookmarked()){
            return R.drawable.ic_filled_bookmark;
        }
        return R.drawable.ic_empty_bookmark;
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        public LayoutArticleItemBinding binding;
        // 이 view는 아마도 recycler view?
        private TextView articleAuthorNicknameTV;
        public ArticleViewHolder(LayoutArticleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Article article){
            this.binding.setArticle(article);
        }
    }
}
