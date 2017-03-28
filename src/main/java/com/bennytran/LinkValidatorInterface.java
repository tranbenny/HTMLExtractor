package com.bennytran;

import java.net.MalformedURLException;

/**
 * Interface for public methods in LinkValidator Helper Class
 */
public interface LinkValidatorInterface {

    // true or false value if passed link is a valid link
    public boolean isValidLink(String link);
    // get base uri value from object's initialized url
    public String getBaseUri();
    // get absolute links from passed relative links
    public String formatRelativeLink(String path);

    // sets url value to run validations against
    public void setUrl(String url) throws MalformedURLException;
    // get String value of currently set url to run other link validations against
    public String getUrl();

}
