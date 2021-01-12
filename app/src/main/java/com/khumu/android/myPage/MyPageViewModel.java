package com.khumu.android.myPage;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.data.Article;
import com.khumu.android.data.ArticleTag;
import com.khumu.android.data.Board;
import com.khumu.android.repository.BoardRepository;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyPageViewModel extends ViewModel {
    private MutableLiveData<List<ArticleTag>> followingArticleTags;

    public MyPageViewModel(){
        followingArticleTags = new MutableLiveData<>(new ArrayList<>());
    }
    public MutableLiveData<List<ArticleTag>> getLiveDataFollowingArticleTags(){
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