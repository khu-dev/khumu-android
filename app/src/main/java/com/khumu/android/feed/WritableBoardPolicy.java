package com.khumu.android.feed;

import com.khumu.android.data.Board;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Policy가 계속해서 변경될 수도 있다는 상상의 개발 세계 속에서
 * 그러한 케이스에 유연히 반응하기 위한 추상화
 */
public interface WritableBoardPolicy {
    boolean isWritableBoard(Board board);
    boolean isWritableBoard(String boardName);
}
