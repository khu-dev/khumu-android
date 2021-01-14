package com.khumu.android.myPage;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.khumu.android.data.Article;
import com.khumu.android.data.ArticleTag;
import com.khumu.android.data.Board;
import com.khumu.android.data.KhumuUser;
import com.khumu.android.repository.BoardRepository;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyPageViewModel extends ViewModel {
    private MutableLiveData<List<ArticleTag>> followingArticleTags;
    private MutableLiveData<KhumuUser> user;
    public MyPageViewModel(){
        user = new MutableLiveData<>(new KhumuUser("jinsu", "진수", "123123","",  "123123", "computer"));
        followingArticleTags = new MutableLiveData<>(new ArrayList<>());
    }

    // return할 때에는 바꿀 수 없도록 LiveData형으로 설정하는 게 나은 듯.
    public LiveData<KhumuUser> getUser() {
        System.out.println("getUser: " + user.getValue().getNickname());
        return user;
    }

    public LiveData<List<ArticleTag>> getLiveDataFollowingArticleTags(){
        return followingArticleTags;
    }
    public void ListFollowingArticleTags() {
        List<ArticleTag> tmpTags = followingArticleTags.getValue();
        for (int i=0; i<16; i++){
            tmpTags.add(new ArticleTag("FakeTag", false));
        }
        followingArticleTags.postValue(tmpTags);
    }
}