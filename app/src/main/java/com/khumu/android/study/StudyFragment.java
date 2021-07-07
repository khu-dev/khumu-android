package com.khumu.android.study;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.R;
import com.khumu.android.data.StudyArticle;
import com.khumu.android.databinding.FragmentStudyBinding;
import com.khumu.android.repository.StudyService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import static com.khumu.android.KhumuApplication.applicationComponent;

public class StudyFragment extends Fragment {
    final static String TAG = "StudyFragment";
    @Inject
    public StudyService studyService;
    private Intent intent;
    private FragmentStudyBinding binding;
    private StudyViewModel studyViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        applicationComponent.inject(this);
        this.intent = getActivity().getIntent();
        super.onCreate(savedInstanceState);
        studyViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @NotNull
            @Override
            public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
                return (T) new StudyViewModel(getContext(), studyService);
            }
        }).get(StudyViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_study, container, false);
        View root = binding.getRoot();
        binding.setStudyViewModel(this.studyViewModel);
        binding.setLifecycleOwner(this);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();
    }

    @Override("study_list")
    public static void bindStudyList(RecyclerView recyclerView, LiveData<List<StudyArticle>> studyList) {
        if (recyclerView.getAdapter() != null) {
            StudyAdapter adapter = (StudyAdapter) recyclerView.getAdapter();
            adapter.studyList.clear();
            adapter.studyList.addAll((List<StudyArticle>) studyList.getValue());
            adapter.notifyDataSetChanged();
        }
    }


}
