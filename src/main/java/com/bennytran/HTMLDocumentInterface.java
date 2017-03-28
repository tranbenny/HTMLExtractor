package com.bennytran;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Interface for public methods in HTMLDocument
 */

public interface HTMLDocumentInterface {

    // get all valid links embedded in html Document
    public ArrayList<String> getLinks();
    // get all valid sequences in html document. (2 or more words where each word is capitalized)
    public ArrayList<String> getSequences();
    // get html as string value with only tags
    public String getOutputString();
    // create a file with link, sequence, and html tag content in passed in file
    public boolean generateFile(String filename);

    // set a new url for htmldocument content
    public boolean setUrl(String url) throws MalformedURLException;

    // get the current url being used to generate values
    public String getUrl();


}
