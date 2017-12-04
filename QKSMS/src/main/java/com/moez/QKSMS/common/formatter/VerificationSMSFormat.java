package com.moez.QKSMS.common.formatter;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by FEILONG on 2017-12-03.
 */

public class VerificationSMSFormat implements Formatter {
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
                text = "VerificationSMSFormat regex error!\n" + regex[0];
                Log.e("VerificationSMSFormat", ex.getMessage());
                Log.e("VerificationSMSFormat", regex[0]);
            }
        }

        return text;
    }

    private String RegexStr[][] = {
        //Your Steam verification code is YMYCP[PIN]
        {
            "Your Steam verification code is (\\w+)\\[PIN\\]",
            "",
            "Steam验证码：$1"
        },
        //【TapTap】921663（用于注册）15分钟内有效，如非本人操作请忽略。
        {
            "【TapTap】(\\d+)（(\\w+)）15分钟内有效，如非本人操作请忽略。",
            "",
            "TapTap验证码：$1\n" +
                "$2"
        },
        //【Google】G-390581 is your Google verification code.
        {
            "【Google】(G-\\d+) is your Google verification code.",
            "",
            "Google验证码\n" +
                "$1"
        },
        //验证码：368856，网易用户，您正在使用手机帐号登入服务[验证码告知他人将导致帐号被盗，请勿泄露]【网易】
        //844476 是你的登陆码。回复TD退订【英富必】
        //将 6345878 用作 Microsoft 帐户安全代码[PIN]
        //您的安全代码是 521101。使用愉快！[PIN]
        {
            ".*(?:安全代|验证|登[陆录])码.*",
            ".*?(\\d{4,}).*",
            "验证码：$1"
        }
    };
}
