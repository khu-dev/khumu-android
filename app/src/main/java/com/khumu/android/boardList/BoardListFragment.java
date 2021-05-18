package com.khumu.android.boardList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.R;
import com.khumu.android.data.Board;
import com.khumu.android.databinding.FragmentArticleDetailBinding;
import com.khumu.android.databinding.FragmentBoardListBinding;
import com.khumu.android.repository.BoardService;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.khumu.android.BR.board;
import static com.khumu.android.KhumuApplication.applicationComponent;

public class BoardListFragment extends Fragment {
    final static String TAG = "BoardListFragment";
    @Inject
    public BoardService boardService;
    private Intent intent;
    private FragmentBoardListBinding binding;
    private BoardViewModel boardViewModel;

    private RecyclerView followingBoardRecyclerView;
    private RecyclerView entireBoardRecyclerView;
    private BoardAdapter followingBoardAdapter;
    private BoardAdapter entireBoardAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        applicationComponent.inject(this);
        this.intent = getActivity().getIntent();
        super.onCreate(savedInstanceState);
        boardViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new BoardViewModel(getContext(), boardService);
            }
        }).get(BoardViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_list, container, false);
        View root = binding.getRoot();

        binding.followingBoardsRecyclerView.setAdapter(followingBoardAdapter);
        binding.entireBoardsRecyclerView.setAdapter(entireBoardAdapter);
        binding.setBoardViewModel(this.boardViewModel);
        binding.setLifecycleOwner(this);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();
        linearLayoutManager = new LinearLayoutManager(view.getContext());

    }

//    @BindingAdapter("bind:followingBoard")
//    public static void bindItem(RecyclerView recyclerView, ObservableArrayList<Board>) {
//        BoardAdapter adapter = (BoardAdapter)recyclerView.getAdapter();
//        if (adapter != null) {
//
//        }
//    }
}