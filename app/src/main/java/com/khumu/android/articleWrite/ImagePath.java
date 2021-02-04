/**
 * 처음 업로드 하는 경우에는 Glide가 local의 Uri를 이용하도록하고
 * 이미 업로드 했던 image의 경우에는 Glide가 원격지 url을 이용하도록
 * 그 둘을 포함하는 ImagePath class를 만듦.
 * ImageAdapter는 get 후에 사용하면 됨.
 */
package com.khumu.android.articleWrite;

import android.net.Uri;

public class ImagePath {
    private String urlPath;
    private Uri uriPath;

    public ImagePath(String urlPath) {
        this.urlPath = urlPath;
    }

    public ImagePath(Uri uriPath) {
        this.uriPath = uriPath;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public Uri getUriPath() {
        return uriPath;
    }

    public void setUriPath(Uri uriPath) {
        this.uriPath = uriPath;
    }

    public boolean isUriPath(){
        return this.uriPath != null;
    }
}
