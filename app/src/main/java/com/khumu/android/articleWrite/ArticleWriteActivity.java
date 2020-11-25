package com.khumu.android.articleWrite;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.Article;
import com.khumu.android.data.SimpleArticle;
import com.khumu.android.feed.BoardsToggler;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.KhumuUserRepository;

import org.json.JSONException;

import java.io.IOException;

import javax.inject.Inject;

public class ArticleWriteActivity extends AppCompatActivity {
    @Inject
    ArticleRepository articleRepository;

    EditText titleET;
    EditText contentET;
    BoardsToggler boardsToggler;
    ImageView boardToggleBTN;
    TextView selectedBoardTV;
    View boardsView;
    Button submitBTN;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KhumuApplication.container.inject(this);

        setContentView(R.layout.activity_article_write);
        findViews();
        setEventListeners();

        setTestInput();
    }

    private void findViews(){
        titleET = findViewById(R.id.article_write_title_et);
        contentET = findViewById(R.id.article_write_content_et);
        boardToggleBTN = findViewById(R.id.article_write_toggle_boards_icon);
        selectedBoardTV = findViewById(R.id.article_write_selected_board_display_name_tv);
        boardsView = findViewById(R.id.article_write_boards_wrapper_layout);
        submitBTN = findViewById(R.id.article_write_submit_btn);
    }

    private void setEventListeners(){
        boardsToggler = new BoardsToggler(boardsView, boardToggleBTN);
        boardsView.setOnClickListener(boardsToggler);

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        SimpleArticle article = new SimpleArticle(
                            "seoul",
                            titleET.getText().toString(),
                            contentET.getText().toString()
                        );
                        try {
                            boolean isArticleCreated = articleRepository.CreateArticle(article);
                            if (!isArticleCreated){
                                throw new Exception("요청은 갔으나 게시물이 생성되지 않았음.");
                            } else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeTz`ext(getApplicationContext(), "게시물을 작성했습니다.", Toast.LENGTH_LONG).show();
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
        titleET.setText("테스트용 제목입니다.");
        contentET.setText("테스트용 본문입니다.");
    }
}
