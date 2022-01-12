/**
 * BaseFeedFragment를 상속받아 크게 추가할 내용은 없고, abstract method인 provideViewModel작업만 정의해주면됨.
 * 기본적인 feed의 layout인 layout_feed.xml을 이용.
 */
package com.khumu.android.signUp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.component.AutoDismissAlertDialog;
import com.khumu.android.data.Info21UserInfo;
import com.khumu.android.data.rest.DefaultResponse;
import com.khumu.android.data.rest.UserResponse;
import com.khumu.android.databinding.FragmentSignUpAdditionalFormBinding;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdditionalFormFragment extends Fragment {
    private final static String TAG = "AdditionalFormFragment";
    private SignUpViewModel signUpViewModel;
    private FragmentSignUpAdditionalFormBinding binding;

    public AdditionalFormFragment(SignUpViewModel viewModel) {
        this.signUpViewModel = viewModel;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        KhumuApplication.applicationComponent.inject(this);
        this.signUpViewModel = new ViewModelProvider(this.getActivity()).get(SignUpViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_additional_form, container, false);
        // binding하며 사용할 Fragment가 사용하는 변수인 viewModel을 설정해줌.
        binding.setViewModel(this.signUpViewModel);
        binding.setFragment(this);
        // LiveData를 이용해 Observe하기 위해선 그 LifeCyclerOwner가 꼭 필요하다!
        // 그렇지 않으면 유효하게 Observer로 동작하지 않고 아무 변화 없음...
        binding.setLifecycleOwner(this.getActivity());
        binding.emailEt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    binding.signUpBtn.callOnClick();
                    return false;
                }
                return false;
            }
        });
        View root = binding.getRoot();
        return root;
    }

    public void onClickNextBTN(View view) {
        Context context = AdditionalFormFragment.this.getActivity();
        Info21SignUpActivity activity = (Info21SignUpActivity) context;
        SweetAlertDialog progressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(ContextCompat.getColor(context, R.color.red_500));
        progressDialog.setTitleText("회원가입 중입니다");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(){
            @Override
            public void run() {
                UserResponse resp = signUpViewModel.signUp();
                progressDialog.dismiss();
                // 가입 실패
                if (resp.getData() == null && resp.getMessage() != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            AutoDismissAlertDialog errorDialog = new AutoDismissAlertDialog(context, SweetAlertDialog.ERROR_TYPE, "회원가입을 완료하지 못했습니다.", resp.getMessage(), 2000L);
                            errorDialog.getProgressHelper().setBarColor(ContextCompat.getColor(context, R.color.red_500));
                            errorDialog.setCancelable(false);
                            errorDialog.show();
                        }
                    });
                } else{
                    // 가입 성공
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            signUpViewModel.postUser(signUpViewModel.getUsername().getValue());
                            SweetAlertDialog successDialog = new SweetAlertDialog(context, SweetAlertDialog.BUTTON_CONFIRM);
                            successDialog.setTitle("회원가입을 성공했습니다.");
                            successDialog.setContentText(signUpViewModel.getNickname().getValue() + "님 환영합니다 >_<");
                            successDialog.setCancelable(false);
                            successDialog.setConfirmText("로그인하러 가기");
                            successDialog.show();
                            successDialog.setOnDismissListener(v ->{
                                activity.proceedSignUpStep();
                            });
                        }
                    });

                }
            }
        }.start();
    }
}