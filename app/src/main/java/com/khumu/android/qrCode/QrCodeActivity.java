package com.khumu.android.qrCode;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.KhumuUser;
import com.khumu.android.data.rest.QrCodeGetResponse;
import com.khumu.android.databinding.ActivityQrCodeBinding;
import com.khumu.android.di.component.ApplicationComponent;
import com.khumu.android.feed.FeedViewModel;
import com.khumu.android.repository.KhumuUserRepository;
import com.khumu.android.repository.QrCodeService;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrCodeActivity extends AppCompatActivity {
    private static final String TAG = "QrCodeActivity";
    final int QR_CODE_WIDTH=200;
    final int QR_CODE_HEIGHT=200;
    @Inject
    QrCodeService qrCodeService;
    ActivityQrCodeBinding binding;
    QrCodeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KhumuApplication.applicationComponent.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_code);
        binding.setLifecycleOwner(this);
        binding.setActivity(this);
        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory(){
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new QrCodeViewModel(qrCodeService);
            }
        }).get(QrCodeViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.getQrCode();
    }

    public Bitmap getQRCodeBitmap(String contents) {
        // 아직 qrcode에 대한 데이터를 성공적으로 가져오지 않은 경우 contents는 null이다.
        if (contents != null) {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            try {
                BitMatrix matrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT);
                int height = matrix.getHeight();
                int width = matrix.getWidth();
                Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }
                return bmp;

            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        return BitmapFactory.decodeResource(this.getResources(), (R.drawable.background_round_white));
    }

    // memory의 bitmap을 image view에 적용하는 방법
    // https://stackoverflow.com/questions/35304185/databinding-an-in-memory-bitmap-to-an-imageview
    @BindingAdapter("bind:imageBitmap")
    public static void loadImage(ImageView iv, Bitmap bitmap) {
        System.out.println("setImage...");
        iv.setImageBitmap(bitmap);
    }
}
