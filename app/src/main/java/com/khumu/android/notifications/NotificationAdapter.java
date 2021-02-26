package com.khumu.android.notifications;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.R;
import com.khumu.android.articleWrite.ImageAdapter;
import com.khumu.android.data.Article;
import com.khumu.android.data.Notification;
import com.khumu.android.databinding.ActivityNotificationsBinding;
import com.khumu.android.databinding.LayoutNotificationItemBinding;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private final static String TAG = "NotificationAdapter";
    private List<Notification> notifications;
    private Context context;

    public NotificationAdapter(@NonNull Context context, @NonNull List<Notification> notifications) {
        // 세 번째 인자가 이 adpater의 collection을 의미
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutNotificationItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_notification_item, parent, false);
        System.out.println("NotificationAdapter.onCreateViewHolder");
        return new NotificationViewHolder(binding, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification n = notifications.get(position);
        holder.binding.setNotification(n);
        System.out.println("NotificationAdapter.onBindViewHolder + binded" + n.getContent());
    }

    @Override
    public long getItemId(int position) {
        return notifications.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void setNotificationList(List<Notification> notifications) {
        this.notifications.clear();
        this.notifications.addAll(notifications);
    }

    public void show() {
        for (Notification n : this.notifications) {
            System.out.println("NotificationAdapter.show");
            System.out.println(n.getId());
            System.out.println(n.getContent());
        }
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        private LayoutNotificationItemBinding binding;
        private Context context;

        // 이 view는 아마도 recycler view?
        public NotificationViewHolder(LayoutNotificationItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }
    }
}
