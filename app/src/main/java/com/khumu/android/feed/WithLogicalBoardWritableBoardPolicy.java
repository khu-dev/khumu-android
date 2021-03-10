package com.khumu.android.feed;

import com.khumu.android.data.Board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;

/**
 * Policy가 계속해서 변경될 수도 있다는 상상의 개발 세계 속에서
 * 그러한 케이스에 유연히 반응하기 위한 추상화
 */
public class WithLogicalBoardWritableBoardPolicy implements WritableBoardPolicy{
    final List<String> unavailableBoardNames;
    @Inject
    public WithLogicalBoardWritableBoardPolicy() {
        unavailableBoardNames = Arrays.asList(new String[]{
                // "following"은 single board feed가 아니라
                // my feed 라서 board를 삽입시키지 않기 때문에 글쓰기 버튼이 있어도 됨.
                // single board feed 에서 글쓰기 버튼 클릭 후
                // article write activity에 해당 board에 글을 쓰도록 하기 때문에 문제가 되는 것.
                "liked", "bookmarked", "commented", "my"
        });
    }
    @Override
    public boolean isWritableBoard(Board board) {
        return unavailableBoardNames.stream().noneMatch(boardName -> boardName.equals(board.getName()));
    }

    @Override
    public boolean isWritableBoard(String boardName) {
        return unavailableBoardNames.stream().noneMatch(bn -> bn.equals(boardName));
    }
}
