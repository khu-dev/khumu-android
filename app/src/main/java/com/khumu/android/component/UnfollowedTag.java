package com.khumu.android.component;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.khumu.android.R;

public class UnfollowedTag extends BaseTag{
    public UnfollowedTag(@NonNull Context context) {
        super(context);
    }

    public UnfollowedTag(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UnfollowedTag(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setBackground(@NonNull Context context, @Nullable AttributeSet attrs) {
        if(attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "background") == null){
            this.setBackgroundResource(R.drawable.round_secondary_bordered_background);
        }
    }

    @Override
    protected void setTextColor(@NonNull Context context, @Nullable AttributeSet attrs) {
        if(attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "textColor") == null){
            tagNameTV.setTextColor(context.getColor(R.color.red_300));
        }
    }
}
