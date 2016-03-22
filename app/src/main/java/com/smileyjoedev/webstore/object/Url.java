package com.smileyjoedev.webstore.object;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

/**
 * Created by cody on 2016/03/21.
 */
public class Url extends SugarRecord{

    private String mTitle;
    private String mNote;
    private String mUrl;
    private String mContent;

    public void setTitle(String title) {
        mTitle = title.trim();
    }

    public void setNote(String note) {
        mNote = note.trim();
    }

    public void setUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        mUrl = url.trim();
    }

    public void setContent(String content) {
        mContent = content.trim();
    }

    public String getTitle() {
        return mTitle;
    }

    public String getNote() {
        return mNote;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getContent() {
        return mContent;
    }

    @Override
    public String toString() {
        return "Url{" +
                "mTitle='" + mTitle + '\'' +
                ", mNote='" + mNote + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mContent='" + mContent + '\'' +
                '}';
    }
}
