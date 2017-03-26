package com.bennytran;

import java.net.MalformedURLException;

/**
 *
 */
public interface LinkValidatorInterface {

    public boolean isValidLink(String link);
    public String getBaseUri();
    public String formatRelativeLink(String path);

    public void setUrl(String url) throws MalformedURLException;
    public String getUrl();

}
