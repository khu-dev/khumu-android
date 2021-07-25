package com.khumu.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.articleDetail.ArticleDetailActivity;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;
import com.khumu.android.data.BookmarkArticle;
import com.khumu.android.data.LikeArticle;
import com.khumu.android.databinding.LayoutArticleItemBinding;
import com.khumu.android.databinding.LayoutSearchedBoardItemBinding;
import com.khumu.android.feed.SingleBoardFeedActivity;
import com.khumu.android.repository.BoardRepository;
import com.khumu.android.repository.BoardService;
import com.khumu.android.repository.BookmarkArticleRepository;
import com.khumu.android.repository.LikeArticleRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchedBoardAdapter extends RecyclerView.Adapter<SearchedBoardAdapter.BoardViewHolder>{
    private final static String TAG = "SearchedBoardAdapter";
    @NonNull public List<Board> boardList;
    @Inject
    public BoardService boardService;
    // Adapter는 바깥 UI 상황을 최대한 모르고싶지만, Toast를 위해 context를 주입함.
    protected Context context;

    public SearchedBoardAdapter(@NotNull List<Board> boardList, @NotNull Context context) {
        KhumuApplication.applicationComponent.inject(this);
        this.boardList = boardList;
        this.context = context;

    }

    @Override
    public long getItemId(int position) {
        // Board는 id가 String이라서 board의 id로 식별할 수가 없네.
        // scroll하는 게 아니라 그닥 상관 없을 듯 position을 사용해도.

        return position;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSearchedBoardItemBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()),R.layout.layout_searched_board_item,  parent, false);
        return new BoardViewHolder(binding, this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        Board board = boardList.get(position);
        holder.bind(board);

        holder.itemView.setTag(position); // ? 뭐하는 거지
        holder.binding.boardListItemLayout.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), SingleBoardFeedActivity.class);
                // intent에서 해당 article에 대한 정보들을 저장
                intent.putExtra("board", board);
                v.getContext().startActivity(intent);
        });
        holder.binding.followBt.setOnClickListener(v -> {
            if (board.followed) {
                boardService.unfollowBoard("application/json", board.getName()).enqueue(new Callback<Board>() {
                    @Override
                    public void onResponse(Call<Board> call, Response<Board> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse: 게시판 언팔로우 완료");
                        } else {
                            Log.e(TAG, "onResponse: 게시판 언팔로우 실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<Board> call, Throwable t) {
                        t.printStackTrace();
                    }
                });;
                board.setFollowed(false);
                holder.binding.followBt.setImageDrawable(context.getDrawable(holder.getFollowIcon()));
            } else {
                boardService.followBoard("application/json", board.getName()).enqueue(new Callback<Board>() {
                    @Override
                    public void onResponse(Call<Board> call, Response<Board> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse: 게시판 팔로우 완료");
                        } else {
                            Log.e(TAG, "onResponse: 게시판 팔로우 실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<Board> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                board.setFollowed(true);
                holder.binding.followBt.setImageDrawable(context.getDrawable(holder.getFollowIcon()));
            }

        });
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        private LayoutSearchedBoardItemBinding binding;
        private Context context;
        public BoardViewHolder(LayoutSearchedBoardItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        public void bind(Board board){
            this.binding.setBoard(board);
            this.binding.setViewHolder(this);
        }

        public int getFollowIcon() {
            Board board = this.binding.getBoard();
            if (board.getFollowed()) {
                return R.drawable.ic_minus;
            } else {
                return R.drawable.ic_plus;
            }
        }
    }
}
