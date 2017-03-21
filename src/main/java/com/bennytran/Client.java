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

    // static Logger log = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {
        String url = "https://pitchbook.com/about-pitchbook";
//        String url2 = "https://www.youtube.com/watch?v=vRszFI3hTVs";
//        LinkValidator.getBaseUri("https://www.indeed.com/cmp/TellWise/jobs/Full-Stack-Developer-097fd8318797bd05?sjdu=QwrRXKrqZ3CNX5W-O9jEvRQls7y2xdBHzhqWkvhd5FFQBGanIjWg7sAFjjkELPdYe2fVGWQHdN8byJJcnU7TBA");
//        LinkValidator.getBaseUri(url);
//        LinkValidator.getBaseUri(url2);
        // HTMLDocument htmlDocument = new HTMLDocument(file);
        HTMLDocument htmlDocument1 = new HTMLDocument(url);
//        ArrayList<String> sequences = htmlDocument1.getSequences();
        ArrayList<String> attrs = htmlDocument1.getLinks();
        for (int i = 0; i < attrs.size(); i++) {
            System.out.println(attrs.get(i));
        }
        // htmlDocument1.getHTMLString();

//        System.out.println("/css/app.css?uq=rcvnUrNU".startsWith("/"));

    }
}

