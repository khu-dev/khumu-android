package com.khumu.android.home;

import com.khumu.android.BaseWebViewFragment;
import com.khumu.android.R;

public class HomeFragment extends BaseWebViewFragment {
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected String getLoadUrl() {
        return "https://khumu-frontend.vercel.app/";
    }
}
