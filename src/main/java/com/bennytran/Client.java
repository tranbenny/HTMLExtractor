package com.bennytran;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import javax.swing.text.html.HTML;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by bennytran on 3/15/17.
 */
public class Client {

    // static Logger log = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {
        String url = "https://pitchbook.com/about-pitchbook";

        // HTMLDocument htmlDocument = new HTMLDocument(file);
        HTMLDocument htmlDocument1 = new HTMLDocument(url);
        ArrayList<String> sequences = htmlDocument1.getSequences();
        ArrayList<String> attrs = htmlDocument1.getLinks();
        for (int i = 0; i < attrs.size(); i++) {
            System.out.println(attrs.get(i));
        }
        // htmlDocument1.getHTMLString();

    }
}

