package com.khumu.android.articleWrite;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Board;
import com.khumu.android.data.Article;
import com.khumu.android.data.rest.ImageUploadResponse;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;
import com.khumu.android.retrofitInterface.ImageService;

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

    public void uploadImages(List<ClipData.Item> imageClipDataItems) throws IOException {
        for(ClipData.Item item: imageClipDataItems) {
            uploadImage(item);
        }
    }
    public void uploadImage(ClipData.Item imageClipDataItem) throws IOException {
        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageClipDataItem.getUri());
        uploadingBitmaps.postValue(Collections.singletonList(imageBitmap));
        File file = new File(context.getCacheDir(), imageClipDataItem.getUri().getPath());

//        File file = new File("content://media/external/images/media/22800/ORIGINAL/NONE/image/jpeg/1659461618");
        System.out.println(imageClipDataItem.getUri().getPath());
        System.out.println(file.canRead());

        Call<ImageUploadResponse> call = imageService.uploadImage(
                "Bearer " + KhumuApplication.getToken(),
                MultipartBody.Part.createFormData("image", imageClipDataItem.getUri().getLastPathSegment(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file)));
        call.enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {

                System.out.println(response.body().message);
                System.out.println(response.body().data);
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }
}
