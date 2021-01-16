package com.khumu.android.myPage;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.R;
import com.khumu.android.data.ArticleTag;
import com.khumu.android.databinding.LayoutArticleTagItemBinding;

import java.util.List;

public class ArticleTagAdapter extends RecyclerView.Adapter<ArticleTagAdapter.ArticleTagViewHolder>{
    private List<ArticleTag> articleTagList;

    public ArticleTagAdapter(List<ArticleTag> articleTagList) {
        this.articleTagList = articleTagList;
    }

    @Override
    public ArticleTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutArticleTagItemBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()),R.layout.layout_article_tag_item,  parent, false);
        return new ArticleTagViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleTagViewHolder holder, int position) {
        holder.bind(articleTagList.get(position));
    }

    @Override
    public int getItemCount() {
        return articleTagList.size();
    }

    public class ArticleTagViewHolder extends RecyclerView.ViewHolder{
        public TextView tagNameTV;
        private LayoutArticleTagItemBinding binding;
        public ArticleTagViewHolder(LayoutArticleTagItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // 이 data를 이용해 view holder를 bind 한다.
        void bind(ArticleTag tag){
            binding.setArticleTag(tag);
        }
    }
}
