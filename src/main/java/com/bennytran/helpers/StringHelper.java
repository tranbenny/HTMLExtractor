package com.bennytran.helpers;

/**
 *
 */
public class StringHelper {

    public static String removeSpaces(String text) {
        return text.replaceAll(" +", "");
    }

    public static String removeLineBreaks(String text) {
        return text.replaceAll("\n", "");
    }


}
