package com.smileyjoedev.webstore.object;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by cody on 2016/03/29.
 */
public class HashTag extends SugarRecord {

    @Unique
    private String mText;

    public HashTag() {
    }

    public HashTag(String text) {
        mText = text;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    @Override
    public String toString() {
        return "HashTag{" +
                "mText='" + mText + '\'' +
                '}';
    }
}
