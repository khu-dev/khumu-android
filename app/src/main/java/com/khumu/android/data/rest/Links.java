package com.khumu.android.data.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Links implements Serializable {

    @SerializedName("next")
    @Expose
    private Object next;
    @SerializedName("previous")
    @Expose
    private Object previous;
    @SerializedName("next_cursor")
    @Expose
    private Object nextCursor;

    public Object getNext() {
        return next;
    }

    public void setNext(Object next) {
        this.next = next;
    }

    public Object getPrevious() {
        return previous;
    }

    public void setPrevious(Object previous) {
        this.previous = previous;
    }

    public Object getNextCursor() {return nextCursor;}

    public void setNextCursor(Object nextCursor) { this.nextCursor = nextCursor;}

}
