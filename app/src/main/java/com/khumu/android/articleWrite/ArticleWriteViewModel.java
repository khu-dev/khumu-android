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
    public BoardRepository boardRepository;
    public ArticleRepository articleRepository;
    public ImageService imageService;
    private MutableLiveData<List<Board>> boards;
    private MutableLiveData<Article> article;
    private MutableLiveData<List<Bitmap>> uploadingBitmaps;
    private ContentResolver contentResolver; // Bitmap 그릴 때 사용
    private Context context;

    public ArticleWriteViewModel(Context context, BoardRepository boardRepository, ArticleRepository articleRepository, ImageService imageService, ContentResolver contentResolver){
        this.context = context;
        this.boardRepository = boardRepository;
        this.articleRepository = articleRepository;
        this.imageService = imageService;
//        this.boards = new MutableLiveData<>(boardRepository.ListBoards());
        this.article = new MutableLiveData<>(new Article());
        this.uploadingBitmaps = new MutableLiveData<>(new ArrayList<>());
        this.contentResolver = contentResolver;
    }

    public LiveData<Article> getArticle(){
        return article;
    }

    public MutableLiveData<List<Bitmap>> getUploadingBitmaps() {
        return uploadingBitmaps;
    }

    public void uploadImages(List<Image> images) throws IOException {
        for(Image img: images) {
            uploadImage(img);
        }
    }
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
                article.getValue().setImages(tmpImgSrcs);
                article.postValue(article.getValue());
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }
}
