package com.khumu.android.articleWrite;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.esafirm.imagepicker.model.Image;
import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;
import com.khumu.android.data.rest.BoardListResponse;
import com.khumu.android.data.rest.ImageUploadResponse;
import com.khumu.android.repository.ArticleService;
import com.khumu.android.repository.BoardService;
import com.khumu.android.repository.ImageService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    public BoardService boardService;
    public ArticleService articleService;
    public ImageService imageService;
    private MutableLiveData<List<Board>> boards;
    // 작성 중인 게시물
    private MutableLiveData<Article> article;
    // 업로드 중인 이미지를 표시하는 recycler view가 사용하는 data
    // 문제점: article의 images 만으로는 recylcer view를 제대로 이용할 수가 없어서 uploadingImagePaths를 또 정의해서 이용 중인데
    // 이러면 article.images와 자동으로 동기화가 안됨.
    private MutableLiveData<List<ImagePath>> uploadingImagePaths;
    private Context context;

    public ArticleWriteViewModel(Context context, BoardService boardService, ArticleService articleService, ImageService imageService){
        this.context = context;
        this.boardService = boardService;
        this.articleService = articleService;
        this.imageService = imageService;
        this.boards = new MutableLiveData<>();
        this.article = new MutableLiveData<>(generateInitialArticle());
        this.uploadingImagePaths = new MutableLiveData<>(new ArrayList<>());

        listBoards();
    }

    public LiveData<Article> getLiveArticle(){
        return article;
    }

    public MutableLiveData<List<Board>> getLiveBoards(){
        return boards;}

    public MutableLiveData<List<ImagePath>> getUploadingImagePaths() {
        return uploadingImagePaths;
    }

    /**
     * article 데이터를 가져옴.
     * 동시에 uploading ImagePaths에도 article.images를 적용해야함.
     * @param a
     */
    public void setArticle(Article a) {
        article.setValue(a);
        List<ImagePath> uploadedImagePaths = new ArrayList<>();
        for (String img : a.getImages()) {
            uploadedImagePaths.add(new ImagePath(img));
        }
        uploadingImagePaths.postValue(uploadedImagePaths);
        Log.d(TAG, "setArticle: " + article.getValue().getBoardDisplayName() + article.getValue().getBoardName());
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

    public void writeArticle(Callback<Article> callback){
        Call<Article> call = articleService.createArticle("application/json", this.article.getValue());
        call.enqueue(callback);
    }

    public void modifyArticle(Callback<Article> callback){
        Call<Article> call = articleService.updateArticle("application/json", this.article.getValue().getId(), this.article.getValue());
        call.enqueue(callback);
    }

    public void uploadImages(List<Image> images) throws IOException {
        new Thread() {
            @Override
            public void run() {
                for (Image img : images) {
                    uploadImage(img);
                }
            }
        }.start();
    }

    /**
     * 갤러리에서 이미지를 선택한 뒤 바로 업로드. 그 upload한 뒤 얻은 url을 this.article의 images에 append한다.
     * local uri를 이용해 Glide로 미리 보기 제공
     * 사실 Callback을 인자로 받아서 Callback 시의 UI 로직은 외부에서 받고 싶음.
     * @param img
     * @throws IOException
     */
    public void uploadImage(Image img) {
        File file = new File(img.getPath());

        Call<ImageUploadResponse> call = imageService.uploadImage(
                "Bearer " + KhumuApplication.getToken(),
                MultipartBody.Part.createFormData("image", img.getUri().getLastPathSegment(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file)));

        Response<ImageUploadResponse> resp = null;
        try {
            resp = call.execute();
            if (resp.isSuccessful()){
                String hashedFileName = resp.body().getData().get("file_name");
                ImagePath p = new ImagePath(hashedFileName, img.getUri());
                Log.d(TAG, "uploadImage: " + p.getUriPath());
                // recycler view가 uri로 이미지를 보여주기 위함.
                uploadingImagePaths.getValue().add(p);
                uploadingImagePaths.postValue(uploadingImagePaths.getValue());
                // 현재 article에 url을 추가하기 위함.
                article.getValue().getImages().add(hashedFileName);
                ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, "업로드 성공!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else{
                ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, "업로드 실패!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (IOException e) {
            ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context, "IOException으로 업로드 실패!", Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
    }

    /**
     * 이미지를 삭제한다. article.images와 uploadingImagePaths 모두를 삭제해야한다.
     * @param imagePath
     * @return
     */
    public boolean deleteImage(ImagePath imagePath){
        // 보여지는 image path에서 삭제
        boolean isSuccessful = uploadingImagePaths.getValue().remove(imagePath);
        uploadingImagePaths.postValue(uploadingImagePaths.getValue());
        // article의 images에서 file name 삭제
        isSuccessful = isSuccessful && article.getValue().getImages().remove(imagePath.getHashedFileName());
        article.setValue(article.getValue());
        Log.d(TAG, "deleteUploadingImage: " + article.getValue().getImages());
        return isSuccessful;
    }

    /**
     * @return 초기 데이터로 사용할 Article
     */
    private Article generateInitialArticle(){
        Article initial = new Article();
        // Inject initial Data
        initial.setBoardDisplayName(INITIAL_BOARD_DISPLAY_NAME);
        initial.setKind("anonymous");
        initial.setTitle("tmp");
        return initial;
    }

    /**
     * board를 선택하기 위한 list를 로드
     */
    private void listBoards(){
        Call<BoardListResponse> call = boardService.getBoards();
        call.enqueue(new Callback<BoardListResponse>() {
            @Override
            public void onResponse(Call<BoardListResponse> call, Response<BoardListResponse> response) {
                boards.postValue(response.body().getData());
            }

            @Override
            public void onFailure(Call<BoardListResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }

}
