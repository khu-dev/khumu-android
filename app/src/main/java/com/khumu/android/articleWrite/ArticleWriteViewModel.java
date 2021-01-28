package com.khumu.android.articleWrite;

import android.content.ClipData;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.data.Board;
import com.khumu.android.data.article.Article;
import com.khumu.android.repository.ArticleRepository;
import com.khumu.android.repository.BoardRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArticleWriteViewModel extends ViewModel {
    private final static String TAG = "ArticleWriteViewModel";
    public BoardRepository boardRepository;
    public ArticleRepository articleRepository;
    private MutableLiveData<List<Board>> boards;
    private MutableLiveData<Article> article;
    private MutableLiveData<List<Bitmap>> uploadingBitmaps;
    private ContentResolver contentResolver; // Bitmap 그릴 때 사용

    public ArticleWriteViewModel(BoardRepository boardRepository, ArticleRepository articleRepository, ContentResolver contentResolver){
        this.boardRepository = boardRepository;
        this.articleRepository = articleRepository;
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
    }
}
