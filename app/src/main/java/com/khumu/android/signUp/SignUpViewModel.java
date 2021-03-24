package com.khumu.android.signUp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.data.KhumuUser;
import com.khumu.android.data.rest.QrCodeGetResponse;
import com.khumu.android.login.LoginActivity;
import com.khumu.android.repository.QrCodeService;
import com.khumu.android.repository.UserService;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
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
        userService.signUp("application/json", user.getValue()).enqueue(new Callback<KhumuUser>() {
            @Override
            public void onResponse(Call<KhumuUser> call, Response<KhumuUser> response) {
                if (!response.isSuccessful() || response.code() != 201) {
                    Toast.makeText(context, "회원가입에 실패했습니다. ㅜㅜ", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println(response.body());
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<KhumuUser> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "알 수 없는 이유로 회원가입에 실패했습니다. ㅜㅜ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}