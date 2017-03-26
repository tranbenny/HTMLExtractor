package com.bennytran;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 *
 */
public interface HTMLDocumentInterface {


    public ArrayList<String> getLinks();
    public ArrayList<String> getSequences();
    public String getOutputString();
    public boolean generateFile(String filename);

    public boolean setUrl(String url) throws MalformedURLException;
    public String getUrl();


}
