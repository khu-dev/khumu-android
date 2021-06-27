package com.khumu.android.repository;

import com.khumu.android.data.PushSubscription;
import com.khumu.android.data.rest.NotificationListResponse;
import com.khumu.android.data.rest.NotificationReadRequest;
import com.khumu.android.data.rest.PushSubscriptionResponse;
import com.khumu.android.data.ResourceSubscription;
import com.khumu.android.data.rest.ResourceSubscriptionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface





NotificationService {
    @GET("notifications")
    Call<NotificationListResponse> getNotifications (@Query("recipient") String recipient);

    @PATCH("notifications/{id}")
    Call<Object> patchRead(@Header("Content-Type") String contentType, @Path("id") Long id, @Body NotificationReadRequest body);

    @PATCH("push-subscriptions")
    Call<PushSubscriptionResponse> subscribe(@Header("Content-Type") String contentType, @Body PushSubscription subscription);

    @POST("subscribe")
    Call<Object> subscribeResource(@Header("Content-Type") String contentType, @Body ResourceSubscription subscription);

    @DELETE("subscribe")
    Call<Object> unsubscribeResource(@Header("Content-Type") String contentType, @Body ResourceSubscription subscription);

    @GET("subscriptions/{username}/article/{id}")
    Call<ResourceSubscriptionResponse> getResourceSubscription(@Path("username")String username, @Path("id") int id);

}
