package com.smileyjoedev.webstore.object;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;
import com.smileyjoedev.autocomplete.AutoCompleteInterface;
import com.smileyjoedev.webstore.R;
import com.smileyjoedev.webstore.activity.MainActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cody on 2016/03/21.
 */
public class Url extends SugarRecord implements AutoCompleteInterface {

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

    public SpannableString getNoteSpannable(Context context) {
        String text = getNote();
        String patternStr = "#\\w+";
        Pattern pattern = Pattern.compile(patternStr);

        SpannableString span = new SpannableString(text);

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String title = matcher.group();
            String first = title.substring(0, 1);
//            title = title.substring(1, title.length());

            int start = matcher.start();
            int end = matcher.end();

            if (first.equals("#")) {
                span.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new OnHashClick(context, title), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return span;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getContent() {
        return mContent;
    }

    public String getShareText() {
        return getNote() + " " + getUrl();
    }

    @Override
    public String getAutoCompleteText() {
        return getTitle() + " " + getNote() + " " + getUrl() + " " + getContent();
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

    private class OnHashClick extends ClickableSpan{

        private String mHash;
        private Context mContext;

        public OnHashClick(Context context, String hash) {
            mContext = context;
            mHash = hash;
        }

        @Override
        public void onClick(View widget) {
            Intent intent = MainActivity.getIntentWithSearch(mContext, mHash);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        }
    }
}
