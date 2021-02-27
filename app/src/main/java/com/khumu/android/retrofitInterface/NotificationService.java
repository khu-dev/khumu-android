package com.khumu.android.retrofitInterface;

import com.khumu.android.data.PushSubscription;
import com.khumu.android.data.rest.BoardListResponse;
import com.khumu.android.data.rest.NotificationListResponse;
import com.khumu.android.data.rest.NotificationReadRequest;
import com.khumu.android.data.rest.PushSubscriptionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NotificationService {
    @GET("notifications")
    Call<NotificationListResponse> getNotifications (@Query("recipient") String recipient);

    @PATCH("notifications/{id}")
    Call<Object> patchRead(@Header("Content-Type") String contentType, @Path("id") Long id, @Body NotificationReadRequest body);

    @PATCH("push-subscriptions")
    Call<PushSubscriptionResponse> subscribe(@Header("Content-Type") String contentType, @Body PushSubscription subscription);
}
