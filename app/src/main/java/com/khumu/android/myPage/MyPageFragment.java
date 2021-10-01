package com.khumu.android.myPage;

import com.khumu.android.BaseWebViewFragment;
import com.khumu.android.BuildConfig;
import com.khumu.android.R;

public class MyPageFragment extends BaseWebViewFragment {
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_my_page;
    }

    @Override
    protected String getLoadUrl() {
        return BuildConfig.KHUMU_WEB_ROOT_URL + "/mypage";
    }
}
