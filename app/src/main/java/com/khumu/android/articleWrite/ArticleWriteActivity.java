package com.khumu.android.articleWrite;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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

    TextView isAnonymousTV;

    @BindingAdapter({"article_image_path_list", "viewModel"})
    public static void bindItem(RecyclerView recyclerView, MutableLiveData<List<ImagePath>> imagePaths, ArticleWriteViewModel viewModel){
        Log.d(TAG, "bindItem: " + imagePaths.getValue().size());
        if (recyclerView.getAdapter() == null){
            //https://recipes4dev.tistory.com/168 이거 참고해서 삭제 작
            recyclerView.setAdapter(new ImageAdapter(viewModel, new ArrayList<>()));
        }
        if (recyclerView.getAdapter() != null && imagePaths != null){
            ImageAdapter adapter = (ImageAdapter) recyclerView.getAdapter();
            adapter.getImagePaths().clear();
            adapter.getImagePaths().addAll(imagePaths.getValue());
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
        // LifeCycle을 설정해주지 않으면 MutableLiveData을 제대로 Observe할 수 없어서 값이 변경이 안됨!
        binding.setLifecycleOwner(this);
        imageRecyclerView = findViewById(R.id.article_upload_images_recycler_view);
        DividerItemDecoration imageItemDivider = new DividerItemDecoration(imageRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        imageItemDivider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_image_item));
        imageRecyclerView.addItemDecoration(imageItemDivider);

        isAnonymousTV = findViewById(R.id.article_is_anonymous_tv);
        // color만 갖고 mutable live data를 만들기는 쫌 그러니까 간단하게 옵저버 직접 달기.
        this.viewModel.getLiveArticle().observe(this, (article) -> {
            int color = this.getColor(R.color.red_500);
            switch (article.getKind()) {
                case "named":
                    color = this.getColor(R.color.gray_300);
                    break;
                case "anonymous":
                    color = this.getColor(R.color.red_500);
                    break;
            }
            isAnonymousTV.setTextColor(color);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
            try {
                viewModel.uploadImages(images);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "이미지 업로드가 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
            // or get a single image only. 하지만 우린 여러 장을 default로 함.
            // Image image = ImagePicker.getFirstImageOrNull(data);
        }
    }

    public void onClickSubmitButton(View v){
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
}
