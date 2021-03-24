package com.khumu.android.qrCode;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Notification;
import com.khumu.android.data.rest.NotificationListResponse;
import com.khumu.android.data.rest.NotificationReadRequest;
import com.khumu.android.data.rest.QrCodeGetResponse;
import com.khumu.android.repository.NotificationService;
import com.khumu.android.repository.QrCodeService;

import java.io.IOException;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
@Setter
public class QrCodeViewModel extends ViewModel {
    final String TAG = "QrCodeViewModel";
    QrCodeService qrCodeService;
    MutableLiveData<QrCodeGetResponse> qrCodeResponse;


    public QrCodeViewModel(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
        qrCodeResponse = new MutableLiveData<>();
    }

    public void getQrCode() {
        qrCodeService.getQrCode().enqueue(new Callback<QrCodeGetResponse>() {
            @Override
            public void onResponse(Call<QrCodeGetResponse> call, Response<QrCodeGetResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    qrCodeResponse.postValue(response.body());
                    System.out.println(response.body().getData());
                    System.out.println(response.body().getData().getQrCodeStr());
                } else {
                    System.out.println("QR 코드를 가져오는 작업 실패");
                }
            }

            @Override
            public void onFailure(Call<QrCodeGetResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}