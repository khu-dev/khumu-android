package com.khumu.android.articleDetail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.Comment;
import com.khumu.android.databinding.ReplyCommentItemBinding;
import com.khumu.android.repository.CommentRepository;
import com.khumu.android.repository.CommentService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {
    private final static String TAG = "ReplyAdapter";

    @Inject
    public CommentRepository commentRepository;
    @Inject
    public CommentService commentService;

    public CommentViewModel commentViewModel;
    public List<Comment> replyList;
    public Context context;
    private ArticleDetailFragment articleDetailFragment;

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {
        private ReplyCommentItemBinding binding;
        private Context context;
        private CommentViewModel commentViewModel;
        private ArticleDetailFragment articleDetailFragment;

        public ReplyViewHolder(ReplyCommentItemBinding binding, Context context, ArticleDetailFragment articleDetailFragment, CommentViewModel commentViewModel) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
            this.articleDetailFragment = articleDetailFragment;
            this.commentViewModel = commentViewModel;
        }

        public void bind(Comment reply) {
            this.binding.setReply(reply);
            this.binding.setViewHolder(this);
        }

        public Drawable getLikedIcon() {
            return binding.getReply().isLiked() ? context.getDrawable(R.drawable.ic_comment_like_true) : context.getDrawable(R.drawable.ic_reply_like_false);
        }
    }


    public ReplyAdapter(List<Comment> ReplyList, Context context, CommentViewModel commentViewModel, ArticleDetailFragment articleDetailFragment) {
        KhumuApplication.applicationComponent.inject(this);
        this.commentViewModel = commentViewModel;
        this.context = context;
        this.replyList = ReplyList;
        this.articleDetailFragment = articleDetailFragment;
    }

    @NonNull
    @Override
    public ReplyAdapter.ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReplyCommentItemBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()), R.layout.reply_comment_item, parent, false);
        //ReplyViewHolder holder = new ReplyViewHolder(view);

        return new ReplyAdapter.ReplyViewHolder(binding, context, articleDetailFragment, commentViewModel);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        Comment reply = replyList.get(position);
        holder.bind(reply);
        holder.binding.replyItemLikeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            if (holder.binding.getReply().getIsAuthor()) {
                                throw new Exception("자신의 대댓글에는 좋아요를 누를 수 없습니다");
                            }
                            commentViewModel.likeComment(holder.binding.getReply().getId());
                            boolean liked = holder.binding.getReply().isLiked();
                            if (liked) {
                                holder.binding.getReply().setLiked(false);
                                holder.binding.getReply().setLikeCommentCount(holder.binding.getReply().getLikeCommentCount() - 1);
                            } else {
                                holder.binding.getReply().setLiked(true);
                                holder.binding.getReply().setLikeCommentCount(holder.binding.getReply().getLikeCommentCount() + 1);
                            }
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    holder.binding.replyItemLikeIcon.setImageDrawable(holder.getLikedIcon());
                                    holder.binding.replyItemLikeCountTv.setText(String.valueOf(holder.binding.getReply().getLikeCommentCount()));
                                }
                            }, 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        holder.binding.wrapperCommentItemContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(reply.getIsAuthor()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("대댓글을 삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            commentViewModel.deleteComment(reply.getId());
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
                    Toast.makeText(context, "대댓글은 작성자만 삭제할 수 있습니다", Toast.LENGTH_SHORT).show();
                }
                return false;
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
            return R.drawable.ic_comment_like_true;
        }
        return R.drawable.ic_reply_like_false;
    }
}
