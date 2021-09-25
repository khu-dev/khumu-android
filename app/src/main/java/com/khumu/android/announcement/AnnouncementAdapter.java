package com.khumu.android.announcement;

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
import com.khumu.android.databinding.LayoutAnnouncementItemBinding;
import com.khumu.android.databinding.LayoutBoardListItemBinding;
import com.khumu.android.repository.AnnouncementService;
import com.thefinestartist.finestwebview.FinestWebView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>{
    private final static String TAG = "AnnouncementAdapter";
    public List<Announcement> announcements;
    private Context context;
    @Inject
    public AnnouncementService announcementService;
    public AnnouncementViewModel announcementViewModel;

    public AnnouncementAdapter(List<Announcement> announcements, Context context, AnnouncementViewModel announcementViewModel) {
        KhumuApplication.applicationComponent.inject(this);
        this.context = context;
        this.announcements = announcements;
        this.announcementViewModel = announcementViewModel;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutAnnouncementItemBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_announcement_item, parent, false);
        return new AnnouncementAdapter.AnnouncementViewHolder(binding, this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);
        holder.bind(announcement);
        holder.binding.announcementTitleTv.setOnClickListener(v -> {
            new FinestWebView.Builder(context)
                .titleColor(ContextCompat.getColor(context, R.color.white))
                .show(announcement.getSubLink());
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        private LayoutAnnouncementItemBinding binding;
        private Context context;
        public AnnouncementViewHolder(LayoutAnnouncementItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        public void bind(Announcement announcement) {
            this.binding.setAnnouncement(announcement);
            this.binding.setViewHolder(this);
        }
    }
}
