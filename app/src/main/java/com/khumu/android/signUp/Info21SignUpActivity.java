package com.khumu.android.signUp;

import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.component.AutoDismissAlertDialog;
import com.khumu.android.data.rest.UserResponse;
import com.khumu.android.databinding.ActivityInfo21SignUpBinding;
import com.khumu.android.login.LoginActivity;
import com.khumu.android.qrCode.QrCodeViewModel;
import com.khumu.android.repository.UserService;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.inject.Inject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Info21SignUpActivity extends AppCompatActivity {
    SignUpViewModel viewModel;
    ActivityInfo21SignUpBinding binding;
    @Inject
    Retrofit retrofit;
    @Inject
    UserService userService;
    SignUpAgreementFragment signUpAgreementFragment;
    Info21AuthenticationFragment info21AuthenticationFragment;
    AdditionalFormFragment additionalFormFragment;

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
                return (T) new SignUpViewModel(Info21SignUpActivity.this, retrofit, userService);
            }
        }).get(SignUpViewModel.class);

        binding.setViewModel(viewModel);
        signUpAgreementFragment = new SignUpAgreementFragment(viewModel);
        info21AuthenticationFragment = new Info21AuthenticationFragment(viewModel);
        additionalFormFragment = new AdditionalFormFragment(viewModel);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.sign_up_frame_layout, signUpAgreementFragment);
        transaction.commit();
    }

//    public void onClickNextBTN(View view) {
//        proceedSignUp();
//        SweetAlertDialog progressDialog = new SweetAlertDialog(Info21SignUpActivity.this, SweetAlertDialog.PROGRESS_TYPE);
//        progressDialog.getProgressHelper().setBarColor(ContextCompat.getColor(Info21SignUpActivity.this, R.color.red_500));
//        progressDialog.setTitleText("회원가입 중입니다");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        userService.signUp("application/json", viewModel.getUser().getValue()).enqueue(new Callback<UserResponse>() {
//            @Override
//            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                progressDialog.dismiss();
//                if (response.isSuccessful() && response.code() == 201) {
//                    new SweetAlertDialog(Info21SignUpActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                            .setTitleText("쿠뮤 회원가입 완료")
//                            .setContentText(viewModel.getUser().getValue().getNickname() + "님 환영합니다 >_<")
//                            .show();
//                    Intent intent = new Intent(Info21SignUpActivity.this, LoginActivity.class);
//                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    Info21SignUpActivity.this.startActivity(intent);
//                } else {
//                    // 400은 validation error. wrong value
//                    if (response.code() == 400) {
//                        // wrong value 에러 메시지 출력
//                        // di container 속 retrofit instance를 load
//                        Converter<ResponseBody, UserResponse> errorConverter =
//                                KhumuApplication.applicationComponent.getRetrofit().responseBodyConverter(UserResponse.class, new Annotation[0]);
//                        try {
//                            UserResponse errorBody = errorConverter.convert(response.errorBody());
//                            SweetAlertDialog errorDialog = new AutoDismissAlertDialog(Info21SignUpActivity.this, SweetAlertDialog.ERROR_TYPE, "회원가입에 실패했습니다 ㅜㅜ", errorBody.getMessage(), 1000L);
////                                    .hideConfirmButton()
////                                    .setTitleText()
////                                    .setContentText();
////                            Window window = errorDialog.getWindow();
////                            window.setGravity(Gravity.BOTTOM);
////                            window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
//                            errorDialog.show();
//                        } catch (IOException e) {
//                            // 에러 바디 없는 경우.
//                            Toast.makeText(Info21SignUpActivity.this, "알 수 없는 오류가 발생했습니다. 쿠뮤에 문의해주세요.", Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    } else{
//                        Toast.makeText(Info21SignUpActivity.this, "알 수 없는 오류가 발생했습니다. 쿠뮤에 문의해주세요.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserResponse> call, Throwable t) {
//                progressDialog.dismiss();
//                t.printStackTrace();
//                Toast.makeText(Info21SignUpActivity.this, "서버와 통신할 수 없습니다. 쿠뮤에 문의해주세요.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    // 회원가입하는 동안 next 버튼이 할 일들
    public void proceedSignUpStep() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (signUpAgreementFragment.isVisible()) {
            transaction.replace(R.id.sign_up_frame_layout, info21AuthenticationFragment);
            transaction.commit();
        } else if (info21AuthenticationFragment.isVisible()) {
            transaction.replace(R.id.sign_up_frame_layout, additionalFormFragment);
            transaction.commit();
        } else{
            this.finish();
        }
    }
}
