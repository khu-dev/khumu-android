package com.khumu.android.articleWrite;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.InverseBindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.SimpleUser;
import com.khumu.android.databinding.ActivityArticleWriteBinding;
import com.khumu.android.feed.ArticleAdapter;
import com.khumu.android.feed.FeedViewModel;
import com.khumu.android.myPage.ArticleTagAdapter;
import com.khumu.android.data.Article;
import com.khumu.android.data.Tag;
import com.khumu.android.data.Board;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;
import com.khumu.android.retrofitInterface.ArticleService;
import com.khumu.android.retrofitInterface.ImageService;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ArticleWriteActivity extends AppCompatActivity {
    final static String TAG = "ArticleWriteActivity";
    public final static int UPLOAD_IMAGE_ACTIVITY = 1;
    @Inject
    ArticleRepository articleRepository;
    @Inject
    BoardRepository boardRepository;
    @Inject
    ImageService imageService;
    @Inject
    ArticleService articleService;
    ActivityArticleWriteBinding binding;
    ArticleWriteViewModel viewModel;
    Article article;
    RecyclerView imageRecyclerView;
    ImageAdapter imageAdapter;
    List<Board> boards;
    Board selectedBoard;

    EditText titleET;
    EditText contentET;
    TextView selectedBoardTV;
    View boardsView;
    Button submitBTN;
    CheckBox isAnonymousCB;
    EditText tagET;
    RecyclerView tagsRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArticleTagAdapter articleTagAdapter;

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
        KhumuApplication.container.inject(this);
        this.viewModel = new ViewModelProvider(ArticleWriteActivity.this, new ViewModelProvider.Factory(){
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ArticleWriteViewModel(ArticleWriteActivity.this,boardRepository, articleService, imageService, ArticleWriteActivity.this.getContentResolver());
            }
        }).get(ArticleWriteViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_write);
        binding.setViewModel(this.viewModel);
        // LifeCycle을 설정해주지 않으면 MutableLiveData을 제대로 Observe할 수 없어서 값이 변경이 안됨!
        binding.setLifecycleOwner(this);
        imageRecyclerView = findViewById(R.id.article_upload_images_recycler_view);

        article = new Article();
        boards = new ArrayList<Board>();
        listBoards();


        findViews();
        setEventListeners();
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

    protected void findViews(){
        titleET = findViewById(R.id.article_write_title_et);
        contentET = findViewById(R.id.article_write_content_et);
        selectedBoardTV = findViewById(R.id.article_write_selected_board_display_name_tv);
        boardsView = findViewById(R.id.article_write_boards_wrapper_layout);
        submitBTN = findViewById(R.id.article_write_submit_btn);
        isAnonymousCB = findViewById(R.id.article_write_is_anonymous_cb);
        isAnonymousCB.setChecked(true);
        tagET = findViewById(R.id.article_write_tag_et);
        tagsRecyclerView = findViewById(R.id.article_write_tags_recycler_view);
        articleTagAdapter = new ArticleTagAdapter(article.getTags());
        tagsRecyclerView.setAdapter(articleTagAdapter);
        this.article.setKind("anonymous");
    }

    protected void setEventListeners() {
        isAnonymousCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //test
                Log.d(TAG, "onCheckedChanged: " + viewModel.getArticle().getValue().getBoardDisplayName());

                if (isChecked) article.setKind("anonymous");
                else article.setKind("named");
            }
        });

        boardsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBoardListDialog();
            }
        });
        tagET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().endsWith(" ") || s.toString().endsWith(",") || s.toString().endsWith("\n")) {
                    String newTagName = s.toString().replace(" ", "").replace(",", "").replace("\n", "");
                    if (!newTagName.isEmpty()) {
                        article.getTags().add(new Tag(newTagName, false)); // follwed는 뭐가 되든 상관없음.
                        articleTagAdapter.notifyItemInserted(article.getTags().size() - 1);
                    } else {
                        Toast.makeText(ArticleWriteActivity.this, "태그 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    tagET.setText("");
                }
            }
        });

    }

    public void onClickSubmitButton(View v){
        viewModel.writeArticle();
    }

    // write 할 article의 kind를 리턴
    protected String getArticleKind(){
        if(this.isAnonymousCB.isChecked()){
            return "anonymous";
        } else {
            return "named";
        }
    }

    protected void createBoardListDialog(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ArticleWriteActivity.this);
//        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("게시판을 선택해주세요.");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ArticleWriteActivity.this, android.R.layout.select_dialog_singlechoice);
        for(Board b: boards){
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
                selectedBoard = boards.get(which);
                viewModel.setBoardToWrite(selectedBoard);
            }
        });
        builderSingle.show();
    }

    protected void listBoards(){
        new Thread(){
            @Override
            public void run() {
                try {
                    boards.addAll(boardRepository.ListBoards("free,department,lecture", null));
                    System.out.println("boards : " + boards);
                } catch (Exception e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ArticleWriteActivity.this, "게시판 목록을 가져오는 작업을 실패했습니다 ㅜㅜ", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }.start();
    }


}
