// component package는 ui component들을 모아둔 package이다
package com.khumu.android.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.flexbox.FlexboxLayout;
import com.khumu.android.R;

public class BaseTag extends androidx.appcompat.widget.LinearLayoutCompat {
    final static String TAG = "BaseTag";
    TextView tagTV;
    public BaseTag(@NonNull Context context) {
        super(context);
    }

    public BaseTag(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public BaseTag(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void initialize(@NonNull Context context, @Nullable AttributeSet attrs){
        // android는 http://schemas.android.com/apk/res/android의 alias이기때문에
        // attr을 얻을 땐 full name인 http://schemas.android.com/apk/res/android을 이용해야한다.
        tagTV = new TextView(context);
        tagTV.setText(attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "text"));
        this.addView(tagTV);

        this.setBackground(context, attrs);
        this.setTextColor(context, attrs);

        if(attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "textSize") == null){
            tagTV.setTextSize(14);
        }

        this.setPadding(20,10,20,10); // 10배 상세한 단위인듯
    }

    protected void setBackground(@NonNull Context context, @Nullable AttributeSet attrs){
        if(attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "background") == null){
            this.setBackgroundResource(R.drawable.round_primary_bordered_background);
        }
    }

    protected void setTextColor(@NonNull Context context, @Nullable AttributeSet attrs){
        if(attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "textColor") == null){
            tagTV.setTextColor(context.getColor(R.color.colorPrimary));
        }
    }
}
