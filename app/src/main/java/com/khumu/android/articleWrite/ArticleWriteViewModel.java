package com.khumu.android.articleWrite;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.Bindable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.esafirm.imagepicker.model.Image;
import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Board;
import com.khumu.android.data.Article;
import com.khumu.android.data.rest.ImageUploadResponse;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;
import com.khumu.android.retrofitInterface.ArticleService;
import com.khumu.android.retrofitInterface.ImageService;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleWriteViewModel extends ViewModel {
    private final static String TAG = "ArticleWriteViewModel";
    private final String INITIAL_BOARD_DISPLAY_NAME = "게시판을 선택해주세요.";
    public BoardRepository boardRepository;
    public ArticleService articleService;
    public ImageService imageService;
    /**
     * boards는 아직 리팩토링 못해따.
     */
    private MutableLiveData<List<Board>> boards;
    private MutableLiveData<Article> article;
    private MutableLiveData<List<Bitmap>> uploadingBitmaps;
    private ContentResolver contentResolver; // Bitmap 그릴 때 사용
    private Context context;

    public ArticleWriteViewModel(Context context, BoardRepository boardRepository, ArticleService articleService, ImageService imageService, ContentResolver contentResolver){
        this.context = context;
        this.boardRepository = boardRepository;
        this.articleService = articleService;
        this.imageService = imageService;
//        this.boards = new MutableLiveData<>(boardRepository.ListBoards());
        this.article = new MutableLiveData<>(generateInitialArticle());

        this.uploadingBitmaps = new MutableLiveData<>(new ArrayList<>());
        this.contentResolver = contentResolver;
    }

    public LiveData<Article> getArticle(){
        return article;
    }

    public MutableLiveData<List<Bitmap>> getUploadingBitmaps() {
        return uploadingBitmaps;
    }

    // 내가 게시물을 적을 게시판을 선택한다. 그 내용을 MutableLiveData인 article에 반영
    // 따로 MutableLiveData Board를 관리하지는 않는 중.
    public void setBoardToWrite(Board b){
        Article a = this.article.getValue();
        a.setBoardName(b.getName());
        a.setBoardDisplayName(b.getDisplayName());
        this.article.setValue(a);
    }

    // boolean이 아닌 Boolean임을 유의.
    // camel case를 맞춤으로서 isAnonymous라는 field로 mutual data binding을 가능하게 함.
    public Boolean getIsAnonymous(){

        return this.article.getValue().getKind().equals("anonymous");
    }
    public void setIsAnonymous(Boolean isAnonymous){
        Article a = this.article.getValue();
        if (isAnonymous){
            a.setKind("anonymous");
        } else{
            a.setKind("named");
        }
        this.article.setValue(a);
    }

    public void writeArticle(){
        Call<Article> call = articleService.createArticle("application/json", this.article.getValue());
        call.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                Toast.makeText(context, "게시물을 작성했습니당!", Toast.LENGTH_SHORT).show();
                ((ArticleWriteActivity)context).finish();
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                Log.d(TAG, "onFailure: ", t);
                Toast.makeText(context, "게시물을 작성 실패 ㅜㅜ", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void uploadImages(List<Image> images) throws IOException {
        for(Image img: images) {
            uploadImage(img);
        }
    }

    /**
     * 갤러리에서 선택한 이미지를 바로 업로드 한 뒤 그 url을 this.article의 images에 append한다.
     * @param img
     * @throws IOException
     */
    public void uploadImage(Image img) throws IOException {
        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, img.getUri());
        uploadingBitmaps.getValue().add(imageBitmap);
        uploadingBitmaps.postValue(uploadingBitmaps.getValue());
        File file = new File(img.getPath());

        Call<ImageUploadResponse> call = imageService.uploadImage(
                "Bearer " + KhumuApplication.getToken(),
                MultipartBody.Part.createFormData("image", img.getUri().getLastPathSegment(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file)));
        call.enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                String uploadedFileName = response.body().getData().get("file_name");
                List tmpImgSrcs = article.getValue().getImages();
                tmpImgSrcs.add(uploadedFileName);
//                article.getValue().setImages(tmpImgSrcs);
//                article.postValue(article.getValue());
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    private Article generateInitialArticle(){
        Article initial = new Article();
        // Inject initial Data
        initial.setBoardDisplayName(INITIAL_BOARD_DISPLAY_NAME);
        initial.setKind("anonymous");
        initial.setTitle("tmp");
        return initial;
    }

}
