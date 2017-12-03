package com.moez.QKSMS.common.formatter;

public class FormatterFactory {
    private static final Formatter[] FORMATTERS = {new NumberToContactFormatter(),new FeiLongSMSFormat()};

    public static String format(String text) {
        for (Formatter formatter : FORMATTERS) {
            text = formatter.format(text);
        }
        return text;
    }
}
