/**
 * BaseFeedFragment를 상속받아 크게 추가할 내용은 없고, abstract method인 generateViewModel작업만 정의해주면됨.
 * 기본적인 feed의 layout인 layout_feed.xml을 이용.
 */
package com.khumu.android.feed;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.khumu.android.KhumuApplication;
import com.khumu.android.R;
import com.khumu.android.databinding.LayoutFeedBinding;
import com.khumu.android.repository.ArticleRepository;

import javax.inject.Inject;

public class SingleBoardFeedFragment extends BaseFeedFragment {
    private final static String TAG = "SingleBoardFeedFragment";

    private LayoutFeedBinding layoutFeedBinding;

    public SingleBoardFeedFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        KhumuApplication.applicationComponent.inject(this);
        provideFeedViewModel();
        feedViewModel.listArticles();
    }

    @Override
    protected void provideFeedViewModel() {
        this.feedViewModel = new ViewModelProvider(this.getActivity()).get(FeedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        layoutFeedBinding = DataBindingUtil.inflate(inflater, R.layout.layout_feed, container, false);
        // binding하며 사용할 Fragment가 사용하는 변수인 viewModel을 설정해줌.
        layoutFeedBinding.setFeedViewModel(this.feedViewModel);
        // LiveData를 이용해 Observe하기 위해선 그 LifeCyclerOwner가 꼭 필요하다!
        // 그렇지 않으면 유효하게 Observer로 동작하지 않고 아무 변화 없음...
        layoutFeedBinding.setLifecycleOwner(this);
        View root = layoutFeedBinding.getRoot();
        return root;
    }
}