package com.khumu.android.signUp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.common.api.Api;
import com.khumu.android.KhumuApplication;
import com.khumu.android.data.KhumuUser;
import com.khumu.android.data.rest.QrCodeGetResponse;
import com.khumu.android.data.rest.UserResponse;
import com.khumu.android.login.LoginActivity;
import com.khumu.android.repository.QrCodeService;
import com.khumu.android.repository.UserService;

import java.io.IOException;
import java.lang.annotation.Annotation;

import lombok.Getter;
import lombok.Setter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

@Getter
@Setter
public class SignUpViewModel extends ViewModel {
    final String TAG = "QrCodeViewModel";
    MutableLiveData<KhumuUser> user;
    Context context;
    UserService userService;

    public SignUpViewModel(Context context, UserService userService) {
        this.context = context;
        this.userService = userService;
        Log.w(TAG, "QrCodeViewModel: " + userService );
        this.user = new MutableLiveData<>(new KhumuUser());
    }

    public void signUp() {
        userService.signUp("application/json", user.getValue()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.code() == 201) {
                        Toast.makeText(context, "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                } else {
                    // 400은 validation error. wrong value
                    if (response.code() == 400) {
                        // wrong value 에러 메시지 출력
                        // di container 속 retrofit instance를 load
                        Converter<ResponseBody, UserResponse> errorConverter =
                                KhumuApplication.applicationComponent.getRetrofit().responseBodyConverter(UserResponse.class, new Annotation[0]);
                        try {
                            UserResponse errorBody = errorConverter.convert(response.errorBody());
                            Toast.makeText(context, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            // 에러 바디 없는 경우.
                            Toast.makeText(context, "알 수 없는 오류가 발생했습니다. 쿠뮤에 문의해주세요.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    } else{
                        Toast.makeText(context, "알 수 없는 오류가 발생했습니다. 쿠뮤에 문의해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "서버와 통신할 수 없습니다. 쿠뮤에 문의해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}