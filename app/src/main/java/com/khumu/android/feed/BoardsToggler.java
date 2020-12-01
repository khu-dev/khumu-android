package com.khumu.android.feed;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.khumu.android.R;
import com.khumu.android.util.Util;

public class BoardsToggler implements View.OnClickListener{
    View boardsView;
    ViewGroup toggleBoardsWrapper;
    public BoardsToggler(View boardsView, ViewGroup toggleBoardsWrapper) {
        this.boardsView = boardsView;
        this.toggleBoardsWrapper = toggleBoardsWrapper;
    }

    public void expand(){
        Util.expandView(boardsView);
        ((ImageView)toggleBoardsWrapper.findViewById(R.id.toggle_boards_btn)).setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
    }
    public void collapse(){
        Util.collapseView(boardsView);
        ((ImageView)toggleBoardsWrapper.findViewById(R.id.toggle_boards_btn)).setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
    }
    public void toggle(){
        if (boardsView.getVisibility()==View.GONE) expand();
        else collapse();
    }
    @Override
    public void onClick(View v) {
        toggle();
    }
}