package com.bennytran;


import java.lang.IllegalArgumentException;
import java.net.MalformedURLException;

/**
 * Main Method for extracting contents from passed url and creating a file with passed
 *  filename
 */
public class Client {

    /**
     * @param args command line arguments
     * @throws MalformedURLException for invalid urls
     * @throws IllegalArgumentException if main method gets more than 2 (required) number of inputs
     */
    public static void main(String[] args) throws MalformedURLException {
        // validate number of input arguments
        if (args.length != 2) {
            throw new IllegalArgumentException("NOT THE CORRECT NUMBER OF INPUT ARGUMENTS");
        } else {
            String url = args[0];
            String fileName = args[1];
            HTMLDocument htmlDocument = null;
            htmlDocument = new HTMLDocument(url);
            htmlDocument.generateFile(fileName);
        }
    }
}

