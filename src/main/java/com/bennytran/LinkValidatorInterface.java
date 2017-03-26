package com.bennytran;

/**
 *
 */
public interface LinkValidatorInterface {

    public boolean isValidLink(String link);
    public String getBaseUri();
    public String formatRelativeLink(String path);

    public void setUrl(String url);
    public String getUrl();

}
