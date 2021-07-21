package com.khumu.android.articleDetail;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.khumu.android.image.ImageDetailActivity;
import com.khumu.android.R;


import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{
    private final static String TAG = "com.khumu.android.articleDetail.ThumbnailAdapter";
    private List<String> imageFileName;
    private Context context; // Glide가 View를 그리기 위함.

    public ImageAdapter(List<String> imageFileNames, Context context) {
        this.imageFileName = imageFileNames;
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
        Log.d(TAG, "onBindViewHolder: " + imageFileName.get(position));
        String basePath = "https://storage.khumu.jinsu.me/";
        // default는 캐시를 이용하기 때문에 같은 이미지에 대해서 다시 요청을 보내지 않고 로컬 캐시를 이용한다.
        // 하지만 thumbnail기능을 좀 더 적극 이용해보고싶어서 일부러 캐시를 사용하지 않는 쪽으 로설정해보고있다.
        Glide.with(this.context)
                .load(basePath + "resized/1024/" + imageFileName.get(position))
                .thumbnail(
                        Glide.with(this.context)
                                .load(basePath + "thumbnail/" + imageFileName.get(position))
                                .centerCrop())
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.imageIV);
        holder.imageIV.setOnClickListener(view ->{
            Intent imageDetailIntent = new Intent(context, ImageDetailActivity.class);
            imageDetailIntent.putStringArrayListExtra("imageFileNames", (ArrayList<String>) imageFileName);
            imageDetailIntent.putExtra("currentImageIndex", position);
            context.startActivity(imageDetailIntent);
        });
    }

    @Override
    public int getItemCount() {
        return imageFileName.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageIV;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIV = itemView.findViewById(R.id.image_iv);
        }
    }
}
