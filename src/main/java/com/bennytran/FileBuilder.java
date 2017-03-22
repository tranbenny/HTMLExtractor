package com.bennytran;

import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

/**
 * Created by bennytran on 3/16/17.
 */
public class FileBuilder {

    static Logger log = Logger.getLogger(FileBuilder.class.getName());
    private String fileName;
//    private HTMLDocument

    public FileBuilder(String fileName, HTMLDocument htmlDocument) {
        this.fileName = fileName;
    }

    public boolean validateFile() {
        return false;
    }

    // TODO: add a check for if the file exists or not
    public void writeToFile(String text) {
        log.info("WRITING TO FILE");
        try {
            // append to file example
            // FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.fileName));
            bw.write(text);
            // bufferedwriter will autoclose
        } catch (IOException e) {
            log.error(getStackTrace(e));
        }
    }

    public void generateFile() {
        try {
            FileWriter fileWriter = new FileWriter(this.fileName);
            fileWriter.write("[LINKS]");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
