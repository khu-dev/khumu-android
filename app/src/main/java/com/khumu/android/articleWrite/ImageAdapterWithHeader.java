///**
// * 백업용임. 현재는 안 씀.
// * ImageAdapterWithHeader는 RecyclerView의 Adapter에서
// * 한 타입의 ViewHolder만 이용하는 것이 아니라
// * 여러 type의 ViewHolder를 이용하는 경우를 구현.
// */
//package com.khumu.android.articleWrite;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.RequestBuilder;
//import com.bumptech.glide.RequestManager;
//import com.esafirm.imagepicker.features.ImagePicker;
//import com.khumu.android.R;
//
//import java.util.List;
//
///**
// * 안 쓰는 중!
// */
//public class ImageAdapterWithHeader extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    private final int TYPE_IMAGE_ADDER = 0;
//    private final int TYPE_ITEM = 1;
//
//    private List<ImagePath> imagePaths;
//    private Context context;
//    private ArticleWriteViewModel viewModel; // viewModel은 upload image delete 문제로 인해 필요함.
//
//    public ImageAdapterWithHeader(ArticleWriteViewModel viewModel, List<ImagePath> imagePaths) {
//        this.viewModel = viewModel;
//        this.imagePaths = imagePaths;
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        context = parent.getContext(); // 이게 여기서 주입되는 게 조금 이상하다.
//        RecyclerView.ViewHolder view = null;
//        if (viewType == TYPE_IMAGE_ADDER){
//            view = new ImageAdder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_article_image_adder, parent, false));
//        } else if (viewType == TYPE_ITEM){
//            view = new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_article_uploading_thumbnail_item, parent, false));
//        }
//        return view;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof ImageAdder){
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Checking if permission is not granted
//                    if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//                        ((AppCompatActivity)context).requestPermissions(
//                                        new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
//                    }
//                    else {
//                        // ImagePicker 관련 몇 가지 옵션들
//                        ImagePicker.create((AppCompatActivity)context) // Activity or Fragment
////                            .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
//                                .folderMode(true) // folder mode (false by default)
//                                .toolbarFolderTitle("폴더") // folder selection title
////                                .toolbarImageTitle("Tap to select") // image selection title
////                                .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
////                                .includeVideo(true) // Show video on image picker
////                                .onlyVideo(onlyVideo) // include video (false by default)
////                                .single() // single mode
//                                .multi() // multi mode (default mode)
////                                .limit(10) // max images can be selected (99 by default)
//                                .showCamera(true) // show camera or not (true by default)
//                                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
////                                .origin(images) // original selected images, used in multi mode
////                                .exclude(images) // exclude anything that in image.getPath()
////                                .excludeFiles(files) // same as exclude but using ArrayList<File>
//                                .theme(R.style.KhumuImagePickerTheme) // must inherit ef_BaseTheme. please refer to sample
//                                .enableLog(true) // disabling log
//                                .start();
//                    }
//                }
//            });
//        } else if (holder instanceof Item){
//            Item imageItem = (Item) holder;
//            imageItem.deleteIV.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    viewModel.deleteImage(imagePaths.get(position - 1));
//                }
//            });
//            ImagePath path = imagePaths.get(position-1);
//
//            // position 0은 adder이지만 images엔 adder가 존재하지 않음.
//            // 이미 업로드 된 녀석은 url에서, 이번에 업로드 하는 녀석은 uri에서 glide load
//            RequestManager glideRM = Glide.with(context);
//            RequestBuilder glideRB = null;
//            if (path.isFromLocalUri()){
//                glideRB = glideRM.load(path.getLocalUri());
//            } else{
//                glideRB = glideRM.load("https://storage.khumu.jinsu.me/" + "thumbnail/" + path.getRemoteFileName());
//            }
//
//            glideRB.into(imageItem.imageIV);
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position){
//        if (position == 0){
//            return TYPE_IMAGE_ADDER;
//        } else{
//            return TYPE_ITEM;
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return imagePaths.size() + 1;
//    }
//
//    public List<ImagePath> getImagePaths() {
//        return imagePaths;
//    }
//
//    /**
//     * Image를 추가하는 ViewHolder. Header 역할로 사용된다.
//     */
//    public class ImageAdder extends RecyclerView.ViewHolder{
//        public ImageAdder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }
//
//    /**
//     * Image 하나 하나 Item을 나타내는 ViewHolder.
//     */
//    public class Item extends RecyclerView.ViewHolder{
//        public ImageView imageIV;
//        public ImageView deleteIV;
//        public Item(@NonNull View itemView) {
//            super(itemView);
//            imageIV = (ImageView) itemView.findViewById(R.id.thumbnail_image_view);
//            deleteIV = (ImageView) itemView.findViewById(R.id.delete_image_view);
//        }
//    }
//}
