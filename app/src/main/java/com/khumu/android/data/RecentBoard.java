package com.khumu.android.data;

public class RecentBoard extends Board{
    public final static String name = "recent";
    public final static String displayName = "최근게시판";
    public RecentBoard(){
        super(RecentBoard.name,
            RecentBoard.displayName,
            "게시판에 상관 없이 최근에 작성된 게시물들을 읽어볼 수 있습니다.",
            null);
    }
}
