package com.bennytran;

import java.net.MalformedURLException;
import java.net.URL;


import org.apache.commons.validator.routines.UrlValidator;

/**
 *
 */
public class LinkValidator extends UrlValidator implements LinkValidatorInterface{

    private String url;
    private String baseUri;

    /**
     *
     * @param url
     */
    public LinkValidator(String url) {
        this.setUrl(url);
    }

    /**
     *
     * @return
     */
    public String getUrl() {
        if (this.url != null) {
            return this.url;
        } else {
            return "";
        }
    }

    /**
     *
     * @param url
     */
    public void setUrl(String url) {
        if (!this.isValidLink(url)) {
            try {
                throw new MalformedURLException("Invalid url entered");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            this.url = url;
            this.baseUri = getBaseUri();
        }
    }

    /**
     *
     * @return
     */
    public String getBaseUri() {
        try {
            URL url = new URL(this.url);
            String base = url.getProtocol() + "://" + url.getHost();
            return base;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // TODO: handle logging here
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            // TODO: add logging here
        }
        return "";

    }

    /**
     *
     * @param linkText
     * @return
     */
    public boolean isValidLink(String linkText) {
        String link = linkText.trim();
        // check links that indicate the protocol
        if (link.startsWith("http://") || link.startsWith("https://") || link.startsWith("//")) {
            return super.isValid(link);
        } else if (link.startsWith("/")) { // relative links
            return super.isValid(this.baseUri + link);
        } else { // any other possible links
            return (super.isValid("http://" + link) || super.isValid("https://" + link));
        }
    }

    /**
     * Format relative links into absolute links
     * @param value
     * @return
     */
    public String formatRelativeLink(String value) {
        StringBuilder urlBuilder = new StringBuilder();
        // absolute link
        if (value.startsWith("http://") || value.startsWith("https://") || value.startsWith("//")) {
            return value;
        } else if (this.baseUri != null) {
            // create absolute link from relative link
            urlBuilder.append(this.baseUri);
            urlBuilder.append(value);
        }

        if (this.isValidLink(urlBuilder.toString())) {
            return urlBuilder.toString();
        } else {
            return "";
        }
    }



}
