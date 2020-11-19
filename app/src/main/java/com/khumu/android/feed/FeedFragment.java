package com.khumu.android.feed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.data.SimpleUser;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.LikeArticleRepository;
import com.khumu.android.util.Util;
import com.khumu.android.R;
import com.khumu.android.data.Article;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

//    private HomeViewModel homeViewModel;
    private FeedViewModel feedViewModel;
    private LikeArticleRepository likeArticleRepository;
    private ArrayList<Article> articleArrayList;
    private ArticleAdapter articleAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ImageView likeIcon;
    private ImageView commentIcon;
    private ImageView bookmarkIcon;
//    private EditText writeArticleTitleET;
//    private EditText writeArticleContentET;
//    private ConstraintLayout writeArticleHeaderCL;
//    private LinearLayout writeArticleExpandableLL;
//    private ImageButton writeArticleExpandBTN;
//    private WriteArticleToggler writeArticleToggler;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
        feedViewModel = new ViewModelProvider(this, new FeedViewModelFactory(new ArticleRepository())).get(FeedViewModel.class);

        // 원랜 여기를 Singleton 형태로 의존성 관리가 되어야하는데 어떻게 하지??
        this.likeArticleRepository = new LikeArticleRepository();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // 아직 ViewModel은 안 다룸.
        // homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // 나의 부모인 컨테이너에서 내가 그리고자 하는 녀석을 얻어옴. 사실상 루트로 사용할 애를 객체와.
        // inflate란 xml => java 객체
        View root = inflater.inflate(R.layout.fragment_feed, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //view는 root 즉 fragment

        // root.findViewById(R.id.action_navigation_board_to_navigation_home).setOnClickListener(Navigation);
        // xml 상에 recyclerview는 실질적으로 아이템이 어떻게 구현되어있는지 정의되어있지 않다.
        // linearlayout의 형태를 이용하겠다면 linearlayoutmanager을 이용한다.
        likeIcon = (ImageView) view.findViewById(R.id.article_item_like_icon);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView = view.findViewById(R.id.feed_articles_list);
        recyclerView.setLayoutManager(linearLayoutManager);
        articleArrayList = new ArrayList<>();
        articleAdapter = new ArticleAdapter(articleArrayList, likeArticleRepository);
        recyclerView.setAdapter(articleAdapter);

        feedViewModel.getLiveDataArticles().observe(getViewLifecycleOwner(), new Observer<ArrayList<Article>>() {
            @Override
            public void onChanged(ArrayList<Article> changedSet) {
                int originalLength = articleArrayList.size();
                int newLength = changedSet.size();
                for(int i=originalLength; i<newLength; i++){
                    articleArrayList.add(changedSet.get(i));
                }
                articleAdapter.notifyItemRangeInserted(originalLength, newLength-originalLength);
                if(newLength > 0) recyclerView.smoothScrollToPosition(newLength-1);
            }
        });
//        writeArticleTitleET = view.findViewById(R.id.article_write_title);
//        writeArticleContentET = view.findViewById(R.id.article_write_content);

//        Button articleWriteBtn = view.findViewById(R.id.article_write_btn);
//        articleWriteBtn.setOnClickListener(v -> {
//            CreateArticle();
//            writeArticleToggler.collapse();
//        });
//        writeArticleToggler = new WriteArticleToggler();
//        writeArticleHeaderCL = view.findViewById(R.id.wrapper_article_write_header);
//        writeArticleExpandableLL = view.findViewById(R.id.wrapper_article_write_expandable);
//        writeArticleExpandBTN = view.findViewById(R.id.wrapper_article_write_expand_btn);
//            //set visibility to GONE
//        writeArticleExpandableLL.setVisibility(View.GONE);
//
//        writeArticleExpandBTN.setOnClickListener(writeArticleToggler);
    }

//    public void CreateArticle(){
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    feedViewModel.CreateArticle(new Article(
//                            null, new SimpleUser(Util.DEFAULT_USERNAME, "active"),
//                            writeArticleTitleET.getText().toString(),
//                            writeArticleContentET.getText().toString(),
//                            null
//                    ));
//                    writeArticleTitleET.setText("");
//                    writeArticleContentET.setText("");
//                    feedViewModel.ListArticle();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    public class FetchAfterCreateArticleAsyncTask extends AsyncTask{
//
//        @Override
//        protected Object doInBackground(Object[] objects) {
//            try {
//                feedViewModel.CreateArticle(new Article(
//                        null, new SimpleUser(Util.DEFAULT_USERNAME, "active"),
//                        writeArticleTitleET.getText().toString(),
//                        writeArticleContentET.getText().toString(),
//                        null
//                ));
//                writeArticleTitleET.setText("");
//                writeArticleContentET.setText("");
//                feedViewModel.ListArticle();
//            } catch (Exception e) {
//                e.printStackTrace();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        e.printStackTrace();
//                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//            return null;
//        }
//    }
//
//    public class WriteArticleToggler implements View.OnClickListener{
//        public void expand(){
//            Util.expandView(writeArticleExpandableLL);
//            writeArticleExpandBTN.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
//        }
//        public void collapse(){
//            Util.collapseView(writeArticleExpandableLL);
//            writeArticleExpandBTN.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
//        }
//        public void toggle(){
//            if (writeArticleExpandableLL.getVisibility()==View.GONE) expand();
//            else collapse();
//        }
//        @Override
//        public void onClick(View v) {
//            toggle();
//        }
//    }
}