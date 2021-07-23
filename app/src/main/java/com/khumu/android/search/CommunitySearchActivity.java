package com.khumu.android.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.articleWrite.ArticleWriteActivity;
import com.khumu.android.articleWrite.ArticleWriteViewModel;
import com.khumu.android.boardList.BoardAdapter;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;
import com.khumu.android.databinding.ActivityCommunitySearchBinding;
import com.khumu.android.di.component.ApplicationComponent;
import com.khumu.android.feed.ArticleAdapter;
import com.khumu.android.login.LoginActivity;
import com.khumu.android.repository.ArticleService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CommunitySearchActivity extends AppCompatActivity {
    CommunitySearchViewModel viewModel;
    ActivityCommunitySearchBinding binding;
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
                return (T) new CommunitySearchViewModel(CommunitySearchActivity.this, articleService, "", new ArrayList<Board>(), new ArrayList<Article>());
            }
        }).get(CommunitySearchViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_community_search);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.searchText.observe(this, text -> {
            viewModel.search(text);
        });
        binding.resultArticles.setAdapter(new ArticleAdapter(new ArrayList<>(), this));

    }

    @BindingAdapter("result_articles")
    public static void bindResultBoards(RecyclerView recyclerView, LiveData<List<Article>> resultArticles) {
        // 아직 recyclerView에 Adapter가 생성이 되지 않았을 때는 넘어가야한다.
        if (recyclerView.getAdapter() != null) {
            ArticleAdapter adapter = (ArticleAdapter) recyclerView.getAdapter();
            adapter.articleList.clear();
            adapter.articleList.addAll((List<Article>) resultArticles.getValue());
            adapter.notifyDataSetChanged();
        }
    }
}
