package com.bennytran;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import org.apache.commons.validator.routines.UrlValidator;
import sun.awt.image.ImageWatched;

/**
 * Created by bennytran on 3/15/17.
 */
public class LinkValidator extends UrlValidator{

    private String url;
    private String baseUri;

    private boolean enableRelativeLink;

    private LinkValidator(String url, String baseUri, boolean enableRelativeLink) {
        this.url = url;
        this.baseUri = (isValidLink(url) && isValidLink(baseUri)) ? baseUri : "";

        this.enableRelativeLink = enableRelativeLink;

    }

    // TODO: HANDLE SCENARIO FOR WHEN PASSED URL IS INVALID
    public LinkValidator(String url) {
        this(url, getBaseUri(url), true);
    }

    public LinkValidator() {
        this("", "", false);
    }

    public static String getBaseUri(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String path = url.getFile().substring(0, url.getFile().lastIndexOf('/'));
        String base = url.getProtocol() + "://" + url.getHost();
//        System.out.println(path);
//        System.out.println(base);
        return base;
    }



    // TODO: ADD ABSOLUTE LINK VERIFICATION, DONE
    // TODO: ADD RELATIVE LINK VERIFICATION
    // TODO: HANDLE DOUBLE SLASHES: WHICH SHOULD BE THE EQUIVALENT OF HTTP OR HTTPS
    public boolean isValidLink(String linkText) {
        // check absolute link: http, https, ftp, //
//        System.out.println();
//        System.out.println(linkText);
        String link = linkText.trim();
//        System.out.println(link);
//        System.out.println(link.startsWith("/"));
        if (link.startsWith("http://") || link.startsWith("https://") || link.startsWith("ftp://") || link.startsWith("//")) {
//            System.out.println("CHECKING ABSOLUTE LINK");
            return super.isValid(link);
        } else if (this.enableRelativeLink && link.startsWith("/")) {
//            System.out.println("CHECKING RELATIVE LINK");
            // check relative link
//            System.out.println(this.baseUri + link);
            return super.isValid(this.baseUri + link);
        }
        return false;
    }

    /**
     * Format relative links into absolute links
     * @param value
     * @return
     */
    public String formatLink(String value) {
        StringBuilder urlBuilder = new StringBuilder();
        // absolute link
        if (value.startsWith("http://") || value.startsWith("https://") || value.startsWith("ftp://") || value.startsWith("//")) {
            return value;
        } else if (this.baseUri != "" && this.enableRelativeLink) {
            // create absolute link from relative link
            urlBuilder.append(this.baseUri);
            urlBuilder.append(value);
        }
        return urlBuilder.toString();
    }



}
