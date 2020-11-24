package com.khumu.android.feed;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;
import com.khumu.android.data.RecentBoard;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.LikeArticleRepository;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

public class BoardAdapter extends ArrayAdapter<Board> {
    private final static String TAG = "BoardAdapter";
    public List<Board> boards;

    @Inject public ArticleRepository articleRepository;

    private String selectedBoardDisplayName = "국제캠퍼스";

    public BoardAdapter(@NonNull Context context, int resource, List<Board> boards) {
        // 세 번째 인자가 이 adpater의 collection을 의미
        super(context, resource, boards);
        KhumuApplication.container.inject(this);
        this.boards = boards;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView: here");
        View view = convertView;
        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feed_board_item, parent, false);
        }
        Board board = boards.get(position);
        TextView boardNameTV = (TextView)view.findViewById(R.id.feed_board_item_display_name);
        System.out.println(board.getDisplayName());
        if(board.getDisplayName().equals(getSelectedBoardDisplayName())){
            boardNameTV.setTextColor(parent.getContext().getColor(R.color.black));
        } else{
            boardNameTV.setTextColor(parent.getContext().getColor(R.color.colorMuted));
        }
        boardNameTV.setText(board.getDisplayName());
        if(board instanceof RecentBoard){
            boardNameTV.setOnClickListener(new RecentBoardClickListener());
        } else{
            boardNameTV.setOnClickListener(new NormalBoardClickListener(board.getName(), board.getDisplayName()));
        }
        return view;
    }

    // DB상에는 존재하지 않지만 "최근게시판"으로서  Article을 가져온다.
    private class RecentBoardClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            new Thread(){
                @Override
                public void run() {
                    try {
                        articleRepository.ListArticle();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
//
    // DB상에 존재하는 일반적인 보드의 Article을 가져온다.
    private class NormalBoardClickListener implements View.OnClickListener{
        private String boardName;
        private String boardDisplayName;
        public NormalBoardClickListener(String boardName, String boardDisplayName) {
            this.boardName = boardName;
            this.boardDisplayName = boardDisplayName;
        }

        @Override
        public void onClick(View v) {
            new Thread(){
                @Override
                public void run() {
                    try {
                        articleRepository.ListArticle(1, NormalBoardClickListener.this.boardName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    public String getSelectedBoardDisplayName() {
        return selectedBoardDisplayName;
    }

    public void setSelectedBoardDisplayName(String selectedBoardDisplayName) {
        this.selectedBoardDisplayName = selectedBoardDisplayName;
    }
}
