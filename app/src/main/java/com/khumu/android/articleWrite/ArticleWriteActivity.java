package com.khumu.android.articleWrite;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;
import com.khumu.android.databinding.ActivityArticleWriteBinding;
import com.khumu.android.repository.ArticleService;
import com.khumu.android.repository.BoardService;
import com.khumu.android.repository.ImageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleWriteActivity extends AppCompatActivity {
    final static String TAG = "ArticleWriteActivity";
    public final static int UPLOAD_IMAGE_ACTIVITY = 1;

    @Inject
    ImageService imageService;
    @Inject
    ArticleService articleService;
    @Inject
    BoardService boardService;
    ActivityArticleWriteBinding binding;
    ArticleWriteViewModel viewModel;
    RecyclerView imageRecyclerView;
    Board selectedBoard;
    EditText titleET;
    TextView isAnonymousTV;

    @BindingAdapter({"article_image_path_list", "viewModel"})
    public static void bindItem(RecyclerView recyclerView, List<ImagePath> imagePaths, ArticleWriteViewModel viewModel){
        Log.d(TAG, "bindItem: " + imagePaths.size());
        if (recyclerView.getAdapter() == null){
            //https://recipes4dev.tistory.com/168 이거 참고해서 삭제 작
            recyclerView.setAdapter(new ImageAdapter(viewModel, new ArrayList<>()));
        }
        if (recyclerView.getAdapter() != null && imagePaths != null){
            ImageAdapter adapter = (ImageAdapter) recyclerView.getAdapter();
            adapter.getImagePaths().clear();
            adapter.getImagePaths().addAll(imagePaths);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KhumuApplication.applicationComponent.inject(this);
        this.viewModel = new ViewModelProvider(ArticleWriteActivity.this, new ViewModelProvider.Factory(){
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ArticleWriteViewModel(ArticleWriteActivity.this, boardService, articleService, imageService);
            }
        }).get(ArticleWriteViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_write);
        binding.setViewModel(this.viewModel);
        binding.setActivity(this);
        // LifeCycle을 설정해주지 않으면 MutableLiveData을 제대로 Observe할 수 없어서 값이 변경이 안됨!
        binding.setLifecycleOwner(this);

        if (this.getIntent().hasExtra("article")) {
            Article original = (Article) this.getIntent().getSerializableExtra("article");
            viewModel.setArticle(original);
        }
        if (this.getIntent().hasExtra("board")) {
            Board currentBoard = (Board) this.getIntent().getSerializableExtra("board");
            viewModel.setBoardToWrite(currentBoard);
        }

        imageRecyclerView = findViewById(R.id.article_upload_images_recycler_view);
        DividerItemDecoration imageItemDivider = new DividerItemDecoration(imageRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        imageItemDivider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_image_item));
        imageRecyclerView.addItemDecoration(imageItemDivider);

        isAnonymousTV = findViewById(R.id.article_is_anonymous_tv);
        // color만 갖고 mutable live data를 만들기는 쫌 그러니까 간단하게 옵저버 직접 달기.
        titleET = findViewById(R.id.article_write_title_et);
        titleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                titleET.setText(s.toString());
                viewModel.updateIsWritable();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("이미지를 업로드 중입니다.");
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
            progressDialog.show();
            List<Image> images = ImagePicker.getImages(data);
            new Thread(){
                @Override
                public void run() {
                    try {
                        viewModel.uploadImages(images);
                        progressDialog.dismiss();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            // or get a single image only. 하지만 우린 여러 장을 default로 함.
            // Image image = ImagePicker.getFirstImageOrNull(data);
        }
    }

    public void onClickSubmitButton(View v){
        if (viewModel.getIsWritable().getValue() == null || !viewModel.getIsWritable().getValue()) {
            Log.d(TAG, "onClickSubmitButton: 글을 작성할 수 없는 상태입니다.");
        } else{
            viewModel.writeArticle(new Callback<Article>() {
                @Override
                public void onResponse(Call<Article> call, Response<Article> response) {
                    Toast.makeText(ArticleWriteActivity.this, "게시물을 작성했습니당!", Toast.LENGTH_SHORT).show();
                    ArticleWriteActivity.this.finish();
                }

                @Override
                public void onFailure(Call<Article> call, Throwable t) {
                    Log.d(TAG, "onFailure: ", t);
                    Toast.makeText(ArticleWriteActivity.this, "게시물을 작성 실패 ㅜㅜ", Toast.LENGTH_SHORT).show();
                }
        });
        }

    }

    public void onClickBoardListPopup(View v){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ArticleWriteActivity.this);
        builderSingle.setTitle("게시판을 선택해주세요.");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ArticleWriteActivity.this, android.R.layout.select_dialog_singlechoice);
        for(Board b: this.viewModel.getLiveBoards().getValue()){
            arrayAdapter.add(b.getDisplayName());
        }

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedBoard = viewModel.getLiveBoards().getValue().get(which);
                viewModel.setBoardToWrite(selectedBoard);
            }
        });
        builderSingle.show();
    }

    public void onClickIsAnonymousTV(View v) {
        Article article = viewModel.getLiveArticle().getValue();
        String kind = "anonymous";
        switch (article.getKind()) {
            case "named":
                kind = "anonymous";
                break;
            case "anonymous":
                kind = "named";
                break;
        }
        article.setKind(kind);
        viewModel.setArticle(article);
    }

    // 직접 Observer를 달지 않고, DataBinding, AAC를 이용하기 위해선
    // layout xml에서 live data의 필드를 전달해줘야한다.
    // 근데 kind를 받아서 anonymous인지 판단하는 로직이 결국 UI 단에서 수행되고 있기 때문에
    // 베스트 방법은 아닌 듯.
    public Drawable getIsAnonymousTVBackground(String kind) {
        Drawable background = this.getDrawable(R.drawable.background_round_filled_red_500);
        if (!kind.equals("anonymous")) {
            background = this.getDrawable(R.drawable.background_round_red_500_bordered_white);
        }
        return background;
    }

    public int getIsAnonymousTVTextColor(String kind) {
        int color = this.getColor(R.color.white);
        if (!kind.equals("anonymous")) {
            color = this.getColor(R.color.red_500);
        }
        return color;
    }

    public Drawable getSubmitBTNBackground() {
        Drawable background = this.getDrawable(R.drawable.background_round_filled_gray_300);
        if (viewModel.getIsWritable().getValue() == null || !this.viewModel.getIsWritable().getValue()) {
            background = this.getDrawable(R.drawable.background_round_filled_red_500);
        }
        return background;
    }



}
