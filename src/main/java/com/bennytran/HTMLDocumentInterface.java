package com.bennytran;

import java.util.ArrayList;

/**
 *
 */
public interface HTMLDocumentInterface {




    public ArrayList<String> getLinks();
    public ArrayList<String> getSequences();
    public String getOutputString();
    public boolean generateFile(String filename);

    public boolean setUrl(String url);
    public String getUrl();


}
