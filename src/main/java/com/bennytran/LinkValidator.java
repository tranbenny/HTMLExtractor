package com.bennytran;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import org.apache.commons.validator.routines.UrlValidator;

/**
 * Created by bennytran on 3/15/17.
 */
public class LinkValidator {


    // TODO: ADD ABSOLUTE LINK VERIFICATION, DONE
    // TODO: ADD RELATIVE LINK VERIFICATION
    public static boolean isValidLink(String link) {
        // System.out.println(link);
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(link);
    }

}
