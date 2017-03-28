package com.bennytran;

import java.net.MalformedURLException;
import java.net.URL;


import org.apache.commons.validator.routines.UrlValidator;

/**
 * Helper class for validating links and resolving relative links
 * Inherits org.apache.commons.validator.routines.UrlValidator
 */
public class LinkValidator extends UrlValidator implements LinkValidatorInterface {

    private String url;
    private String baseUri;

    /**
     * @param url: String url for validation
     * @throws MalformedURLException on invalid url values
     */
    public LinkValidator(String url) throws MalformedURLException {
        this.setUrl(url);
    }

    /**
     * @return current set url, if blank, returns empty string
     */
    public String getUrl() {
        if (this.url != null) {
            return this.url;
        } else {
            return "";
        }
    }

    /**
     * sets current url to passed input
     * @param url : string value
     * @throws MalformedURLException on invalid urls
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
     * @return Base URI extracted from current url, otherwise empty string if exceptions are found
     * MalformedURLException: on invalid urls
     * StringIndexOutofBoundsException if base cannot be extracted from protocol and host methods
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
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * Attempts to resolve link if protocol is not added
     * @param linkText: passed in link
     * @return boolean value on if link is valid
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
     * Resolves relative links with set base URI value or returns full passed in valid link
     *  Will return empty string if final formatted link is not valid
     * @param value: link value
     * @return String value of an absolute link based on currently set base URI value
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
     * @param url
     * @return boolean value on whether contains http,https protocol in its value
     */
    private boolean containsProtocol(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    /**
     * @param url string value
     * @return url value with http:// appended if it doesn't contain a protocol
     */
    public String createFullLink(String url) {
        if (!containsProtocol(url)) {
            return "http://" + url;
        } else {
            return url;
        }
    }
    
}
