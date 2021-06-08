package com.khumu.android.boardList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.Board;
import com.khumu.android.databinding.LayoutBoardListItemBinding;
import com.khumu.android.feed.SingleBoardFeedActivity;
import com.khumu.android.repository.BoardService;

import java.util.List;

import javax.inject.Inject;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder>{
    private final static String TAG = "BoardAdapter";
    public List<Board> boardList;
    private Context context;
    @Inject
    public BoardService boardService;
    public BoardViewModel boardViewModel;

    public BoardAdapter(List<Board> boardList, Context context, BoardViewModel boardViewModel) {
        KhumuApplication.applicationComponent.inject(this);
        this.context = context;
        this.boardList = boardList;
        this.boardViewModel = boardViewModel;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutBoardListItemBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_board_list_item, parent, false);
        return new BoardAdapter.BoardViewHolder(binding, this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        Board board = boardList.get(position);
        holder.bind(board);

        holder.binding.boardListItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SingleBoardFeedActivity.class);
                intent.putExtra("board", board);
                v.getContext().startActivity(intent);
            }
        });
        holder.binding.followBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (board.getFollowed() == false) {
                    boardViewModel.followBoard(board.getDisplayName());
                } else {
                    boardViewModel.unfollowBoard(board.getDisplayName());
                }
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return boardList == null ? 0 : boardList.size();
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        private LayoutBoardListItemBinding binding;
        private Context context;
        public BoardViewHolder(LayoutBoardListItemBinding binding, Context context) {
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

//        public void onClickFollowBt(View v) {
//            boardViewModel.
//        }
    }


}
