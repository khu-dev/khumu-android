package com.khumu.android.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.R;
import com.khumu.android.data.ArticleTag;

import java.util.List;

public class ArticleTagAdapter extends RecyclerView.Adapter<ArticleTagAdapter.ArticleTagViewHolder>{
    private List<ArticleTag> articleTagList;

    public ArticleTagAdapter(List<ArticleTag> articleTagList) {
        this.articleTagList = articleTagList;
    }

    @NonNull
    @Override
    public ArticleTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_base_tag_item, parent, false);
        ArticleTagAdapter.ArticleTagViewHolder holder = new ArticleTagAdapter.ArticleTagViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleTagViewHolder holder, int position) {
        holder.tagNameTV.setText(articleTagList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return articleTagList.size();
    }

    public class ArticleTagViewHolder extends RecyclerView.ViewHolder{
        public TextView tagNameTV;
        public ArticleTagViewHolder(@NonNull View itemView) {
            super(itemView);
            BaseTag tagView = (BaseTag) itemView;
            tagNameTV = (TextView) itemView.findViewWithTag("tag_name_tv");
        }
    }
}
