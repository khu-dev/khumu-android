package com.khumu.android.login;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.KhumuApplication;
import com.khumu.android.MainActivity;
import com.khumu.android.R;
import com.khumu.android.data.Announcement;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;
import com.khumu.android.data.KhumuJWT;
import com.khumu.android.data.rest.ArticleListResponse;
import com.khumu.android.data.rest.BoardListResponse;
import com.khumu.android.data.rest.JWTErrorResponse;
import com.khumu.android.data.rest.JWTRequest;
import com.khumu.android.data.rest.JWTResponse;
import com.khumu.android.repository.ArticleService;
import com.khumu.android.repository.BoardService;
import com.khumu.android.repository.TokenService;
import com.khumu.android.util.FcmManager;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dagger.Module;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

@Getter
@Setter
@Module
@RequiredArgsConstructor
public class LoginViewModel extends ViewModel {
    private final static String TAG = "LoginViewModel";
    private MutableLiveData<String> username = new MutableLiveData<>("");
    private MutableLiveData<String> password = new MutableLiveData<>("");
    private final LoginActivity activity;
    private final TokenService tokenService;
    private final FcmManager fcmManager;

    public void login(){
        SweetAlertDialog progressDialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(ContextCompat.getColor(activity, R.color.red_500));
        progressDialog.setTitleText("로그인 중입니다.");
        progressDialog.setCancelable(false);
        progressDialog.show();
        try {
            System.out.println("username: " + username.getValue());
            System.out.println(password.getValue());
            Call<JWTResponse> call = tokenService.postToken("application/json", new JWTRequest(username.getValue(), password.getValue()));
            call.enqueue(new Callback<JWTResponse>() {
                @Override
                public void onResponse(Call<JWTResponse> call, Response<JWTResponse> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: " + response.raw());
                        Log.d(TAG, "onResponse: " + response.body().getAccess());
                        progressDialog.dismiss();
                        KhumuJWT jwt = new KhumuJWT(response.body().getAccess());
                        KhumuApplication.setKhumuConfig(jwt.getUsername(), jwt.getNickname(), jwt.toString(), KhumuApplication.getPushToken());
                        KhumuApplication.loadKhumuConfig();
                        fcmManager.createOrUpdatePushSubscription();
                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                    } else {
                        // 잘못된 credential로 인한 권한 없음.
                        if (response.code() == 401) {
                            // wrong value 에러 메시지 출력
                            // di container 속 retrofit instance를 load
                            Converter<ResponseBody, JWTErrorResponse> errorConverter =
                                    KhumuApplication.applicationComponent.getRetrofit().responseBodyConverter(JWTErrorResponse.class, new Annotation[0]);
                            try {
                                JWTErrorResponse errorBody = errorConverter.convert(response.errorBody());
                                Toast.makeText(activity, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                // 에러 바디 없는 경우.
                                e.printStackTrace();
                            }
                        } else{
                            Intent intent = new Intent(activity, LoginActivity.class);
                            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                            activity.startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<JWTResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d(TAG, "onFailure: ", t);
                    Toast.makeText(activity, "서버와 통신할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "로그인 관련 알 수 없는 문제 발생.\n관리자에게 문의해주세요..ㅜㅜ", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public boolean isLoginEnabled(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }
}