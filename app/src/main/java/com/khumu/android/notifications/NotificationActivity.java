//package com.khumu.android;
//
//import android.os.Bundle;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        Helper.Init();
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
////        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
////                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
////                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
////        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
//    }
//
//}

package com.khumu.android.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.KhumuApplication;
import com.khumu.android.MainActivity;
import com.khumu.android.R;
import com.khumu.android.data.KhumuJWT;
import com.khumu.android.data.Notification;
import com.khumu.android.data.Tag;
import com.khumu.android.data.rest.JWTRequest;
import com.khumu.android.data.rest.JWTResponse;
import com.khumu.android.databinding.ActivityNotificationsBinding;
import com.khumu.android.myPage.ArticleTagAdapter;
import com.khumu.android.myPage.MyPageViewModel;
import com.khumu.android.retrofitInterface.NotificationService;
import com.khumu.android.retrofitInterface.TokenService;
import com.khumu.android.signUp.SignUpActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    final static String TAG = "NotificationActivity";
    @Inject
    NotificationService notificationService;
    NotificationsViewModel viewModel;

    @BindingAdapter("notification_list")
    public static void bindItem(RecyclerView recyclerView, LiveData<List<Notification>> newNotifications){
        NotificationAdapter adapter = (NotificationAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            System.out.println("NotificationActivity.bindItem");
            adapter.setNotificationList(newNotifications.getValue());
            adapter.notifyItemRangeInserted(0, newNotifications.getValue().size());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
        KhumuApplication.container.inject(this);
        ActivityNotificationsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_notifications);
        List<Notification> tmp = new ArrayList<>();
        tmp.add(new Notification(1L));
        tmp.add(new Notification(2L));
        tmp.add(new Notification(3L));
        binding.notificationRecyclerView.setAdapter(new NotificationAdapter(this, tmp));
        binding.setLifecycleOwner(this);

        this.viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new NotificationsViewModel(notificationService, new ArrayList<Notification>());
            }
        }).get(NotificationsViewModel.class);
        binding.setViewModel(this.viewModel);
        this.viewModel.listNotifications();
    }

}