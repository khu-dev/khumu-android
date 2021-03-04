package com.khumu.android.feed;

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
import com.khumu.android.MainActivity;
import com.khumu.android.R;
import com.khumu.android.data.Board;
import com.khumu.android.databinding.LayoutFollowingBoardItemBinding;
import com.khumu.android.login.LoginActivity;
import com.khumu.android.repository.BoardRepository;

import java.util.List;

import javax.inject.Inject;

public class FollowingBoardAdapter extends RecyclerView.Adapter<FollowingBoardAdapter.FollowingBoardViewHolder>{
    private final static String TAG = "FollowingBoardAdapter";
    public List<Board> boardList;
    @Inject
    public BoardRepository boardRepository;
    // Adapter는 바깥 UI 상황을 최대한 모르고싶지만, Toast를 위해 context를 주입함.
    private Context context;

    public FollowingBoardAdapter(List<Board> boardList, Context context) {
        this.context = context;
        this.boardList = boardList;
    }

    @Override
    public long getItemId(int position) {
        // board는 primary key가 String이라..
        // 일단은 임시로 position으로 하겠음.
        return position;
    }

    @NonNull
    @Override
    public FollowingBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutFollowingBoardItemBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()),R.layout.layout_following_board_item,  parent, false);
        return new FollowingBoardAdapter.FollowingBoardViewHolder(binding, this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingBoardViewHolder holder, int position) {
        Board board = boardList.get(position);
        holder.bind(board);
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public class FollowingBoardViewHolder extends RecyclerView.ViewHolder {
        private LayoutFollowingBoardItemBinding binding;
        private Context context;
        public FollowingBoardViewHolder(LayoutFollowingBoardItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        public void bind(Board board){
            this.binding.setFollowingBoard(board);
            this.binding.setViewHolder(this);
        }

        public void onClick(View v) {
            Intent intent = new Intent(context, SingleBoardFeedActivity.class);
            intent.putExtra("board", binding.getFollowingBoard());
            context.startActivity(intent);
        }
    }
}
