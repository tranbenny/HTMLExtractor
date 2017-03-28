package com.bennytran;

import java.util.ArrayList;


/**
 * Helper class for validation sequences
 * Sequence is defined as 2 or more words where each word is capitalized
 * Sequences contain no punctuation and only numbers/letters
 */
public class SequenceValidator {

    /**
     * @param inputText string value
     * @return list of all found sequences in passed in string value
     */
    public static ArrayList<String> getValidSequences(String inputText) {
        // remove all punctuation
        String text = removePunc(inputText);
        ArrayList<String> combinations = new ArrayList<String>();

        String[] words = text.split("\\s+");
        if (words.length >= 2) {
            String currCombination = "";
            for (int i = 0; i < words.length; i++) {
                // check if word is capitalized
                if (words[i].length() > 2 && words[i].charAt(0) == Character.toUpperCase(words[i].charAt(0))) {
                    currCombination = currCombination + words[i] + " ";
                } else {
                    if (currCombination.trim().split(" ").length >= 2) {
                        combinations.add(currCombination.trim());
                    }
                    currCombination = "";
                }
            }
            if (currCombination.split(" ").length >= 2) {
                combinations.add(currCombination.trim());
            }
        }

        return combinations;
    }


    /**
     * Handles non-break spaces and ampersands seperately
     * @param text
     * @return same text value stripped of all content except numbers and letters
     */
    private static String removePunc(String text) {
        // handle no break spaces
        String result = text;
        // replace non-break space characters
        result = result.replaceAll("\u00A0", " ");
        // handle ampersand characters
        result  = handleAmpersand(result);
        // remove all characters that are not numbers or letters
        result = result.replaceAll("[^0-9a-zA-Z\\s]", "");
        return result;
    }

    /**
     * If ampersand is surrounded by a space at both sides, replaces ampersand with space
     *  otherwise amerpsand is just stripped off with no space replacement
     * @param text
     * @return text value stripped of ampersands
     */
    private static String handleAmpersand(String text) {
        int index = text.indexOf("\u0026");
        if (index != -1 && index > 0 && index < text.length() - 1) {
            // check for spaces on both sides
            String result = text;
            if (text.charAt(index - 1) == ' ' && text.charAt(index + 1) == ' ') {
                result = text.replace("\u0026", "");
            } else {
                result = text.replace("\u0026", "");
            }
            return result;
        }
        return text;
    }

}
