package com.khumu.android.articleDetail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.Comment;
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
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView commentAuthorNicknameTV;
        public TextView commentContentTV;
        public TextView commentLikeCountTV;
        public ImageView commentLikeIcon;
        public TextView commentCreatedAtTV;
        public TextView commentReplyCountTV;
        public ImageView writeReplyIcon;
        public RecyclerView replyRecyclerView;
        public ReplyAdapter replyAdapter;
        public ConstraintLayout commentItemBody;
        private ArrayList<Comment> replyArrayList;
        private LinearLayoutManager linearLayoutManager;


        public CommentViewHolder(@NonNull View view) {
            super(view);
            this.replyRecyclerView = view.findViewById(R.id.recycler_view_reply_list);
            this.commentAuthorNicknameTV = view.findViewById(R.id.comment_item_author_nickname_tv);
            this.commentContentTV = view.findViewById(R.id.comment_item_content_tv);
            this.commentLikeCountTV = view.findViewById(R.id.comment_item_like_count_tv);
            this.commentLikeIcon = view.findViewById(R.id.comment_item_like_icon);
            this.commentCreatedAtTV = view.findViewById(R.id.comment_item_created_at_tv);
            this.commentReplyCountTV = view.findViewById(R.id.comment_item_reply_count_tv);
            this.writeReplyIcon = view.findViewById(R.id.comment_item_reply_icon);
            this.commentItemBody = view.findViewById(R.id.comment_item_body);
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view);

        commentViewHolder = holder;

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.linearLayoutManager = new LinearLayoutManager(context);
        holder.replyArrayList = new ArrayList<>();
        if(!comment.getChildren().isEmpty())
            holder.replyArrayList.addAll(comment.getChildren());
        holder.replyAdapter = new ReplyAdapter(holder.replyArrayList, context, commentViewModel);
        holder.replyRecyclerView.setAdapter(holder.replyAdapter);
        holder.replyRecyclerView.setLayoutManager(holder.linearLayoutManager);
        holder.commentAuthorNicknameTV.setText(comment.getAuthor().getNickname());
        holder.commentContentTV.setText(comment.getContent());
        holder.commentLikeCountTV.setText(String.valueOf(comment.getLikeCommentCount()));
        holder.commentLikeIcon.setImageResource(getCommentLikedImage(comment));
        holder.commentCreatedAtTV.setText(comment.getCommentCreatedAt());
        holder.commentReplyCountTV.setText(String.valueOf(comment.getChildren().size()));
        holder.commentItemBody.setTag(position);
        holder.commentItemBody.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(comment.isAuthor()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("댓글을 삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 삭제되기 전에 ListComment되는 것을 막기 위해 쓰레드를 잠시 멈춘다음에 ListComment를 해준다.
                            commentViewModel.deleteComment(comment.getId());
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
                }
                else {
                    Toast.makeText(context, "댓글은 작성자만 삭제할 수 있습니다", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        holder.commentItemBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("대댓글을 작성하시겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.commentAuthorNicknameTV.setTextColor(Color.WHITE);
                        holder.commentContentTV.setTextColor(Color.WHITE);
                        holder.commentCreatedAtTV.setTextColor(Color.WHITE);
                        holder.commentLikeCountTV.setTextColor(Color.WHITE);
                        holder.commentReplyCountTV.setTextColor(Color.WHITE);
                        holder.commentItemBody.setBackground(ContextCompat.getDrawable(context, R.drawable.background_comment_body_writing));
                        articleDetailFragment.setPosition(position);
                        articleDetailFragment.setCommentToWrite(comment);
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
        });
        holder.commentLikeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현기 : Network 작업시 새 쓰레드 필요
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            if (comment.isAuthor()) {
                                throw new Exception("자신의 댓글에는 좋아요를 누를 수 없습니다");
                            }
                            commentViewModel.likeComment(comment.getId());
                            boolean liked = comment.isLiked();
                            if (liked) {
                                comment.setLiked(false);
                                comment.setLikeCommentCount(comment.getLikeCommentCount() - 1);
                            } else {
                                comment.setLiked(true);
                                comment.setLikeCommentCount(comment.getLikeCommentCount() + 1);
                            }
                            // Network 쓰레드에서 작업 수행 후 MainThread에서 UI 작업을 post
                            holder.commentLikeIcon.setImageResource(getCommentLikedImage(comment));
                            holder.commentLikeCountTV.setText(String.valueOf(comment.getLikeCommentCount()));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }, 0);
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

    private int getCommentLikedImage(Comment comment) {
        if(comment.isLiked()) {
            return R.drawable.ic_filled_heart;
        }
        return R.drawable.ic_bordered_red_500_heart;
    }

    public Comment getCommentToWrite() {
        return commentToWrite;
    }

    public void setCommentToWrite(Comment comment) {
        this.commentToWrite = comment;
    }
}


