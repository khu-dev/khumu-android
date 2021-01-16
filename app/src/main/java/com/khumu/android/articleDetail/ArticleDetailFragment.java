package com.khumu.android.articleDetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.articleWrite.ArticleModifyActivity;
import com.khumu.android.myPage.ArticleTagAdapter;
import com.khumu.android.data.Article;
import com.khumu.android.data.Comment;
import com.khumu.android.data.SimpleComment;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.CommentRepository;
import com.khumu.android.usecase.ArticleUseCase;

import java.util.ArrayList;

import javax.inject.Inject;

public class ArticleDetailFragment extends Fragment {
    private static final String TAG = "ArticleDetailFragment";
    private static final int MODIFY_ARTICLE_ACTIVITY = 1;
    @Inject
    public ArticleRepository articleRepository;
    @Inject
    public CommentRepository commentRepository;
    @Inject
    ArticleUseCase articleUseCase;

    private Intent intent;
    private CommentViewModel commentViewModel;
    private Article article;

    private ArrayList<Comment> commentArrayList;
    private CommentAdapter commentAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextView articleDetailTitleTV;
    private TextView articleDetailContentTV;
    private TextView articleCommentCountTV;
    private TextView articleAuthorNicknameTV;
    private TextView articleLikeCountTV;
    private ImageView articleLikeIcon;
    private TextView articleDetailCreatedAtTV;
    private EditText writeCommentContentET;
    private Button writeCommentContentBTN;
    private ImageView articleSettingIcon;
    private PopupMenu articleSettingPopupMenu;

    private RecyclerView articleTagRecyclerView;
    private ArticleTagAdapter articleTagAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.intent = getActivity().getIntent();
        this.article = (Article) intent.getSerializableExtra("article");
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
        KhumuApplication.container.inject(this);
        commentViewModel = new ViewModelProvider(this,
                new CommentViewFactory(commentRepository, Integer.toString(article.getID()))
        ).get(CommentViewModel.class);
//        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 나의 부모인 컨테이너에서 내가 그리고자 하는 녀석을 얻어옴. 사실상 루트로 사용할 애를 객체와.
        // inflate란 xml => java 객체
        View root = inflater.inflate(R.layout.fragment_article_detail, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // onCreateView에서 return된 view를 가지고 있다
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();

        linearLayoutManager = new LinearLayoutManager(view.getContext());
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
        recyclerView = view.findViewById(R.id.recycler_view_comment_list);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(commentAdapter);

        articleTagRecyclerView = view.findViewById(R.id.article_detail_article_tags_recycler_view);

        articleDetailTitleTV = view.findViewById(R.id.article_detail_title_tv);
        articleDetailContentTV = view.findViewById(R.id.article_detail_content_tv);
        articleCommentCountTV = view.findViewById(R.id.article_detail_comment_count_tv);
        articleAuthorNicknameTV = view.findViewById(R.id.article_detail_author_nickname_tv);
        articleDetailCreatedAtTV= view.findViewById(R.id.article_detail_created_at_tv);
        articleLikeCountTV = view.findViewById(R.id.article_detail_like_article_count_tv);
        articleLikeIcon = view.findViewById(R.id.article_detail_like_icon);
        writeCommentContentET = view.findViewById(R.id.comment_write_content);
        writeCommentContentBTN = view.findViewById(R.id.comment_write_btn);
        articleSettingIcon = view.findViewById(R.id.article_detail_setting_icon);

        writeCommentContentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        SimpleComment simpleComment = new SimpleComment(
                                article.getID(),
                                writeCommentContentET.getText().toString()
                        );
                        try {
                            boolean isCommentCreated = commentRepository.CreateComment(simpleComment, String.valueOf(article.getID()));
                            if (!isCommentCreated) {
                                throw new Exception("요청은 갔으나 게시물이 생성되지 않았음.");
                            } else {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        commentViewModel.ListComment();
                                        Toast.makeText(getContext(), "댓글을 작성했습니다.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "알 수 없는 이유로 댓글을 생성하지 못했습니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }.start();
            }
        });

        loadArticleToView();

        commentViewModel.getLiveDataComments().observe(getViewLifecycleOwner(), new Observer<ArrayList<Comment>>() {
            @Override
            public void onChanged(ArrayList<Comment> changedSet) {
                int originalLength = commentAdapter.commentList.size();
                int newLength = changedSet.size();
                for (int i = originalLength; i<newLength; i++) {
                    commentAdapter.commentList.add(changedSet.get(i));
                }
                commentAdapter.notifyItemRangeInserted(originalLength, newLength-originalLength);
//                if(newLength > 0) recyclerView.smoothScrollToPosition(newLength-1);
            }
        });

        articleSettingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleSettingPopupMenu = new PopupMenu(ArticleDetailFragment.this.getActivity(), v);
                articleSettingPopupMenu.inflate(R.menu.menu_article_detail_more);
                articleSettingPopupMenu.show();
                articleSettingPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        new Thread(){
                            @Override
                            public void run() {
                                switch (item.getItemId()){
                                    case R.id.article_detail_modify_item:
                                        Intent intent = new Intent(v.getContext(), ArticleModifyActivity.class);
                                        // intent에서 해당 article에 대한 정보들을 저장
                                        intent.putExtra("article", article);
                                        ArticleDetailFragment.this.startActivityForResult(intent, MODIFY_ARTICLE_ACTIVITY);
//                                        Toast.makeText(ArticleDetailFragment.this.getActivity(), "modify", Toast.LENGTH_LONG).show();
                                        break;
                                    case R.id.article_detail_delete_item:
                                        boolean isDeleted = articleRepository.DeleteArticle(article.getID());
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (isDeleted){
                                                    Toast.makeText(ArticleDetailFragment.this.getActivity(), "게시물을 삭제했습니다.", Toast.LENGTH_LONG).show();
                                                    ArticleDetailFragment.this.getActivity().finish();
                                                } else{
                                                    Toast.makeText(ArticleDetailFragment.this.getActivity(), "게시물을 삭제를 실패했습니다.", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                        break;

                                }
                            }
                        }.start();
                        return true;
                    }
                });
            }
        });
    }

    /*
    public class FetchCommentsAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                commentViewModel.CreateComment(new Comment(
                        null,
                        null,
                        0,
                        0
                ));
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
    }
     */
    // this.article의 정보를 view에 적용한다.
    private void loadArticleToView(){
        articleDetailTitleTV.setText(article.getTitle());
        articleDetailContentTV.setText(article.getContent());
        articleCommentCountTV.setText(String.valueOf(article.getCommentCount()));
        // 글쓴이가 본인인 경우
        if (article.getAuthor().getUsername().equals(KhumuApplication.getUsername())){
            articleAuthorNicknameTV.setTextColor(getContext().getColor(R.color.red_300));
            if (article.getKind().equals("anonymous")){
                articleAuthorNicknameTV.setText("익명");
            } else{
                articleAuthorNicknameTV.setText(article.getAuthor().getNickname());
            }
        } else{
            // 글쓴이가 본인이 아닌 경우
            articleAuthorNicknameTV.setText(article.getAuthor().getNickname());
        }

        articleDetailCreatedAtTV.setText(article.getArticleCreatedAt());
        articleLikeCountTV.setText(String.valueOf(article.getLikeArticleCount()));
        articleLikeIcon.setImageResource(getCommentLikedImage(article.isLiked()));
        if(articleUseCase.amIAuthor(article.getAuthor().getUsername())){
            articleSettingIcon.setVisibility(View.VISIBLE);
        } else{
            articleSettingIcon.setVisibility(View.GONE);
        }

        articleTagRecyclerView.setAdapter(new ArticleTagAdapter(article.getTags()));

        /*
        articleLikeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        try{
                            likeArticleRepository.toggleLikeArticle(new LikeArticle(articleID));
                            boolean liked = isLikedBooloean;
                            if(liked){
                                article.setLiked(false);
                                article.setLikeArticleCount(article.getLikeArticleCount() - 1);
                            } else{
                                article.setLiked(true);
                                article.setLikeArticleCount(article.getLikeArticleCount() + 1);
                            }
                            // Network thread 에서 작업 수행 후 MainThread에 UI 작업을 Post
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    holder.articleLikeIcon.setImageResource(getArticleLikedImage(article));
                                    holder.articleLikeCountTV.setText(String.valueOf(article.getLikeArticleCount()));
                                }
                            });
                        } catch (LikeArticleRepository.BadRequestException e){
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            // article modify 후에 성공적이었다면.
            case ArticleDetailFragment.MODIFY_ARTICLE_ACTIVITY:{
                if(resultCode == Activity.RESULT_OK){
                    this.article = (Article) data.getSerializableExtra("article");
                    this.loadArticleToView();
                }
            }
        }
    }

    private int getCommentLikedImage(boolean isLiked) {
        if(isLiked) {
            return R.drawable.ic_filled_heart;
        }
        return R.drawable.ic_empty_heart;
    }

    private void setEventListeners() {

    }
}
