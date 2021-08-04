package com.khumu.android.signUp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.rest.UserResponse;
import com.khumu.android.databinding.ActivityInfo21SignUpBinding;
import com.khumu.android.login.LoginActivity;
import com.khumu.android.qrCode.QrCodeViewModel;
import com.khumu.android.repository.UserService;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class Info21SignUpActivity extends AppCompatActivity {
    SignUpViewModel viewModel;
    ActivityInfo21SignUpBinding binding;
    @Inject
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KhumuApplication.applicationComponent.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_info21_sign_up);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory(){
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SignUpViewModel(Info21SignUpActivity.this, userService);
            }
        }).get(SignUpViewModel.class);

        binding.setViewModel(viewModel);
    }

    public void onClickSignUpBTN(View view) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("회원가입 중입니다.");
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
        progressDialog.show();
        userService.signUp("application/json", viewModel.getUser().getValue()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.code() == 201) {
                    Toast.makeText(Info21SignUpActivity.this, "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Info21SignUpActivity.this, LoginActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Info21SignUpActivity.this.startActivity(intent);
                } else {
                    // 400은 validation error. wrong value
                    if (response.code() == 400) {
                        // wrong value 에러 메시지 출력
                        // di container 속 retrofit instance를 load
                        Converter<ResponseBody, UserResponse> errorConverter =
                                KhumuApplication.applicationComponent.getRetrofit().responseBodyConverter(UserResponse.class, new Annotation[0]);
                        try {
                            UserResponse errorBody = errorConverter.convert(response.errorBody());
                            Toast.makeText(Info21SignUpActivity.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            // 에러 바디 없는 경우.
                            Toast.makeText(Info21SignUpActivity.this, "알 수 없는 오류가 발생했습니다. 쿠뮤에 문의해주세요.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } else{
                        Toast.makeText(Info21SignUpActivity.this, "알 수 없는 오류가 발생했습니다. 쿠뮤에 문의해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                progressDialog.dismiss();
                t.printStackTrace();
                Toast.makeText(Info21SignUpActivity.this, "서버와 통신할 수 없습니다. 쿠뮤에 문의해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
