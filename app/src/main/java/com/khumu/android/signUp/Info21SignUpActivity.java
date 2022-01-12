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
import com.khumu.android.repository.AnnouncementService;
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
    @Inject
    AnnouncementService announcementService;
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
                return (T) new SignUpViewModel(Info21SignUpActivity.this, retrofit, userService, announcementService);
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
