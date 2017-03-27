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
    public LinkValidator(String url) throws MalformedURLException {
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
    public void setUrl(String url) throws MalformedURLException {
        if (!this.isValidLink(url)) {
            throw new MalformedURLException("Invalid url entered");
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
            if (!containsProtocol(this.url)) {
                this.url = "http://" + this.url;
            }
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
        if (containsProtocol(link)) {
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
        if (this.containsProtocol(value)) {
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

    /**
     *
     * @param url
     * @return
     */
    private boolean containsProtocol(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }



}
