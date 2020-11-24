package com.khumu.android.feed;

import android.view.View;
import android.widget.ImageView;

import com.khumu.android.R;
import com.khumu.android.util.Util;

public class BoardsToggler implements View.OnClickListener{
    View boardsView;
    ImageView toggleBoardsBTN;
    public BoardsToggler(View boardsView, ImageView toggleBoardsBTN) {
        this.boardsView = boardsView;
        this.toggleBoardsBTN = toggleBoardsBTN;
    }

    public void expand(){
        Util.expandView(boardsView);
        toggleBoardsBTN.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
    }
    public void collapse(){
        Util.collapseView(boardsView);
        toggleBoardsBTN.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
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