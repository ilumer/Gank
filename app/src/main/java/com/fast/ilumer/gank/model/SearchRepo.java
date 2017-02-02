package com.fast.ilumer.gank.model;

/**
 * Created by ${ilumer} on 2/2/17.
 */

public class SearchRepo {
    private String showItem;
    private int tag;
    private String Uri;

    public SearchRepo(String showItem, int tag, String uri) {
        this.showItem = showItem;
        this.tag = tag;
        this.Uri = uri;
    }

    public SearchRepo() {
    }

    public String getShowItem() {
        return showItem;
    }

    public void setShowItem(String showItem) {
        this.showItem = showItem;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }
}
