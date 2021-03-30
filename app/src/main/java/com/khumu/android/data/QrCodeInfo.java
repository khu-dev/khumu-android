package com.khumu.android.data;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class QrCodeInfo {
    @SerializedName("qr_code_str")
    String qrCodeStr;
    String name;
    @SerializedName("student_number")
    String studentNumber;
    String department;
}
