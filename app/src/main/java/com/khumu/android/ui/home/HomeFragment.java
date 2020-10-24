package com.khumu.android.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.R;
import com.khumu.android.ui.board.ArticleAdapter;
import com.khumu.android.ui.board.ArticleData;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

//    private HomeViewModel homeViewModel;
    private ArrayList<ArticleData> articleDataArrayList;
    private ArticleAdapter articleAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Layout inflate 이전
        // savedInstanceState을 이용해 다룰 데이터가 있으면 다룸.
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 나의 부모인 컨테이너에서 내가 그리고자 하는 녀석을 얻어옴. 사실상 루트로 사용할 애를 객체와.
        // inflate란 xml => java 객체
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // 데이터는 viewModel에서 가져와라.
        TextView t = root.findViewById(R.id.counter);
        homeViewModel.increase();
        t.setText(homeViewModel.getText()+"");
        return root;
    }

}