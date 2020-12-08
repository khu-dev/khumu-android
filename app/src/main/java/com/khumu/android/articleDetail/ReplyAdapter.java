package com.khumu.android.articleDetail;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
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
import com.khumu.android.repository.CommentRepository;
import com.khumu.android.repository.LikeCommentRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {
    private final static String TAG = "ReplyAdapter";

    @Inject
    public LikeCommentRepository likeCommentRepository;
    @Inject
    public CommentRepository commentRepository;

    public List<Comment> replyList;

    private Context context;
    public class ReplyViewHolder extends RecyclerView.ViewHolder {
        public TextView replyAuthorNicknameTV;
        public TextView replyContentTV;
        public TextView replyLikeCountTV;
        public ImageView replyLikeIcon;
        public TextView replyCreatedAtTV;

        public ReplyViewHolder(@NonNull View view) {
            super(view);
            this.replyAuthorNicknameTV = view.findViewById(R.id.reply_item_author_nickname_tv);
            this.replyContentTV = view.findViewById(R.id.reply_item_content_tv);
            this.replyLikeCountTV = view.findViewById(R.id.reply_item_like_count_tv);
            this.replyLikeIcon = view.findViewById(R.id.reply_item_like_icon);
            this.replyCreatedAtTV = view.findViewById(R.id.reply_item_created_at_tv);
        }
    }

    public ReplyAdapter(ArrayList<Comment> ReplyList, Context context)    {
        KhumuApplication.container.inject(this);
        this.context = context;
        this.replyList = ReplyList;
    }

    @NonNull
    @Override
    public ReplyAdapter.ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_comment_item, parent, false);
        ReplyViewHolder holder = new ReplyViewHolder(view);

        return holder;
    }
    
    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        Comment reply = replyList.get(position);
        holder.replyAuthorNicknameTV.setText(reply.getAuthor().getNickname());
        holder.replyContentTV.setText(reply.getContent());
        holder.replyLikeCountTV.setText(String.valueOf(reply.getLikeCommentCount()));
        holder.replyLikeIcon.setImageResource(getReplyLikedImage(reply));
        holder.replyCreatedAtTV.setText(reply.getCommentCreatedAt());
        holder.itemView.setTag(position);
        
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove(holder.getAdapterPosition());
                return false;
            }
        });

        holder.replyLikeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        try{
                            System.out.println(replyList);
                            likeCommentRepository.toggleLikeComment(new LikeComment(reply.getID()));
                            boolean liked = reply.isLiked();
                            if(liked) {
                                reply.setLiked(false);
                                reply.setLikeCommentCount(reply.getLikeCommentCount() - 1);
                            } else {
                                reply.setLiked(true);
                                reply.setLikeCommentCount(reply.getLikeCommentCount() + 1);
                            }
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    holder.replyLikeIcon.setImageResource(getReplyLikedImage(reply));
                                    holder.replyLikeCountTV.setText(String.valueOf(reply.getLikeCommentCount()));
                                }
                            });
                        } catch (LikeCommentRepository.BadRequestException e) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

    @Override
    public int getItemCount() { return replyList == null ? 0 : replyList.size(); }

    private void remove(int position) {
        try {
            replyList.remove(position);
            notifyItemRemoved(position);
        } catch(IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    private int getReplyLikedImage(Comment reply) {
        if(reply.isLiked()) {
            return R.drawable.ic_filled_heart;
        }
        return R.drawable.ic_empty_heart;
    }
}
