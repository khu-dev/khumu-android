package com.khumu.android.articleDetail;

import android.content.Context;
import android.os.Looper;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.Comment;
import com.khumu.android.data.LikeComment;
import com.khumu.android.repository.LikeArticleRepository;
import com.khumu.android.repository.LikeCommentRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final static String TAG = "CommentAdapter";
    public List<Comment> commentList;
    @Inject
    public LikeCommentRepository likeCommentRepository;
    private Context context;
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView commentAuthorNicknameTV;
        public TextView commentContentTV;
        public TextView commentLikeCountTV;
        public ImageView commentLikeIcon;
        public TextView commentCreatedAtTV;

        public CommentViewHolder(@NonNull View view) {
            super(view);
            System.out.println(view);
            this.commentAuthorNicknameTV = view.findViewById(R.id.comment_item_author_nickname_tv);
            this.commentContentTV = view.findViewById(R.id.comment_item_content_tv);
            this.commentLikeCountTV = view.findViewById(R.id.comment_item_like_count_tv);
            this.commentLikeIcon = view.findViewById(R.id.comment_item_like_icon);
            this.commentCreatedAtTV = view.findViewById(R.id.comment_item_created_at_tv);
        }
    }

    public CommentAdapter(ArrayList<Comment> commentList) {
        KhumuApplication.container.inject(this);
        this.context = context;
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
        Comment comment = commentList.get(position);
        holder.commentAuthorNicknameTV.setText(comment.getAuthor().getNickname());
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
                // 현기 : Network 작업시 새 쓰레드 필요
                new Thread() {
                    @Override
                    public void run() {
                        try{
                            likeCommentRepository.toggleLikeComment(new LikeComment(comment.getID()));
                            boolean liked = comment.isLiked();
                            if(liked) {
                                comment.setLiked(false);
                                comment.setLikeCommentCount(comment.getLikeCommentCount() - 1);
                            }
                            else {
                                comment.setLiked(true);
                                comment.setLikeCommentCount(comment.getLikeCommentCount() + 1);
                            }
                            // Network 쓰레드에서 작업 수행 후 MainThread에서 UI 작업을 post
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    holder.commentLikeIcon.setImageResource(getCommentLikedImage(comment));
                                    holder.commentLikeCountTV.setText(String.valueOf(comment.getLikeCommentCount()));
                                }
                            });
                        } catch(LikeCommentRepository.BadRequestException e) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();;
                        }

                    }
                }.start();

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


