package com.khumu.android.signUp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.common.api.Api;
import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.Info21UserInfo;
import com.khumu.android.data.KhumuUser;
import com.khumu.android.data.rest.DefaultResponse;
import com.khumu.android.data.rest.Info21AuthenticationRequest;
import com.khumu.android.data.rest.QrCodeGetResponse;
import com.khumu.android.data.rest.UserResponse;
import com.khumu.android.login.LoginActivity;
import com.khumu.android.repository.QrCodeService;
import com.khumu.android.repository.UserService;

import java.io.IOException;
import java.lang.annotation.Annotation;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

@Getter
@Setter
public class SignUpViewModel extends ViewModel {
    private final String TAG = "QrCodeViewModel";
    private MutableLiveData<KhumuUser> user;
    private MutableLiveData<Boolean> isAgreedPolicy;
    private MutableLiveData<Boolean> isAgreedPrivacy;

    private Context context;
    private UserService userService;
    private Retrofit retrofit;


    public SignUpViewModel(Context context, Retrofit retrofit, UserService userService) {
        this.context = context;
        this.retrofit = retrofit;
        this.userService = userService;
        Log.w(TAG, "QrCodeViewModel: " + userService );
        this.user = new MutableLiveData<>(new KhumuUser());
        this.isAgreedPolicy = new MutableLiveData<>(false);
        this.isAgreedPrivacy = new MutableLiveData<>(false);
    }

    public String getWelcomeMessage() {
        KhumuUser user = this.user.getValue();
        return user.getDepartment() + "학과 " + user.getStudentNumber() + "님\n" + "환영합니다!";
    }

    public UserResponse signUp() {
        Call<UserResponse> call= userService.signUp("application/json", user.getValue());
        try {
            Response<UserResponse> response = call.execute();
            if (!response.isSuccessful()) {
                UserResponse errorResp = (UserResponse) retrofit.responseBodyConverter(UserResponse.class, UserResponse.class.getAnnotations()).convert(response.errorBody());
                return errorResp;

            } else{
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new UserResponse(null, "서버의 응답을 해석할 수 없습니다.");
        }
    }
    public DefaultResponse<Info21UserInfo> verifyNewStudent() {
        Call<DefaultResponse<Info21UserInfo>> call= userService.verifyNewStudent("application/json", Info21AuthenticationRequest.builder().username(user.getValue().getUsername()).password(user.getValue().getPassword()).build());
        try {
            Response<DefaultResponse<Info21UserInfo>> response = call.execute();
            if (!response.isSuccessful()) {
                DefaultResponse<Info21UserInfo> errorResp = (DefaultResponse<Info21UserInfo>) retrofit.responseBodyConverter(DefaultResponse.class, DefaultResponse.class.getAnnotations()).convert(response.errorBody());
                return errorResp;

            } else{
                Info21UserInfo userInfo = response.body().getData();
                KhumuUser user = this.user.getValue();
                user.setStudentNumber(userInfo.getStudentNum());
                user.setDepartment(userInfo.getDept());

                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new DefaultResponse<Info21UserInfo>(null, "서버의 응답을 해석할 수 없습니다.");
        }
    }
}