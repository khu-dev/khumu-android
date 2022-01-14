/**
 * BaseFeedFragment를 상속받아 몇 가지 추가 기능을 수행한다.
 *
 * 1. fragment_feed.xml이 아닌 fragment_tab_feed.xml이용
 *    fragment_feed.xml을 include하는 fragment_tab_feed.xml을 바탕으로 inflate
 *
 * 2. ListBoards
 *    Tab layout이 추가되면서 한 Board에 대한 Article을 불러오는 것 뿐만 아니라 Board의 List도 필요하기에
 *
 * 3. ArticleCreate
 *    Article을 생성하는 버튼을 통해 ArticleWriteActivity를 띄움.
 */
package com.khumu.android.feed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.adapter.ArticleAdapter;
import com.khumu.android.adapter.SimpleAnnouncementAdapter;
import com.khumu.android.articleWrite.ArticleWriteActivity;
import com.khumu.android.data.Announcement;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;
import com.khumu.android.databinding.FragmentMyFeedBinding;
import com.khumu.android.repository.AnnouncementService;
import com.khumu.android.repository.ArticleService;
import com.khumu.android.repository.BoardService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyFeedFragment extends Fragment {
    private final static String TAG = "TabFeedFragment";
    @Inject public BoardService boardService;
    @Inject public ArticleService articleService;
    @Inject
    public AnnouncementService announcementService;

    FragmentMyFeedBinding binding;
    private Button articleWriteBTN;
    private FeedViewModel feedViewModel;

    @BindingAdapter("following_boards")
    public static void bindFollowingBoards(RecyclerView recyclerView, LiveData<List<Board>> followingBoards){
        if (recyclerView.getAdapter() != null) {
//            Log.w(TAG, "bindFollowingBoards: " +  followingBoards.getValue().get(0));
            FollowingBoardAdapter adapter = (FollowingBoardAdapter) recyclerView.getAdapter();
            adapter.boardList.clear();
            adapter.boardList.addAll((List<Board>) followingBoards.getValue());
            adapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter("announcements")
    public static void bindAnnouncements(RecyclerView recyclerView, List<Announcement> announcements){
        if (recyclerView.getAdapter() != null) {
            SimpleAnnouncementAdapter adapter = (SimpleAnnouncementAdapter) recyclerView.getAdapter();
            adapter.setAnnouncementList(announcements);
            adapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter("article_list")
    public static void bindItem(RecyclerView recyclerView, List<Article> articleList){
        // null 인 경우에는 아직 다룰 때가 아님.
        if (recyclerView.getAdapter() != null){
            ArticleAdapter adapter = (ArticleAdapter)recyclerView.getAdapter();
            adapter.articleList = articleList;
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        KhumuApplication.applicationComponent.inject(this);
        this.feedViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.Factory(){
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new FeedViewModel(boardService, articleService, announcementService);
            }
        }).get(FeedViewModel.class);
        this.feedViewModel.listBoards(null, true);
        this.feedViewModel.listArticles();
        this.feedViewModel.listRecentAnnouncements();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // 아직 ViewModel은 안 다룸.
        // homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // 나의 부모인 컨테이너에서 내가 그리고자 하는 녀석을 얻어옴. 사실상 루트로 사용할 애를 객체와.
        // inflate란 xml => java 객체
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_feed, container, false);
        View root = binding.getRoot();
        // binding하며 사용할 Fragment가 사용하는 변수인 viewModel을 설정해줌.
        binding.setFeedViewModel(this.feedViewModel);
        // viewModel owner도 activity로 설정해놨으니
        // activity를 lifecycler owner로 하면 더 좋을 것 같음.
        binding.setLifecycleOwner(this.getActivity());

        binding.feedFollowingBoardsRecyclerView.setAdapter(new FollowingBoardAdapter(this.getContext(), new ArrayList<Board>()));
        binding.feedAnnouncementRecyclerView.setAdapter(new SimpleAnnouncementAdapter(this.getContext(), new ArrayList<Announcement>()));
        binding.feedBodyNestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) ->  {
            if (v.getChildAt(v.getChildCount() - 1) != null)
            {
                if (scrollY > oldScrollY)
                {
                    if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight()))
                    {
                        feedViewModel.loadMoreArticles();
                    }
                }
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        articleWriteBTN = view.findViewById(R.id.tab_feed_article_write_btn);
        TextView toolbarTitleTV =view.findViewById(R.id.toolbar_title_tv);

        String toolbarTitle = "나의 피드";
        toolbarTitleTV.setText(toolbarTitle);
        ArticleAdapter articleAdapter = new ArticleAdapter(toolbarTitle, new ArrayList<>(), getContext());
        articleAdapter.setHasStableIds(true);
        binding.feedFragment.feedArticlesList.setAdapter(articleAdapter);

        articleWriteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent writeIntent = new Intent(getContext(), ArticleWriteActivity.class);
                v.getContext().startActivity(writeIntent);
            }
        });

        binding.feedBodySwipeRefreshLayout.setOnRefreshListener(()->{
            feedViewModel.listRecentAnnouncements();
            feedViewModel.listArticles();
            binding.feedBodySwipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 굳이 refresh 안해도 될 것 같기도 하다.
        // refresh하려면 글을 작성한 Activity가 종료된 건지 Result 확인 해줘야함.
        // 안 그러면 계속해서 fragment가 resume될 때마다 게시글 새로 받아오게됨.
//        feedViewModel.clearArticles();
//        feedViewModel.listArticles();
    }
}