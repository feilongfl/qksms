package com.moez.QKSMS.common.formatter;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by FEILONG on 2017-12-03.
 */

public class GarbageSMSFormat implements Formatter {
    private String ReplaceText(String text, String regex, String replace) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        return m.replaceAll(replace);
    }

    @Override
    public String format(String text) {
        for (String[] regex : RegexStr) {
            try {
                text = (!text.matches(regex[0])) ?
                    text : ReplaceText(text, (regex[1] == "") ? regex[0] : regex[1], regex[2]);
            } catch (Exception ex) {
                text = "GarbageSMSFormat regex error!\n" + regex[0];
                Log.e("GarbageSMSFormat", ex.getMessage());
                Log.e("GarbageSMSFormat", regex[0]);
            }
        }

        return text;
    }

    private String RegexStr[][] = {
        {
            ".*回复?\\w+退[订定].*",
            "",
            "垃圾短信"
        },
        {
            ".*退[订定]回.*",
            "",
            "垃圾短信"
        }
    };
}
