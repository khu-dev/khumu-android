package com.khumu.android.announcement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        binding.announcementSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchWord = s.toString();
                announcementViewModel.refreshAnnouncement();
                announcementViewModel.searchAnnouncements(searchWord);
            }
        });
        binding.announcementRecyclerView.setAdapter(new AnnouncementAdapter(new ArrayList<Announcement>(), getContext(), announcementViewModel));
        binding.entireAnnouncementTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.announcementSearchEt.setText("");
                announcementViewModel.refreshAnnouncement();
                announcementViewModel.listAnnouncements();
            }
        });
        binding.followingAnnouncementTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                announcementViewModel.refreshAnnouncement();
                announcementViewModel.listFollowingAnnouncements();;
            }
        });
        binding.setAnnouncementViewModel(this.announcementViewModel);
        binding.setAnnouncementFragment(this);
        binding.setLifecycleOwner(this);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();
        Log.d(TAG, getActivity().toString());
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        binding.announcementBodySwipeRefreshLayout.setOnRefreshListener(()->{
            announcementViewModel.refreshAnnouncement();
            if(!binding.announcementSearchEt.getText().toString().equals("")) {
                announcementViewModel.searchAnnouncements(binding.announcementSearchEt.getText().toString());
            } else {
                if (announcementViewModel.showFollowedAnnouncement.getValue()) {
                    announcementViewModel.listFollowingAnnouncements();
                } else {
                    announcementViewModel.listAnnouncements();
                }
            }
            binding.announcementBodySwipeRefreshLayout.setRefreshing(false);
        });
        binding.announcementRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisiableItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                if(lastVisiableItemPosition == itemTotalCount) {
                    //System.out.println("뭐지"+binding.announcementSearchEt.getText().toString());
                    if(!binding.announcementSearchEt.getText().toString().equals("")) {
                        announcementViewModel.searchAnnouncements(binding.announcementSearchEt.getText().toString());
                    } else {
                        if (announcementViewModel.showFollowedAnnouncement.getValue()) {
                            announcementViewModel.listFollowingAnnouncements();
                        } else {
                            announcementViewModel.listAnnouncements();
                        }
                    }
                }
            }
        });
    }

    @BindingAdapter("announcement_list")
    public static void bindAnnouncementList(RecyclerView recyclerView, LiveData<List<Announcement>> announcements) {
        if (recyclerView.getAdapter() != null && announcements!=null) {
            AnnouncementAdapter adapter = (AnnouncementAdapter) recyclerView.getAdapter();
            adapter.announcements.clear();
            adapter.announcements.addAll((List<Announcement>) announcements.getValue());
            adapter.notifyDataSetChanged();
        }
    }
}
