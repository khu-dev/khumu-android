package com.khumu.android.announcement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.R;
import com.khumu.android.data.Announcement;
import com.khumu.android.databinding.FragmentAnnouncementBinding;
import com.khumu.android.repository.AnnouncementService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.khumu.android.KhumuApplication.applicationComponent;

public class AnnouncementFragment extends Fragment {
    final static String TAG = "AnnouncementFragment";
    @Inject
    public AnnouncementService announcementService;
    private Intent intent;
    private FragmentAnnouncementBinding binding;
    private AnnouncementViewModel announcementViewModel;
    private AnnouncementAdapter announcementAdapter;
    private RecyclerView announcementRecyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        applicationComponent.inject(this);
        this.intent = getActivity().getIntent();
        super.onCreate(savedInstanceState);
        announcementViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NotNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new AnnouncementViewModel(getContext(), announcementService);
            }
        }).get(AnnouncementViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_announcement, container, false);
        View root = binding.getRoot();
        binding.announcementRecyclerView.setAdapter(new AnnouncementAdapter(new ArrayList<Announcement>(), getContext(), announcementViewModel));
        binding.entireAnnouncementTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                announcementViewModel.listAnnouncements();
            }
        });
        binding.followingAnnouncementTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                announcementViewModel.listFollowingBoards();;
            }
        });
        return root;
    }

    @BindingAdapter("announcement_list")
    public static void bindAnnouncementList(RecyclerView recyclerView, LiveData<List<Announcement>> announcements) {
        if (recyclerView.getAdapter() != null) {
            AnnouncementAdapter adapter = (AnnouncementAdapter) recyclerView.getAdapter();
            adapter.announcements.clear();
            adapter.announcements.addAll((List<Announcement>) announcements.getValue());
            adapter.notifyDataSetChanged();
        }
    }
}
