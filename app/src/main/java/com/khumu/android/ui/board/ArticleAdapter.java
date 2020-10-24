package com.khumu.android.ui.board;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.khumu.android.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Timer;

import static android.widget.Toast.LENGTH_LONG;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>{
    private ArrayList<ArticleData> articleDataList;
    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_article_title;
        protected TextView tv_article_content;

        public ArticleViewHolder(View view) {
            super(view);
            this.tv_article_title = view.findViewById(R.id.tv_article_title);
            this.tv_article_content = view.findViewById(R.id.tv_article_content);

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
        holder.tv_article_title.setText(articleDataList.get(position).getTitle());
        holder.tv_article_content.setText(articleDataList.get(position).getContent());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curName = holder.tv_article_title.getText().toString();
                Toast.makeText(v.getContext(), curName, LENGTH_LONG);
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
