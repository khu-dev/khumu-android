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
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.articleDetail.ArticleDetailActivity;
import com.khumu.android.data.Article;
import com.khumu.android.data.BookmarkArticle;
import com.khumu.android.data.LikeArticle;
import com.khumu.android.databinding.LayoutArticleItemBinding;
import com.khumu.android.repository.BookmarkArticleRepository;
import com.khumu.android.repository.LikeArticleRepository;
import com.khumu.android.util.Util;

import java.util.List;

import javax.inject.Inject;

public class    ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>{
    private final static String TAG = "ArticleAdapter";
    // Article Detail Activity를 열 때 툴바에 어떤 제목을 표시할 것인가
    private String toolbarTitle = "게시글";
    public List<Article> articleList;
    @Inject
    public LikeArticleRepository likeArticleRepository;
    @Inject
    public BookmarkArticleRepository bookmarkArticleRepository;
    // Adapter는 바깥 UI 상황을 최대한 모르고싶지만, Toast를 위해 context를 주입함.
    protected Context context;

    public ArticleAdapter(String toolbarTitle, List<Article> articleList, Context context) {
        KhumuApplication.applicationComponent.inject(this);
        this.toolbarTitle = toolbarTitle;
        this.context = context;
        this.articleList = articleList;
    }

    @Override
    public long getItemId(int position) {
        return articleList.get(position).getId();
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutArticleItemBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()),R.layout.layout_article_item,  parent, false);
        return new ArticleAdapter.ArticleViewHolder(binding, this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.bind(article);

        if (!article.getImages().isEmpty()) {
            Glide.with(this.context)
                    .load(Util.getDriveRootUrl() + "/thumbnail/" + article.getImages().get(0))
                    .transform(new CenterCrop(), new RoundedCorners(13))
                    .into(holder.binding.articleItemThumbnailIv);
        }


        holder.itemView.setTag(position); // ?
        holder.binding.articleItemBodyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ArticleDetailActivity.class);
                // intent에서 해당 article에 대한 정보들을 저장
                intent.putExtra("article", article);
                intent.putExtra("toolbarBoardTitle", toolbarTitle);
                v.getContext().startActivity(intent);
            }
        });
        holder.binding.articleItemBookmarkWrapperLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList == null ? 0 : articleList.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        private LayoutArticleItemBinding binding;
        private Context context;
        public ArticleViewHolder(LayoutArticleItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        public void bind(Article article){
            this.binding.setArticle(article);
            this.binding.setViewHolder(this);
        }

        public int getThumbnailImageVisibility(){
            Article article = this.binding.getArticle();
            if (article.getImages().isEmpty()) {
                return View.GONE;
            } else if (article.getImages().size() == 1){
                return View.VISIBLE;
            } else{
                return View.VISIBLE;
            }
        }
        public int getThumbnailCountVisibility(){
            Article article = this.binding.getArticle();
            if (article.getImages().isEmpty()) {
                return View.GONE;
            } else if (article.getImages().size() == 1){
                return View.GONE;
            } else{
                return View.VISIBLE;
            }
        }

        public float getThumbnailImageAlpha(){
            Log.d(TAG, "getThumbnailImageAlpha: " + this.binding.getArticle().getImages().size());
            if(this.binding.getArticle().getImages().size() >= 2){
                return (float) 0.5;
            }
            return (float) 1;
        }

        public String getThumbnailCountText(){
            return "+" + (this.binding.getArticle().getImages().size()-1);
        }

        public int getAuthorNicknameColor(){
            if(this.binding.getArticle().getIsAuthor()){
                return R.color.red_300;
            }
            return R.color.gray_500;
        }
    }
}
