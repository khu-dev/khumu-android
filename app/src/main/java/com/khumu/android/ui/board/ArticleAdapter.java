package com.khumu.android.ui.board;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.khumu.android.R;
import com.khumu.android.ui.article.detail.ArticleDetailActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Timer;

import static android.widget.Toast.LENGTH_LONG;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>{
    private ArrayList<ArticleData> articleDataList;
    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        public TextView articleTitleTV;
        public TextView articleContentTV;
        public TextView articleAuthorUsernameTV;
        public TextView articleCommentCountTV;
        // 이 view는 아마도 recycler view?
        public ArticleViewHolder(View view) {
            super(view);
            System.out.println(view);
            this.articleTitleTV = view.findViewById(R.id.article_title_tv);
            this.articleContentTV = view.findViewById(R.id.article_content_tv);
            this.articleAuthorUsernameTV = view.findViewById(R.id.article_author_username_tv);
            this.articleCommentCountTV = view.findViewById(R.id.article_comment_count_tv);
        }

    }

    public ArticleAdapter(ArrayList<ArticleData> articleDataList) {
        this.articleDataList = articleDataList;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // attach to root를 하지 않을 것이기 때문에 parent인 recyclerview를 전달하지 않아도 된다(null).
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wrapper_article_item, parent, false);
        ArticleViewHolder holder = new ArticleViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        holder.articleTitleTV.setText(articleDataList.get(position).getTitle());
        holder.articleContentTV.setText(articleDataList.get(position).getContent());
        holder.articleAuthorUsernameTV.setText(articleDataList.get(position).getAuthorUsername());
        holder.articleCommentCountTV.setText("댓글 " + articleDataList.get(position).getCommentCount());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ArticleDetailActivity.class);
                // intent에서 해당 article에 대한 정보들을 저장
                intent.putExtra("articleID", articleDataList.get(position).getID());
                intent.putExtra("articleTitle", articleDataList.get(position).getTitle());
                intent.putExtra("articleContent", articleDataList.get(position).getContent());
                intent.putExtra("articleCommentCount", articleDataList.get(position).getCommentCount());
                intent.putExtra("articleAuthorUsername", articleDataList.get(position).getAuthorUsername());

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
        return articleDataList == null ? 0 : articleDataList.size();
    }

    public void remove(int position){
        try{
            articleDataList.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }
}
