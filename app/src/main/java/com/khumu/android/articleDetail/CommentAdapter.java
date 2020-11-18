package com.khumu.android.articleDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.R;
import com.khumu.android.data.Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private ArrayList<Comment> commentList;
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView commentAuthorUsernameTV;
        public TextView commentContentTV;
        public CommentViewHolder(@NonNull View view) {
            super(view);
            System.out.println(view);
            this.commentAuthorUsernameTV = (TextView) view.findViewById(R.id.comment_item_author_username_tv);
            this.commentContentTV = (TextView) view.findViewById(R.id.comment_item_content_tv);
        }
    }

    public CommentAdapter(ArrayList<Comment> commentList) {
        this.commentList = commentList;
    }


    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.commentAuthorUsernameTV.setText(commentList.get(position).getAuthorUsername());
        holder.commentContentTV.setText(commentList.get(position).getContent());

        holder.itemView.setTag(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove(holder.getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList == null ? 0 : commentList.size();
    }

    public void remove(int position) {
        try {
            commentList.remove(position);
            // 새로고침
            notifyItemRemoved(position);
        } catch(IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }
}


