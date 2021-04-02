package com.khumu.android.data.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khumu.android.data.Article;
import com.khumu.android.data.QrCodeInfo;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class QrCodeGetResponse  implements Serializable{
    QrCodeInfo data;
    String message;
}
