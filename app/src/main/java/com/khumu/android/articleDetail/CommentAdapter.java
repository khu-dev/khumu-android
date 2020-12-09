package com.khumu.android.articleDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
<<<<<<< Updated upstream
=======
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
>>>>>>> Stashed changes
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.R;
import com.khumu.android.data.Comment;
import com.khumu.android.repository.LikeArticleRepository;
<<<<<<< Updated upstream
=======
import com.khumu.android.repository.LikeCommentRepository;
import com.khumu.android.repository.ReplyRepository;
>>>>>>> Stashed changes

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
<<<<<<< Updated upstream

    private ArrayList<Comment> commentList;
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView commentAuthorUsernameTV;
=======
    private final static String TAG = "CommentAdapter";

    @Inject
    public LikeCommentRepository likeCommentRepository;
    @Inject
    public CommentRepository commentRepository;
    @Inject
    public ReplyRepository replyRepository;
    public List<Comment> commentList;
    public ReplyViewModel replyViewModel;
    private Context context;

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView.RecycledViewPool recycledViewPool;
        public TextView commentAuthorNicknameTV;
>>>>>>> Stashed changes
        public TextView commentContentTV;
        public TextView commentLikeCountTV;
        public ImageView commentLikeIcon;
        public TextView commentCreatedAtTV;
<<<<<<< Updated upstream

        public CommentViewHolder(@NonNull View view) {
            super(view);
            System.out.println(view);
            this.commentAuthorUsernameTV = view.findViewById(R.id.comment_item_author_username_tv);
=======
        public ImageView writeReplyIcon;
        public RecyclerView replyRecyclerView;
        public ReplyAdapter replyAdapter;
        public ArrayList<Comment> replyArrayList;
        public LinearLayoutManager linearLayoutManager;



        public CommentViewHolder(@NonNull View view) {
            super(view);
            this.recycledViewPool = new RecyclerView.RecycledViewPool();
            this.linearLayoutManager = new LinearLayoutManager(view.getContext());
            this.replyRecyclerView = view.findViewById(R.id.recycler_view_comment_list);
            this.commentAuthorNicknameTV = view.findViewById(R.id.comment_item_author_nickname_tv);
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        System.out.println("hi");
        Comment comment = commentList.get(position);
        holder.commentAuthorUsernameTV.setText(comment.getAuthor().getUsername());
=======

        Comment comment = commentList.get(position);
        replyViewModel = new ViewModelProvider((ViewModelStoreOwner) this,
                new ReplyViewFactory(replyRepository, String.valueOf(comment.getID()))
        ).get(ReplyViewModel.class);
        holder.replyArrayList = new ArrayList<>();
        holder.replyAdapter = new ReplyAdapter(holder.replyArrayList, context);
        holder.replyRecyclerView.setAdapter(holder.replyAdapter);
        holder.replyRecyclerView.setRecycledViewPool(holder.recycledViewPool);

        holder.commentAuthorNicknameTV.setText(comment.getAuthor().getNickname());
>>>>>>> Stashed changes
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


