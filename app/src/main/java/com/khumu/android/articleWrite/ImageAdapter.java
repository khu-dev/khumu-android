/**
 * Article에 첨부된 Image를 나타내는 RecyclerView가 사용하는 Adpapter.
 * ViewHolder의 타입이 단 한 개뿐이었던 다른 Adapter들과 달리 ImageAdder와 Item이라는 두 가지 타입을 이용함.
 * ImageAdder는 헤더 같은 느낌이다.
 */
package com.khumu.android.articleWrite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khumu.android.R;
import com.khumu.android.data.article.Tag;
import com.khumu.android.databinding.LayoutArticleItemBinding;
import com.khumu.android.databinding.LayoutArticleTagItemBinding;
import com.khumu.android.feed.ArticleAdapter;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_IMAGE_ADDER = 0;
    private final int TYPE_ITEM = 1;

    private List<Bitmap> bitmaps;
    private Context context;

    public ImageAdapter(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        RecyclerView.ViewHolder view = null;
        if (viewType == TYPE_IMAGE_ADDER){
            view = new ImageAdder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_article_image_adder, parent, false));
        } else if (viewType == TYPE_ITEM){
            view = new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_article_image_item, parent, false));
        }
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageAdder){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    //기기 기본 갤러리 접근
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    //구글 갤러리 접근
                    // intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    ((AppCompatActivity)context).startActivityForResult(intent, ArticleWriteActivity.UPLOAD_IMAGE_ACTIVITY);
                }
            });
        } else if (holder instanceof Item){
            Item imageItem = (Item) holder;
            // position 0은 adder이지만 images엔 adder가 존재하지 않음.
            imageItem.imageIV.setImageBitmap(bitmaps.get(position-1));
        }
    }

    @Override
    public int getItemViewType(int position){
        if (position == 0){
            return TYPE_IMAGE_ADDER;
        } else{
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return bitmaps.size() + 1;
    }

    public List<Bitmap> getBitmaps() {
        return bitmaps;
    }

    /**
     * Image를 추가하는 ViewHolder. Header 역할로 사용된다.
     */
    public class ImageAdder extends RecyclerView.ViewHolder{
        public ImageAdder(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     * Image 하나 하나 Item을 나타내는 ViewHolder.
     */
    public class Item extends RecyclerView.ViewHolder{
        public ImageView imageIV;
        public Item(@NonNull View itemView) {
            super(itemView);
            imageIV = (ImageView) itemView.findViewById(R.id.image_iv);
        }
    }
}
