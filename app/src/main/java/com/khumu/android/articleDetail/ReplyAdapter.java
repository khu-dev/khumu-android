package com.khumu.android.articleDetail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {
    private final static String TAG = "ReplyAdapter";

    @Inject
    public CommentRepository commentRepository;
    public CommentViewModel commentViewModel;
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

    public ReplyAdapter(ArrayList<Comment> ReplyList, Context context, CommentViewModel commentViewModel) {
        KhumuApplication.container.inject(this);
        this.commentViewModel = commentViewModel;
        this.context = context;
        this.replyList = ReplyList;
    }

    @NonNull
    @Override
    public ReplyAdapter.ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent .getContext()).inflate(R.layout.reply_comment_item, parent, false);
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
                if(reply.isAuthor()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("댓글을 삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            commentViewModel.DeleteComment(reply.getId());
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    Toast.makeText(context, "댓글은 작성자만 삭제할 수 있습니다", Toast.LENGTH_SHORT).show();
                }
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
                            commentRepository.toggleLikeComment(reply.getId());
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
