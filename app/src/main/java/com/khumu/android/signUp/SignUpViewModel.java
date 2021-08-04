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
}