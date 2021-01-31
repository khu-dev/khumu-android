package com.khumu.android.data.rest;

import java.util.HashMap;

public class ImageUploadResponse {
    private HashMap<String, String> data;
    private String message;

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
