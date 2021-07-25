/**
 * Article에 첨부된 Image를 나타내는 RecyclerView가 사용하는 Adpapter.
 * ViewHolder의 타입이 단 한 개뿐이었던 다른 Adapter들과 달리 ImageAdder와 Item이라는 두 가지 타입을 이용함.
 * ImageAdder는 헤더 같은 느낌이다.
 */
package com.khumu.android.image;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.khumu.android.R;

import java.util.List;

public class DetailImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "DetailImageAdapter";
    private List<String> imageFileNames;
    private Context context;

    public DetailImageAdapter(Context context, List<String> imageFileNames) {
        this.context = context;
        this.imageFileNames = imageFileNames;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext(); // 이게 여기서 주입되는 게 조금 이상하다.
        RecyclerView.ViewHolder view = new ImageDetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_detail, parent, false));

        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageDetailViewHolder imageDetailViewHolder = (ImageDetailViewHolder) holder;
        Glide.with(this.context)
                .load("https://storage.khumu.jinsu.me/original/" + imageFileNames.get(position))
                .into(imageDetailViewHolder.photoView);
        Log.d(TAG, "onBindViewHolder: PhotoView 바인드 완료!");
        imageDetailViewHolder.photoView.setOnOutsidePhotoTapListener(imageView -> {
            if (context instanceof Activity){
                ((Activity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageFileNames.size();
    }

    public List<String> getImagePaths() {
        return imageFileNames;
    }
    /**
     * Image 하나 하나 Item을 나타내는 ViewHolder.
     */
    public class ImageDetailViewHolder extends RecyclerView.ViewHolder{
        public PhotoView photoView;
        public ImageDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.image_detail_photo_view);
        }
    }
}
