package com.khumu.android.articleWrite;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;
import com.khumu.android.data.SimpleArticle;
import com.khumu.android.feed.BoardsToggler;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;
import com.khumu.android.repository.KhumuUserRepository;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ArticleWriteActivity extends AppCompatActivity {
    @Inject
    ArticleRepository articleRepository;
    @Inject
    BoardRepository boardRepository;

    List<Board> boards;
    Board selectedBoard;

    EditText titleET;
    EditText contentET;
    BoardsToggler boardsToggler;
    ImageView boardToggleBTN;
    TextView selectedBoardTV;
    View boardsView;
    Button submitBTN;
    CheckBox isAnonymousCB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KhumuApplication.container.inject(this);

        boards = new ArrayList<Board>();

        listBoards();
        setContentView(R.layout.activity_article_write);
        findViews();
        setEventListeners();

        setTestInput();
    }

    private void findViews(){
        titleET = findViewById(R.id.article_write_title_et);
        contentET = findViewById(R.id.article_write_content_et);
        boardToggleBTN = findViewById(R.id.article_write_popup_boards_icon);
        selectedBoardTV = findViewById(R.id.article_write_selected_board_display_name_tv);
        boardsView = findViewById(R.id.article_write_boards_wrapper_layout);
        submitBTN = findViewById(R.id.article_write_submit_btn);
        isAnonymousCB = findViewById(R.id.article_write_is_anonymous_cb);
    }

    private void setEventListeners(){
        boardsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBoardListDialog();
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        SimpleArticle article = new SimpleArticle(
                            selectedBoard.getName(),
                            titleET.getText().toString(),
                            contentET.getText().toString(),
                            ArticleWriteActivity.this.getArticleKind()
                        );
                        try {
                            boolean isArticleCreated = articleRepository.CreateArticle(article);
                            if (!isArticleCreated){
                                throw new Exception("요청은 갔으나 게시물이 생성되지 않았음.");
                            } else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "게시물을 작성했습니다.", Toast.LENGTH_LONG).show();
                                    }
                                });
                                finish();
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "알 수 없는 이유로 게시글을 생성하지 못했습니다. ㅜ.ㅜ", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }.start();
            }
        });
    }

    private void setTestInput(){
        titleET.setText("텟트");
        contentET.setText("텟트");
    }

    // write 할 article의 kind를 리턴
    private String getArticleKind(){
        if(this.isAnonymousCB.isChecked()){
            return "anonymous";
        } else {
            return "named";
        }
    }

    private void createBoardListDialog(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ArticleWriteActivity.this);
//        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("게시판을 선택해주세요.");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ArticleWriteActivity.this, android.R.layout.select_dialog_singlechoice);
        for(Board b: boards){
            arrayAdapter.add(b.getDisplayName());
        }

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedBoard = boards.get(which);
                selectedBoardTV.setText(selectedBoard.getDisplayName());
            }
        });
        builderSingle.show();
    }

    private void listBoards(){
        new Thread(){
            @Override
            public void run() {
                try {
                    boards.addAll(boardRepository.ListBoards());
                } catch (Exception e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ArticleWriteActivity.this, "게시판 목록을 가져오는 작업을 실패했습니다 ㅜㅜ", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }.start();
    }
}
