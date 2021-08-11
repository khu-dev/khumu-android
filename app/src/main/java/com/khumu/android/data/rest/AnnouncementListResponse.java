package com.khumu.android.data.rest;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khumu.android.data.Announcement;
import com.khumu.android.data.Article;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize
public class AnnouncementListResponse extends ArrayList<Announcement> {}
