package com.khumu.android.retrofitInterface;

import com.khumu.android.data.rest.BoardListResponse;
import com.khumu.android.data.rest.NotificationListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface NotificationService {
    @GET("notifications")
    Call<NotificationListResponse> getNotifications (@Query("recipient") String recipient);
}
