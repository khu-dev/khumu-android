package com.khumu.android.articleWrite;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ArticleModifyActivity extends ArticleWriteActivity {
    private Intent intent;
    private Board selectedBoard;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 원래는 write 버튼인 것을 수정버튼으로.
        this.submitBTN.setText("수정");
        this.intent = getIntent();
        this.loadOriginalArticle();
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            // 원래는 UI 컴포넌트가 액션을 취할 때 마다 실시간으로 article의 값을 변경한 뒤
                            // update시에는 단순히 article을 이용해 업데이트하고싶은데,,,
//                            article.
                            articleRepository.UpdateArticle(article);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "게시물을 수정했습니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                            // 수정한 경우에만 Activity 수행 코드 OK
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("article", article);
                            ArticleModifyActivity.this.setResult(Activity.RESULT_OK, resultIntent);
                            finish();

                        } catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "게시물을 수정하지 못했습니다..", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }.start();
            }
        });
    }

    protected void loadOriginalArticle(){
        this.article = (Article) this.intent.getSerializableExtra("article");
        this.titleET.setText(article.getTitle());
        this.contentET.setText(article.getContent());

        if(this.getArticleKind() == "anonymous"){
            this.isAnonymousCB.setChecked(true);
        } else{
            this.isAnonymousCB.setChecked(false);
        }
    }

    @Override
    protected void listBoards() {
        new Thread(){
            @Override
            public void run() {
                try {
                    boards.addAll(boardRepository.ListBoards("free,department,lecture", null));
                    ArticleModifyActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setInitialBoard();
                        }
                    });
                } catch (Exception e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ArticleModifyActivity.this, "게시판 목록을 가져오는 작업을 실패했습니다 ㅜㅜ", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }.start();
    }

    private void setInitialBoard(){
        for (Board b: boards){
            if(article.getBoardName().equals(b.getName())){
                selectedBoard = b;
                selectedBoardTV.setText(selectedBoard.getDisplayName());
            }
        }
    }
}
