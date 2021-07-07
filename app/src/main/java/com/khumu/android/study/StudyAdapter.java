package com.khumu.android.study;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.data.StudyArticle;
import com.khumu.android.databinding.LayoutStudyItemBinding;
import com.khumu.android.repository.StudyService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

public class StudyAdapter extends RecyclerView.Adapter<StudyAdapter.StudyViewHolder> {
    private final static String TAG = "StudyAdapter";
    public List<StudyArticle> studyList;
    private Context context;
    @Inject
    public StudyService studyService;
    public StudyViewModel studyViewModel;

    public StudyAdapter(List<StudyArticle> studyList, Context context, StudyViewModel studyViewModel) {
        KhumuApplication.applicationComponent.inject(this);
        this.context = context;
        this.studyList = studyList;
        this.studyViewModel = studyViewModel;
    }

    @NonNull
    @NotNull
    @Override
    public StudyAdapter.StudyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutStudyItemBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_study_item, parent, false);
        return new StudyAdapter.StudyViewHolder(binding, this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StudyAdapter.StudyViewHolder holder, int position, @NonNull @NotNull List<Object> payloads) {
        StudyArticle study = studyList.get(position);
        holder.bind(study);

        holder.binding.studyItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO link StudyDetailPage
            }
        });
        //TODO add study detail
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class StudyViewHolder  extends RecyclerView.ViewHolder {
        private LayoutStudyItemBinding binding;
        private Context context;
        public StudyViewHolder(LayoutStudyItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        public void bind(StudyArticle study) {
            this.binding.setStudy(study);
            this.binding.setViewHolder(this);
        }
    }
}
