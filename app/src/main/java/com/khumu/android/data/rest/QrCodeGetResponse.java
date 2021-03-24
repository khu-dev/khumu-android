package com.khumu.android.data.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khumu.android.data.Article;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class QrCodeGetResponse  implements Serializable{
    QrCodeData data;
    String message;

    @NoArgsConstructor
    @Data
    public static class QrCodeData implements Serializable {
        @SerializedName("qr_code_str")
        String qrCodeStr;
        String name;
        String student_number;
        String department;

    }
}
