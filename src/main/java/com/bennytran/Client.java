package com.bennytran;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by bennytran on 3/15/17.
 */
public class Client {

    static Logger log = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {
        String url1 = "http://www.google.com";
        String url2 = "https://pitchbook.com/about-pitchbook";

//         HTMLDocument htmlObject = new HTMLDocument(url2);
         // com.bennytran.HTMLDocument htmlObject = new com.bennytran.HTMLDocument(new File("/Users/bennytran/Documents/Projects/PitchBook/InterviewProject/src/main/resources/HTMLTestFiles/Test1.html"));
//        System.out.println(htmlObject.getTitle());
//        htmlObject.printAllTags();
//        System.out.println(htmlObject.getLinks());
//        System.out.println(htmlObject.getTags());
//        ArrayList<String> sequences = htmlObject.getSequences();
//        sequences.stream().forEach(x -> System.out.println(x));
//        htmlObject.compareSequences();
//        htmlObject.printAllNodes();

        log.info(url1);
        log.info(url2);
    }
}

