package com.khumu.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.Announcement;
import com.khumu.android.databinding.LayoutSimpleAnnouncementItemBinding;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


public class SimpleAnnouncementAdapter extends RecyclerView.Adapter<SimpleAnnouncementAdapter.SimpleAnnouncementViewHolder>{
    private final static String TAG = "AnnouncementAdapter";
    // Announcement Detail Activity를 열 때 툴바에 어떤 제목을 표시할 것인
    @Getter @Setter
    public List<Announcement> announcementList;
    // Adapter는 바깥 UI 상황을 최대한 모르고싶지만, Toast를 위해 context를 주입함.
    protected Context context;

    public SimpleAnnouncementAdapter(Context context, List<Announcement> announcementList) {
        KhumuApplication.applicationComponent.inject(this);
        this.context = context;
        this.announcementList = announcementList;
    }

    @Override
    public long getItemId(int position) {
        return announcementList.get(position).getId();
    }

    @NonNull
    @Override
    public SimpleAnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSimpleAnnouncementItemBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()),R.layout.layout_simple_announcement_item,  parent, false);
        return new SimpleAnnouncementAdapter.SimpleAnnouncementViewHolder(binding, context);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleAnnouncementViewHolder holder, int position) {
        Announcement announcement = announcementList.get(position);
        holder.bind(announcement);
        holder.binding.getRoot().setOnClickListener(v -> {
            new FinestWebView.Builder(context)
                    .titleColor(ContextCompat.getColor(context, R.color.white))
                    .show(announcement.getReferenceUrl());
//            Intent intent = new Intent(context, WebViewActivity.class);
//            intent.putExtra("url", announcement.getReferenceUrl());
//            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return announcementList == null ? 0 : announcementList.size();
    }

    public static class SimpleAnnouncementViewHolder extends RecyclerView.ViewHolder {
        private LayoutSimpleAnnouncementItemBinding binding;
        private Context context;
        public SimpleAnnouncementViewHolder(LayoutSimpleAnnouncementItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        public void bind(Announcement announcement){
            this.binding.setAnnouncement(announcement);
        }
    }
}
