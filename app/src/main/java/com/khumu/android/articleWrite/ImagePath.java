/**
 * 처음 업로드 하는 경우에는 Glide가 local의 Uri를 이용하도록하고
 * 이미 업로드 했던 image의 경우에는 Glide가 원격지 url을 이용하도록
 * 그 둘을 포함하는 ImagePath class를 만듦.
 * ImageAdapter는 get 후에 사용하면 됨.
 */
package com.khumu.android.articleWrite;

import android.net.Uri;

public class ImagePath {
    private String hashedFileName;
    private Uri uriPath;

    // 새로 업로드하는 것이 아니라 원래 업로드 했던 이미지에 대한 Path
    // uri는 null이고 url만 존재
    public ImagePath(String hashedFileName) {
        this.hashedFileName = hashedFileName;
    }

    // 새로 업로드 할 때의 이미지 Path
    // uri와 url모두 갖게되지만 url은 아직 렌더링 되기 이전이라
    // uri로 렌더링 해야함.
    public ImagePath(String hashedFileName, Uri uriPath) {
        this.hashedFileName = hashedFileName;
        this.uriPath = uriPath;
    }

    public String getHashedFileName() {
        return hashedFileName;
    }

    public void setHashedFileName(String hashedFileName) {
        this.hashedFileName = hashedFileName;
    }

    public Uri getUriPath() {
        return uriPath;
    }

    public void setUriPath(Uri uriPath) {
        this.uriPath = uriPath;
    }

    public boolean isRenderedByUri(){
        return this.uriPath != null;
    }
}
