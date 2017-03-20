package com.bennytran;

import org.apache.log4j.Logger;

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
        File file = new File("/Users/bennytran/Documents/Projects/PitchBook/InterviewProject/src/main/resources/HTMLTestFiles/Test2.html");
        File file2 = new File("/Users/bennytran/Documents/Projects/PitchBook/InterviewProject/src/main/resources/HTMLTestFiles/Test3.html");

        HTMLDocument htmlDocument = new HTMLDocument(file);
        HTMLDocument htmlDocument1 = new HTMLDocument(url);

    }
}

