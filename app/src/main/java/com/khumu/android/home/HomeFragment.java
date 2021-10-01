package com.khumu.android.home;

import com.khumu.android.BaseWebViewFragment;
import com.khumu.android.R;
import com.khumu.android.util.Util;

public class HomeFragment extends BaseWebViewFragment {
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected String getLoadUrl() {
        return Util.getKhumuWebRootUrl() + "/main";
    }
}
