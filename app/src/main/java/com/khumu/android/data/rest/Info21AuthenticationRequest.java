package com.khumu.android.data.rest;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@JsonSerialize
@Data
@Builder
public class Info21AuthenticationRequest implements Serializable {
    String username;
    String password;
}
