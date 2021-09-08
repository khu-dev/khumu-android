package com.khumu.android.announcement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.databinding.FragmentAnnouncementBinding;
import com.khumu.android.repository.AnnouncementService;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import static com.khumu.android.KhumuApplication.applicationComponent;

public class AnnouncementFragment extends Fragment {
    final static String TAG = "AnnouncementFragment";
    @Inject
    public AnnouncementService announcementService;
    private Intent intent;
    private FragmentAnnouncementBinding binding;
    private AnnouncementViewModel announcementViewModel;

    private RecyclerView announcementRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        applicationComponent.inject(this);
        this.intent = getActivity().getIntent();
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_announcement, container, false);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
