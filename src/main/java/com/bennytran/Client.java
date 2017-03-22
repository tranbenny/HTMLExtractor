package com.bennytran;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import javax.swing.text.html.HTML;
import java.io.File;
import java.util.ArrayList;

/**
 *
 */
public class Client {

    public static void main(String[] args) {
        String url = "https://pitchbook.com/about-pitchbook";
        HTMLDocument htmlDocument = new HTMLDocument(url);
        htmlDocument.generateFile("sample.txt");

    }
}

