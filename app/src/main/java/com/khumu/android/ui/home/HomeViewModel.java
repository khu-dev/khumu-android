package com.khumu.android.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<Integer> counter;

    public HomeViewModel() {
        counter = new MutableLiveData<>();
        counter.setValue(0);
//        mText = new MutableLiveData<>();
//        mText.setValue("This is home fragment");
    }

    public void increase(){
        counter.setValue(counter.getValue() + 1);
    }
    public void decrease(){
        counter.setValue(counter.getValue() - 1);
    }
    public Integer getText() {
        return counter.getValue();
    }
}