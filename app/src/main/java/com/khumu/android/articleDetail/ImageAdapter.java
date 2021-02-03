package com.khumu.android.articleDetail;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khumu.android.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{
    private final static String TAG = "com.khumu.android.articleDetail.ThumbnailAdapter";
    private List<String> thumbnailSrc;
    private Context context; // Glide가 View를 그리기 위함.
    public ImageAdapter(List<String> thumbnailSrcs, Context context) {
        this.thumbnailSrc = thumbnailSrcs;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_article_detail_image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: " + thumbnailSrc.get(position));
        Glide.with(this.context)
                .load("https://storage.khumu.jinsu.me/" + "resized/256/" + thumbnailSrc.get(position))
                .centerCrop()
                .into(holder.imageIV);
    }

    @Override
    public int getItemCount() {
        return thumbnailSrc.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageIV;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIV = itemView.findViewById(R.id.image_iv);
        }
    }
}
