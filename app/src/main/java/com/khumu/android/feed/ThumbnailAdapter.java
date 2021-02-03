// ArticleDetail에서 써야겠다.
//package com.khumu.android.feed;
//
//import android.content.ContentResolver;
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.khumu.android.R;
//
//import java.util.List;
//
//public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ThumbnailViewHolder>{
//    private final static String TAG = "ThumbnailAdapter";
//    private List<String> thumbnailSrc;
//    private Context context; // Glide가 View를 그리기 위함.
//    public ThumbnailAdapter(List<String> thumbnailSrcs, Context context) {
//        this.thumbnailSrc = thumbnailSrcs;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public ThumbnailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_article_thumbnail_item, parent, false);
//        return new ThumbnailViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ThumbnailViewHolder holder, int position) {
//            Log.d(TAG, "onBindViewHolder: " + thumbnailSrc.get(position));
//        Glide.with(this.context)
//                .load("https://storage.khumu.jinsu.me/" + "thumbnail/" + thumbnailSrc.get(position))
//                .centerCrop()
//                .into(holder.thumbnailIV);
//    }
//
//    @Override
//    public int getItemCount() {
//        return thumbnailSrc.size();
//    }
//
//    public class ThumbnailViewHolder extends RecyclerView.ViewHolder{
//        public ImageView thumbnailIV;
//        public ThumbnailViewHolder(@NonNull View itemView) {
//            super(itemView);
//            thumbnailIV = itemView.findViewById(R.id.thumbnail_image_view);
//        }
//    }
//}
