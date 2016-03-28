package com.smileyjoedev.webstore.general;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cody on 2016/03/28.
 */
public class StringHelper {

    public static List<String> getUrls(String string){
        List<String> urls = new ArrayList<>();

        Pattern urlPattern = Pattern.compile(
                "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                        + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                        + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

        Matcher matcher = urlPattern.matcher(string);
        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();

            String url = string.substring(matchStart, matchEnd);

            urls.add(url);
        }

        return urls;
    }

}
