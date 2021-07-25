package com.khumu.android.study;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.data.StudyArticle;
import com.khumu.android.data.rest.StudyListResponse;
import com.khumu.android.repository.StudyService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyViewModel extends ViewModel {

    private final static String TAG = "StudyViewModel";
    public StudyService studyService;
    public MutableLiveData<List<StudyArticle>> studies;
    private Context context;

    public StudyViewModel(Context context, StudyService studyService) {
        this.context = context;
        this.studyService = studyService;
        studies = new MutableLiveData<>();
        studies.setValue(new ArrayList<StudyArticle>());
        listStudies();
        Log.d(TAG, "Created");
    }

    public void listStudies() {
        Log.d(TAG, "listStudies");
        Call<StudyListResponse> call = studyService.getStudies(1);
        call.enqueue(new Callback<StudyListResponse>() {
            @Override
            public void onResponse(Call<StudyListResponse> call, Response<StudyListResponse> response) {
                if (response.isSuccessful()) {
                    studies.postValue(response.body().getData());
                    Log.d(TAG, "Response: " + response.body().getData());
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<StudyListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }



    //TODO add other method
}
