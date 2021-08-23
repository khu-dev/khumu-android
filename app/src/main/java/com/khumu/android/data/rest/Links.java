package com.khumu.android.data.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Links implements Serializable {

    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("previous")
    @Expose
    private String previous;
//    @SerializedName("next_cursor")
//    @Expose
//    private Object nextCursor;

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    //public Object getNextCursor() {return nextCursor;}

    //public void setNextCursor(Object nextCursor) { this.nextCursor = nextCursor;}



}
