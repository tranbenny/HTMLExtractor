package com.bennytran;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by bennytran on 3/15/17.
 */
public class Client {

    // static Logger log = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {
        String url = "https://pitchbook.com/about-pitchbook";
        File file = new File("/Users/bennytran/Documents/Projects/PitchBook/InterviewProject/src/main/resources/HTMLTestFiles/Test2.html");
        File file2 = new File("/Users/bennytran/Documents/Projects/PitchBook/InterviewProject/src/main/resources/HTMLTestFiles/Test3.html");
        //        HTMLDocument htmlObject = new HTMLDocument(url);
//        HTMLDocument htmlObject =  new HTMLDocument(file);

//        htmlObject.getSequences().stream().forEach(x -> System.out.println(x));
//        HTMLDocument htmlObject = new HTMLDocument(file2);
//        String sampleWebPage = htmlObject.executeGetRequest(url);

        HTMLDocument htmlObject2 = new HTMLDocument(true);
        htmlObject2.handleSelfClosingTags();

    }
}

