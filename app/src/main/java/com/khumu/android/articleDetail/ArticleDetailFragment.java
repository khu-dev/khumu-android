package com.khumu.android.articleDetail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.R;
import com.khumu.android.articleWrite.ArticleModifyActivity;
import com.khumu.android.data.Article;
import com.khumu.android.data.Comment;
import com.khumu.android.data.SimpleComment;
import com.khumu.android.databinding.FragmentArticleDetailBinding;
import com.khumu.android.myPage.ArticleTagAdapter;
import com.khumu.android.repository.ArticleService;
import com.khumu.android.repository.CommentService;
import com.khumu.android.repository.NotificationService;

import java.io.Serializable;
import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.khumu.android.KhumuApplication.applicationComponent;

public class ArticleDetailFragment extends Fragment {
    private static final String TAG = "ArticleDetailFragment";
    private static final int MODIFY_ARTICLE_ACTIVITY = 1;
    @Inject
    public ArticleService articleService;
    @Inject
    public CommentService commentService;
    @Inject
    public NotificationService notificationService;
    private FragmentArticleDetailBinding binding;
    private Intent intent;
    private CommentViewModel commentViewModel;
    private Article article;
    private Comment commentToWrite;

    private ArrayList<Comment> commentArrayList;
    private CommentAdapter commentAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextView articleDetailContentTV;
    private TextView articleCommentCountTV;
    private TextView articleAuthorNicknameTV;
    private TextView articleLikeCountTV;
    private ImageView articleLikeIcon;
    private TextView articleDetailCreatedAtTV;
    private Button articleDetailSubscribeBTN;
    private EditText writeCommentContentET;
    private Button writeCommentContentBTN;
    private ImageView articleSettingIcon;
    private PopupMenu articleSettingPopupMenu;
    private CheckBox commentAnonymousCKB;
    private RecyclerView articleTagRecyclerView;
    private ArticleTagAdapter articleTagAdapter;
    private OnBackPressedCallback onBackPressedCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        applicationComponent.inject(this);
        this.intent = getActivity().getIntent();
        this.article = (Article) intent.getSerializableExtra("article");
        this.commentToWrite = null;
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
        commentViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory(){
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CommentViewModel(getContext(), articleService, commentService, notificationService, String.valueOf(article.getId()));
            }
        }).get(CommentViewModel.class);

        // 뒤로가기 시 하는 작업을 Callback을 통해 커스터마이징함
        onBackPressedCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                System.out.println("commentToWrite : " + commentToWrite);
                // 현재 대댓글 작성 중인 댓글이 있다면
                if (commentToWrite != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("대댓글 입력을 취소하겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            commentToWrite = null;
                            writeCommentContentET.setHint("댓글을 입력하세요");
                            // 해당 Callback 비활성화한 후 나중에 다시 대댓글 입력 시 활성화
                            setEnabled(false);
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 나의 부모인 컨테이너에서 내가 그리고자 하는 녀석을 얻어옴. 사실상 루트로 사용할 애를 객체와.
        // inflate란 xml => java 객체
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_detail, container, false);
        View root = binding.getRoot();
        // binding하며 사용할 Fragment가 사용하는 변수인 viewModel을 설정해줌.
        binding.recyclerViewCommentList.setAdapter(commentAdapter);
        binding.setViewModel(this.commentViewModel);
        binding.setFragment(this);
        binding.setLifecycleOwner(this);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // onCreateView에서 return된 view를 가지고 있다
        super.onViewCreated(view, savedInstanceState);

        this.binding.layoutArticleContent.articleDetailImageRecyclerView.setAdapter(new ImageAdapter(this.article.getImages(), this.getContext()));

        Intent intent = getActivity().getIntent();
        linearLayoutManager = new LinearLayoutManager(view.getContext());
//        linearLayoutManager = new LinearLayoutManager(view.getContext()) {
//            // 댓글만 scroll 되는 것을 막는다
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        };
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
        recyclerView = view.findViewById(R.id.recycler_view_comment_list);
        // 댓글창과 게시글이 하나로 이어져야하는데 댓글창이 따로 스크롤되는 경우를 막아줌
        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(new ArrayList<>(), getContext(), commentViewModel, ArticleDetailFragment.this);
        recyclerView.setAdapter(commentAdapter);

        articleTagRecyclerView = view.findViewById(R.id.article_detail_article_tags_recycler_view);

        articleDetailContentTV = view.findViewById(R.id.article_detail_content_tv);
        articleCommentCountTV = view.findViewById(R.id.article_detail_comment_count_tv);
        articleAuthorNicknameTV = view.findViewById(R.id.article_detail_author_nickname_tv);
        articleDetailCreatedAtTV= view.findViewById(R.id.article_detail_created_at_tv);
        articleDetailSubscribeBTN = view.findViewById(R.id.article_detail_subscribe_btn);
        articleLikeCountTV = view.findViewById(R.id.article_detail_like_article_count_tv);
        articleLikeIcon = view.findViewById(R.id.article_detail_like_icon);
        writeCommentContentET = view.findViewById(R.id.comment_write_content);
        writeCommentContentBTN = view.findViewById(R.id.comment_write_btn);
        articleSettingIcon = view.findViewById(R.id.article_detail_setting_icon);
        commentAnonymousCKB = view.findViewById(R.id.comment_anonymous_ckb);
        articleDetailSubscribeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentViewModel.getArticleSubscribed().getValue()) {
                    commentViewModel.unsubscribeArticle();
                }
                else {
                    commentViewModel.subscribeArticle();
                }
            }
        });
        writeCommentContentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleComment simpleComment = new SimpleComment(
                        article.getId(),
                        writeCommentContentET.getText().toString().trim()
                );
                if (commentAnonymousCKB.isChecked()){
                    simpleComment.setKind("anonymous");
                }
                else {
                    simpleComment.setKind("named");
                }
                try {
                    if (commentToWrite == null)
                        simpleComment.setParent(null);
                    else {
                        simpleComment.setParent(commentToWrite.getId());
                        //System.out.printf("commentToWrite : " + commentToWrite.getId());
                    }
                    if (TextUtils.isEmpty(simpleComment.getContent())) {
                        throw new Exception("내용을 입력하세요");
                    }
                    commentViewModel.createComment(simpleComment);
                    // 댓글 작성 후 작성창 비우기
                    writeCommentContentET.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        commentViewModel.getArticle();
        commentViewModel.getLiveDataComments().observe(getViewLifecycleOwner(), new Observer<ArrayList<Comment>>() {
            @Override
            public void onChanged(ArrayList<Comment> changedSet) {
                //int originalLength = commentAdapter.commentList.size();
                int newLength = changedSet.size();
                commentAdapter.commentList.clear();
                for (int i = 0; i < newLength; i++) {
                    commentAdapter.commentList.add(changedSet.get(i));
                }
                commentAdapter.notifyDataSetChanged();

                if (newLength > 0) recyclerView.smoothScrollToPosition(newLength - 1);
            }
        });
    }

    public void onClickArticleSettingMenu(View v){
        articleSettingPopupMenu = new PopupMenu(ArticleDetailFragment.this.getActivity(), v);
        articleSettingPopupMenu.inflate(R.menu.menu_article_detail_more);
        articleSettingPopupMenu.show();
        articleSettingPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.article_detail_modify_item:
                        Intent intent = new Intent(v.getContext(), ArticleModifyActivity.class);
                        // intent에서 해당 article에 대한 정보들을 저장
                        intent.putExtra("article", (Serializable) article);
                        ArticleDetailFragment.this.startActivityForResult(intent, MODIFY_ARTICLE_ACTIVITY);
                        break;
                    case R.id.article_detail_delete_item:
                        Call<Void> call = articleService.deleteArticle("application/json", article.getId());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(ArticleDetailFragment.this.getActivity(), "게시물을 삭제했습니다.", Toast.LENGTH_LONG).show();
                                ArticleDetailFragment.this.getActivity().finish();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(ArticleDetailFragment.this.getActivity(), "게시물을 삭제를 실패했습니다.", Toast.LENGTH_LONG).show();
                            }
                        });

                }
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            // article modify 후에 성공적이었다면.
            case ArticleDetailFragment.MODIFY_ARTICLE_ACTIVITY:{
                if(resultCode == Activity.RESULT_OK){
                    this.article = (Article) data.getSerializableExtra("article");
                    this.commentViewModel.getArticle();
                }
            }
        }
    }

    public void setCommentHint(String string) {
        writeCommentContentET.setHint(string);
    }

    private int getCommentLikedImage(boolean isLiked) {
        if(isLiked) {
            return R.drawable.ic_filled_heart;
        }
        return R.drawable.ic_empty_heart;
    }

    // 웬만하면 View의 로직은 Fragment에서 처리하도록.
    public int getSettingVisibility(){
        if(this.commentViewModel.getLiveDataArticle().getValue().getIsAuthor()){
            return View.VISIBLE;
        } else{
            return View.GONE;
        }
    }

    public void setCommentToWrite(Comment comment) {
        commentToWrite = comment;
        // 대댓글 입력 시 뒤로가기 누를 때 Callback이 작동하도록 활성화
        onBackPressedCallback.setEnabled(true);
    }
}


