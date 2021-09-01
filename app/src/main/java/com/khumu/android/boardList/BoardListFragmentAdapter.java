package com.khumu.android.boardList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.khumu.android.data.Board;

import org.jetbrains.annotations.NotNull;

public class BoardListFragmentAdapter extends FragmentStateAdapter {
    public BoardListFragmentAdapter(Fragment fragment) {
        super(fragment);
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new BoardListFragment();
        } else if (position == 1) {
            //TODO AnnouncementFragment만들고 바꾸기
            return new BoardListFragment();
        }
        return new BoardListFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
