package com.khumu.android.articleDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.R;
import com.khumu.android.data.Comment;
import com.khumu.android.repository.LikeArticleRepository;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private ArrayList<Comment> commentList;
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView commentAuthorUsernameTV;
        public TextView commentContentTV;
        public TextView commentLikeCountTV;
        public ImageView commentLikeIcon;
        public TextView commentCreatedAtTV;

        public CommentViewHolder(@NonNull View view) {
            super(view);
            System.out.println(view);
            this.commentAuthorUsernameTV = view.findViewById(R.id.comment_item_author_username_tv);
            this.commentContentTV = view.findViewById(R.id.comment_item_content_tv);
            this.commentLikeCountTV = view.findViewById(R.id.comment_item_like_count_tv);
            this.commentLikeIcon = view.findViewById(R.id.comment_item_like_icon);
            this.commentCreatedAtTV = view.findViewById(R.id.comment_item_created_at_tv);
        }
    }

    public CommentAdapter(ArrayList<Comment> commentList) {
        this.commentList = commentList;
    }


    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        System.out.println("hi");
        Comment comment = commentList.get(position);
        holder.commentAuthorUsernameTV.setText(comment.getAuthor().getUsername());
        holder.commentContentTV.setText(comment.getContent());
        holder.commentLikeCountTV.setText(String.valueOf(comment.getLikeCommentCount()));
        holder.commentLikeIcon.setImageResource(getCommentLikedImage(comment));
        holder.commentCreatedAtTV.setText(comment.getCommentCreatedAt());
        holder.itemView.setTag(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove(holder.getAdapterPosition());
                return false;
            }
        });

        holder.commentLikeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean liked = comment.isLiked();
                if(liked) {
                    comment.setLiked(false);
                    comment.setLikeCommentCount(comment.getLikeCommentCount() - 1);
                }
                else {
                    comment.setLiked(true);
                    comment.setLikeCommentCount(comment.getLikeCommentCount() + 1);
                }
                holder.commentLikeIcon.setImageResource(getCommentLikedImage(comment));
                holder.commentLikeCountTV.setText(String.valueOf(comment.getLikeCommentCount()));
                //TODO 아직 Comment를 좋아요 한 것이 database에 적용되지 않음 나갔다오면 초기화
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

    private int getCommentLikedImage(Comment comment) {
        if(comment.isLiked()) {
            return R.drawable.ic_filled_heart;
        }
        return R.drawable.ic_empty_heart;
    }

}


