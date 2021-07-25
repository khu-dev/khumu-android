package com.khumu.android.search;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.adapter.GrayBackgroundArticleAdapter;
import com.khumu.android.adapter.SearchedBoardAdapter;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;
import com.khumu.android.databinding.ActivityCommunitySearchBinding;
import com.khumu.android.adapter.ArticleAdapter;
import com.khumu.android.repository.ArticleService;
import com.khumu.android.repository.BoardService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CommunitySearchActivity extends AppCompatActivity {
    private static final String TAG = "CommunitySearchActivity";
    CommunitySearchViewModel viewModel;
    ActivityCommunitySearchBinding binding;
    @Inject
    BoardService boardService;
    @Inject
    ArticleService articleService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KhumuApplication.applicationComponent.inject(this);
        this.viewModel = new ViewModelProvider(CommunitySearchActivity.this, new ViewModelProvider.Factory(){
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CommunitySearchViewModel(CommunitySearchActivity.this, boardService, articleService, "", new ArrayList<Board>(), new ArrayList<Article>());
            }
        }).get(CommunitySearchViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_community_search);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.searchText.observe(this, text -> {
            viewModel.search(text);
        });
        binding.resultBoards.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1){
                    outRect.bottom = 20;
                }
            }
        });
        binding.resultBoards.setAdapter(new SearchedBoardAdapter(new ArrayList<>(), this));
        binding.resultArticles.setAdapter(new GrayBackgroundArticleAdapter(new ArrayList<>(), this));

    }

    @BindingAdapter("result_articles")
    public static void bindResultArticles(RecyclerView recyclerView, LiveData<List<Article>> resultArticles) {
        // 아직 recyclerView에 Adapter가 생성이 되지 않았을 때는 넘어가야한다.
        if (recyclerView.getAdapter() != null) {
            ArticleAdapter adapter = (ArticleAdapter) recyclerView.getAdapter();
            if (resultArticles.getValue() != null) {
                adapter.articleList.clear();
                adapter.articleList.addAll(resultArticles.getValue());
            } else {
                Log.e(TAG, "bindResultArticles: adapter가 전달받은 article list가 null입니다.");
            }
            adapter.notifyDataSetChanged();
        }
    }
    @BindingAdapter("result_boards")
    public static void bindResultBoards(RecyclerView recyclerView, LiveData<List<Board>> resultBoards) {
        // 아직 recyclerView에 Adapter가 생성이 되지 않았을 때는 넘어가야한다.
        if (recyclerView.getAdapter() != null) {
            SearchedBoardAdapter adapter = (SearchedBoardAdapter) recyclerView.getAdapter();
            if (resultBoards.getValue() != null) {
                adapter.boardList.clear();
                adapter.boardList.addAll(resultBoards.getValue());
            } else {
                Log.e(TAG, "bindResultBoards: adapter가 전달받은 board list가 null입니다.");
            }
            adapter.notifyDataSetChanged();
        }
    }
}
