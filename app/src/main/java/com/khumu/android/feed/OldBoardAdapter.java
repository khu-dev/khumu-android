//package com.khumu.android.feed;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import com.khumu.android.R;
//import com.khumu.android.data.Board;
//import com.khumu.android.repository.BoardRepository;
//
//import java.util.List;
//
//import javax.inject.Inject;
//
//// Adapter는 외부의 UI는 최대한 모르고싶다.
//// data 조작을 하고싶은 경우 ViewModel의 데이터만을 조작하며
//// 그 내용을 View 계층에서 이용해 보여준다.
//public class OldBoardAdapter extends ArrayAdapter<Board> {
////    private final static String TAG = "BoardAdapter";
//    public List<Board> boardList;
//
//    @Inject
//    public BoardRepository boardRepository;
//
//    public OldBoardAdapter(List<Board> boardList) {
//        this.boardList = boardList;
//    }
//    // private FeedViewModel feedViewModel;
//
////    public BoardAdapter(@NonNull Context context, int resource, List<Board> boards) {
////        // 세 번째 인자가 이 adpater의 collection을 의미
////        super(context, resource, boards);
////        KhumuApplication.container.inject(this);
////        this.boards = boards;
////        this.feedViewModel = feedViewModel;
////    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = convertView;
//        // view가 안 만들어져있으면 만든다.
//        if(convertView == null){
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feed_board_item, parent, false);
//        }
//        Board board = boards.get(position);
//        TextView boardNameTV = (TextView)view.findViewById(R.id.feed_board_item_display_name);
//
//        // 현재 선택되어있는 게시판
//        String currentBoardDisplayName = this.feedViewModel.getCurrentBoard().getDisplayName();
//        if(board.getDisplayName().equals(currentBoardDisplayName)){
//            boardNameTV.setTextColor(parent.getContext().getColor(R.color.black));
//        } else{
//            // 선택되지 않은 게시판
//            boardNameTV.setTextColor(parent.getContext().getColor(R.color.gray_500));
//        }
//
//        boardNameTV.setText(board.getDisplayName());
//
//        boardNameTV.setOnClickListener(new BoardClickListener(board));
//        return view;
//    }
//    private class BoardClickListener implements View.OnClickListener{
//        private Board board;
//        public BoardClickListener(Board board) {
//            this.board = board;
//        }
//
//        @Override
//        public void onClick(View v) {
//            new Thread(){
//                @Override
//                public void run() {
//                    OldBoardAdapter.this.feedViewModel.ListArticles();
//                }
//            }.start();
//            OldBoardAdapter.this.feedViewModel.setCurrentBoard(board);
//        }
//    }
//}
