// 동일한 FeedViewModel Class를 이용할 수 있으면서 Feed의 기능을 할
// Fragment들의 기본 클래스
package com.khumu.android.feed;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.R;
import com.khumu.android.data.Article;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFeedFragment extends Fragment {
    private final static String TAG = "BaseFeedFragment";
    protected LinearLayoutManager linearLayoutManager;
    protected RecyclerView articleRecyclerView;
    protected FeedViewModel feedViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    // 자식들은 꼭 feedViewModel을 초기화해야한다.
    abstract protected void provideFeedViewModel();

    @BindingAdapter("article_list")
    public static void bindItem(RecyclerView recyclerView, LiveData articleList){
        // null 인 경우에는 아직 다룰 때가 아님.
        if (recyclerView.getAdapter() != null){
            ArticleAdapter adapter = (ArticleAdapter)recyclerView.getAdapter();
            adapter.articleList.clear();
            adapter.articleList.addAll((List<Article>) articleList.getValue());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //view는 root 즉 fragment
        // root.findViewById(R.id.action_navigation_board_to_navigation_home).setOnClickListener(Navigation);
        // xml 상에 recyclerview는 실질적으로 아이템이 어떻게 구현되어있는지 정의되어있지 않다.
        // linearlayout의 형태를 이용하겠다면 linearlayoutmanager을 이용한다.
        this.findViews(view);
        this.setAdapters();
        this.setEventListeners(view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected void findViews(View root){
        linearLayoutManager = new LinearLayoutManager(root.getContext());
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        articleRecyclerView = root.findViewById(R.id.feed_articles_list);
    }

    protected void setAdapters(){
        ArticleAdapter articleAdapter = new ArticleAdapter(new ArrayList<>(), getContext());
        articleRecyclerView.setLayoutManager(linearLayoutManager);
        articleRecyclerView.setAdapter(articleAdapter);
    }

    protected void setEventListeners(View root){}
}