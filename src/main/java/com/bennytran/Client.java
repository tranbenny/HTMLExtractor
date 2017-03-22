package com.bennytran;


import java.lang.IllegalArgumentException;

public class Client {

    public static void main(String[] args) {
        // validate number of input arguments
        if (args.length != 2) {
            throw new IllegalArgumentException("NOT THE CORRECT NUMBER OF INPUT ARGUMENTS");
        } else {
            String url = args[0];
            String fileName = args[1];
            HTMLDocument htmlDocument = new HTMLDocument(url);
            htmlDocument.generateFile(fileName);
        }

//        String url = "https://pitchbook.com/about-pitchbook";
//        HTMLDocument htmlDocument = new HTMLDocument(url);
//        htmlDocument.generateFile("sample.txt");

    }
}

