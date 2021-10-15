package com.khumu.android.boardList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.khumu.android.R;
import com.khumu.android.data.Board;
import com.khumu.android.databinding.FragmentBoardListBinding;
import com.khumu.android.feed.FollowingBoardAdapter;
import com.khumu.android.repository.BoardService;
import com.khumu.android.search.CommunitySearchActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.khumu.android.KhumuApplication.applicationComponent;

public class BoardListFragment extends Fragment {
    final static String TAG = "BoardListFragment";
    @Inject
    public BoardService boardService;
    private Intent intent;
    private FragmentBoardListBinding binding;
    private BoardViewModel boardViewModel;
    private final static Map<String, String> boardCategories = new HashMap<>();

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
        boardCategories.put("공식", "official");
        boardCategories.put("자율", "free");
        boardCategories.put("학과", "department");
        boardCategories.put("강의", "lecture_suite");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_list, container, false);
        View root = binding.getRoot();
        binding.followingBoardListRecyclerView.setAdapter(new BoardAdapter(new ArrayList<Board>(), this.getContext(), boardViewModel));
        binding.categoryBoardListRecyclerView.setAdapter(new BoardAdapter(new ArrayList<Board>(), this.getContext(), boardViewModel));
        binding.communityBoardCategoryTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String boardCategoryName = tab.getText().toString();
                if (!boardCategories.containsKey(boardCategoryName)) {
                    Toast.makeText(BoardListFragment.this.getContext(), "올바르지 않은 게시판 카테고리에요. 최신 버전인지 확인해주세요!", Toast.LENGTH_SHORT).show();

                    return;
                }

                if (boardCategoryName.equals("강의")) {
                    binding.textView.setVisibility(View.VISIBLE);
                    boardViewModel.categoryBoards.postValue(new ArrayList<>());
                } else {
                    binding.textView.setVisibility(View.GONE);
                    boardViewModel.listCategoryBoards(boardCategories.get(boardCategoryName));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        binding.setBoardViewModel(this.boardViewModel);
        binding.setBoardListFragment(this);
        binding.setLifecycleOwner(this);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();
        Log.d(TAG, getActivity().toString());
    }

    @BindingAdapter("following_board_list")
    public static void bindFollowingBoardList(RecyclerView recyclerView, LiveData<List<Board>> followingBoards) {
        // 아직 recyclerView에 Adapter가 생성이 되지 않았을 때는 넘어가야한다.
        if (recyclerView.getAdapter() != null) {
            BoardAdapter adapter = (BoardAdapter) recyclerView.getAdapter();
            adapter.boardList.clear();
            adapter.boardList.addAll((List<Board>) followingBoards.getValue());
            adapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter("category_board_list")
    public static void bindCategoryBoardList(RecyclerView recyclerView, LiveData<List<Board>> categoryBoards) {
        if (recyclerView.getAdapter() != null) {
            BoardAdapter adapter = (BoardAdapter) recyclerView.getAdapter();
            adapter.boardList.clear();
            adapter.boardList.addAll((List<Board>) categoryBoards.getValue());
            adapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter("is_category_lecture")
    public static void bindIsCategoryLecture(TextView textView, boolean isCategoryLecture) {
        if (isCategoryLecture) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    public void onClickSearchBtn(View view) {
        Context context = this.getContext();
        Intent intent = new Intent(context, CommunitySearchActivity.class);
        this.getActivity().startActivity(intent);
    }

}