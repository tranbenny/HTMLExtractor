package com.bennytran;

import java.util.ArrayList;

/**
 *
 */
public class SequenceValidator {

    // TODO: SEQUENCE VALIDATION
    // TODO: NEED TO HANDLE SYMBOLS AND NUMBERS
    // TODO: NEED TO TEST HOW IT REMOVES THE PUNCTUATION
    public static ArrayList<String> isValid(String inputText) {
        // remove all punctuation
        String text = inputText.replaceAll("\\p{Punct}", "");
//        String text = inputText;
        ArrayList<String> combinations = new ArrayList<String>();

        String[] words = text.split(" +");

        if (words.length >= 2) {
            String currCombination = "";
            for (int i = 0; i < words.length; i++) {
                // check if word is capitalized
                if (words[i].charAt(0) == Character.toUpperCase(words[i].charAt(0))
                        && words[i].length() > 2) {
                    currCombination = currCombination + words[i] + " ";
                } else {
                    if (currCombination.trim().split(" ").length >= 2) {
                        combinations.add(currCombination);
                    }
                    currCombination = "";
                }
            }
            if (currCombination.split(" ").length >= 2) {
                combinations.add(currCombination);
            }
        }


        return combinations;
    }



}
