package com.khumu.android.articleWrite;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.esafirm.imagepicker.model.Image;
import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
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

import lombok.Getter;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
@Setter
public class ArticleWriteViewModel extends ViewModel {
    final static String TAG = "ArticleWriteViewModel";
    final String INITIAL_BOARD_DISPLAY_NAME = "게시판을 선택해주세요.";
    BoardService boardService;
    ArticleService articleService;
    ImageService imageService;
    MutableLiveData<List<Board>> boards;
    MutableLiveData<Board> currentBoard;
    // 작성 중인 게시물
    MutableLiveData<Article> article;
    // 업로드 중인 이미지를 표시하는 recycler view가 사용하는 data
    // 문제점: article의 images 만으로는 recylcer view를 제대로 이용할 수가 없어서 uploadingImagePaths를 또 정의해서 이용 중인데
    // 이러면 article.images와 자동으로 동기화가 안됨.
    MutableLiveData<List<ImagePath>> uploadingImagePaths;
    MutableLiveData<Boolean> isWritable;
    Context context;

    // 원래는 필요한 값들 다 주입 받는 게 좋은데...
    // 얜 좀 번거로워서 그냥 여기서 설정..
    public ArticleWriteViewModel(Context context, BoardService boardService, ArticleService articleService, ImageService imageService){
        this.context = context;
        this.boardService = boardService;
        this.articleService = articleService;
        this.imageService = imageService;
        this.boards = new MutableLiveData<>(new ArrayList<>());
        this.currentBoard = new MutableLiveData<>(provideInitialCurrentBoard());
        this.article = new MutableLiveData<>(provideInitialArticle());
        this.uploadingImagePaths = new MutableLiveData<>(new ArrayList<>());
        this.isWritable = new MutableLiveData<>(Boolean.FALSE);

        // current board의 value가 변경되면 writable한 지도 업데이트.
        // article은 observe할 수 없는 이유 - article은 article의 값이 변경되는 게 아니라 내부 property인 title이 변경되기 때문
        this.currentBoard.observe((LifecycleOwner) context, board -> updateIsWritable());

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
     * article write이 아니라 수정인 경우 초기 article 정보를 가져와 저장시킴.
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


    /**
     * 내가 게시물을 적을 게시판을 선택한다. 그 내용을 MutableLiveData인 article에 반영
      */
    public void setBoardToWrite(Board b){
        Article a = this.article.getValue();
        a.setBoardName(b.getName());
        a.setBoardDisplayName(b.getDisplayName());
        this.currentBoard.setValue(b);
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
        for (Image img : images) {
            uploadImage(img);
        }
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
                Log.i(TAG, "uploadImage: 성공");
            } else{
                Log.e(TAG, "uploadImage: 실패");
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
    private Article provideInitialArticle(){
        Article initial = new Article();
        // Inject initial Data
        initial.setKind("anonymous");
        return initial;
    }

    private Board provideInitialCurrentBoard() {
        Board board = new Board();
        board.setDisplayName(INITIAL_BOARD_DISPLAY_NAME);
        return board;
    }

    /**
     * board를 선택하기 위한 list를 로드
     */
    private void listBoards(){
        Call<BoardListResponse> call = boardService.getFollowingBoards("application/json", true);
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

    public void updateIsWritable() {
        // 글쓰기 버튼 활성화 조건
        if (article.getValue() != null) {
            Log.d(TAG, "getIsWritable: " + (article.getValue().getTitle() != null));
            if (article.getValue().getTitle() != null) {
                Log.d(TAG, "getIsWritable: " + (article.getValue().getTitle().length() > 0));
            }
        }

        this.isWritable.postValue(
                this.currentBoard.getValue() != null &&
                this.currentBoard.getValue().name != null &&
                article.getValue() != null &&
                article.getValue().getTitle() != null &&
                article.getValue().getTitle().length() > 0
        );
    }
}
