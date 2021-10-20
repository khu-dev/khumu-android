package com.khumu.android.articleDetail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.Comment;
import com.khumu.android.data.SimpleUser;
import com.khumu.android.databinding.LayoutCommentItemBinding;
import com.khumu.android.repository.CommentRepository;
import com.khumu.android.repository.CommentService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final static String TAG = "CommentAdapter";

    @Inject
    public CommentRepository commentRepository;
    @Inject
    public CommentService commentService;

    public List<Comment> commentList;
    public CommentViewModel commentViewModel;
    public Comment commentToWrite;
    public ArticleDetailFragment articleDetailFragment;
    private CommentViewHolder commentViewHolder;
    private Context context;

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private LayoutCommentItemBinding binding;
        private Context context;
        private CommentViewModel commentViewModel;
        private ArticleDetailFragment articleDetailFragment;
        public List<Comment> replyArrayList;

        public CommentViewHolder(LayoutCommentItemBinding binding, Context context, ArticleDetailFragment articleDetailFragment, CommentViewModel commentViewModel) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
            this.articleDetailFragment = articleDetailFragment;
            this.commentViewModel = commentViewModel;
        }

        public void bind(Comment comment){
            this.binding.setComment(comment);
            this.binding.setViewHolder(this);
        }

        public String getAuthorNickName(){
            String authorNickname = binding.getComment().getAuthor().getNickname();
            if (binding.getComment().getAuthor().getState().equals("deleted")) {
                authorNickname = "탈퇴한 유저";
            } else if (binding.getComment().getIsWrittenByArticleAuthor()) {
                authorNickname = binding.getComment().getAuthor().getNickname() + " (글쓴이)";
            }
            return authorNickname;
        }

        public Drawable getLikedIcon() {
            return binding.getComment().isLiked() ? context.getDrawable(R.drawable.ic_comment_like_true) : context.getDrawable(R.drawable.ic_comment_like_false);
        }

        public boolean onLongClick(View v) {
            if (binding.getComment().getIsAuthor()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("댓글을 삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 삭제되기 전에 ListComment되는 것을 막기 위해 쓰레드를 잠시 멈춘다음에 ListComment를 해준다.
                        commentViewModel.deleteComment(binding.getComment().getId());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        commentViewModel.listComment();
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(context, "댓글은 작성자만 삭제할 수 있습니다", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("대댓글을 작성하시겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    articleDetailFragment.setCommentToWrite(binding.getComment());
                    articleDetailFragment.setCommentHint("대댓글을 입력하세요");
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

        public void onClickLikeIcon(View view) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        if (binding.getComment().getIsAuthor()) {
                            throw new Exception("자신의 댓글에는 좋아요를 누를 수 없습니다");
                        }
                        commentViewModel.likeComment(binding.getComment().getId());
                        boolean liked = binding.getComment().isLiked();
                        if (liked) {
                            binding.getComment().setLiked(false);
                            binding.getComment().setLikeCommentCount(binding.getComment().getLikeCommentCount() - 1);

                        } else {
                            binding.getComment().setLiked(true);
                            binding.getComment().setLikeCommentCount(binding.getComment().getLikeCommentCount() + 1);
                        }
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.commentItemLikeIcon.setImageDrawable(getLikedIcon());
                                binding.commentItemLikeCountTv.setText(String.valueOf(binding.getComment().getLikeCommentCount()));
                            }
                        }, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    public CommentAdapter(ArrayList<Comment> commentList, Context context, CommentViewModel commentViewModel, ArticleDetailFragment articleDetailFragment) {
        KhumuApplication.applicationComponent.inject(this);
        this.commentViewModel = commentViewModel;
        this.context = context;
        this.commentList = commentList;
        this.commentToWrite = null;
        this.articleDetailFragment = articleDetailFragment;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCommentItemBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()),R.layout.layout_comment_item,  parent, false);
        return new CommentAdapter.CommentViewHolder(binding, context, articleDetailFragment, commentViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.bind(comment);
        if (holder.binding.getComment().getIsWrittenByArticleAuthor()) {
            holder.binding.commentItemAuthorNicknameTv.setTypeface(null, Typeface.BOLD);
        } else{
            holder.binding.commentItemAuthorNicknameTv.setTypeface(null, Typeface.NORMAL);
        }

        // TODO: TextView에 취소선 넣기 참고: https://dev-daddy.tistory.com/20
        // 동작안하네...
//        if (holder.binding.getComment().getAuthor().getState().equals("deleted")) {
//            holder.binding.commentItemAuthorNicknameTv.setPaintFlags(holder.binding.commentItemAuthorNicknameTv.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
//        } else{
//            holder.binding.commentItemAuthorNicknameTv.setPaintFlags(0);
//        }

        holder.replyArrayList = new ArrayList<>();
        if(!comment.getChildren().isEmpty())
            holder.replyArrayList.addAll(comment.getChildren());
        holder.binding.recyclerViewReplyList.setAdapter(new ReplyAdapter(holder.replyArrayList, context, commentViewModel, articleDetailFragment));
        // TODO: 태그 뭐임?
//        holder.commentItemBody.setTag(position);
    }

    @Override
    public int getItemCount() {
        return commentList == null ? 0 : commentList.size();
    }
}


