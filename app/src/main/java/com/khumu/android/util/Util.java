package com.khumu.android.util;
//import com.khumu.android.util.Helper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.text.MessageFormat;
import java.util.Scanner;

public class Util {
    static String APIProtocol = "https";
    static String APIHost = "api.khumu.jinsu.me";
    static String APIPort = "443";
//    static String APIProtocol = "http";
//    static String APIHost = "192.168.219.254";
//    static String APIPort = "8000";
    static String APISubPathForRoot = "/api";
    public static String APIRootEndpoint;
    public static String DEFAULT_USERNAME = "admin";
    public static String DEFAULT_PASSWORD = "123123";


    public static void init(){
        APIRootEndpoint = APIProtocol + "://" +  APIHost +  ":" + APIPort + APISubPathForRoot + "/";
        System.out.println(APIRootEndpoint);
    }

    public static void expandView(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT //어떤 LayoutParams?
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight*3 / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapseView(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight*3 / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
