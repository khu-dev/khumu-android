package com.khumu.android.signUp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.databinding.ActivityInfo21SignUpBinding;
import com.khumu.android.qrCode.QrCodeViewModel;
import com.khumu.android.repository.UserService;

import javax.inject.Inject;

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
        viewModel.signUp();
    }
}
