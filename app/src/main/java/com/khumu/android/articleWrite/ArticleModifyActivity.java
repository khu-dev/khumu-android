/**
 * ArticleWriteActivity를 상속받아서 ArticleModify를 수행한다.
 * 부모 생성자에 비해 intent를 통해서 serializable로 Feed에게서 전달받은 Article을
 * ViewModel에 주입시키는 것이 차이이다.
 */
package com.khumu.android.articleWrite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.myPage.ArticleTagAdapter;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleModifyActivity extends ArticleWriteActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Article original = (Article) this.getIntent().getSerializableExtra("article");
        viewModel.setArticle(original);
    }

    public void onClickSubmitButton(View v){
        viewModel.modifyArticle(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                Toast.makeText(ArticleModifyActivity.this, "게시물을 수정했습니당!", Toast.LENGTH_SHORT).show();
                ArticleModifyActivity.this.finish();
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                Log.d(TAG, "onFailure: ", t);
                Toast.makeText(ArticleModifyActivity.this, "게시물을 수정 실패 ㅜㅜ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
