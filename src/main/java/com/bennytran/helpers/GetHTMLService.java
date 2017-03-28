package com.bennytran.helpers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

/**
 * HTTP Service helper functions to retrieve html web page response as a String
 *
 */

public class GetHTMLService {

    /**
     * @throws MalformedURLException for invalid urls and
     * @throws IOException if there is an error for writing from the input stream
     * @param url String
     * @return String value of response from get request to passed url
     */
    public static String getFromURL(String url) {
        StringBuffer response = new StringBuffer();

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    /**
     * @throws FileNotFoundException if passed file cannot be found
     * @param file variable for loading in text from individual files
     * @return String value of file contents
     */
    public static String getFromFile(File file) {
        StringBuilder response = new StringBuilder();
        try {
            Scanner in = new Scanner(file);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                response.append(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    /**
     * creates a HEAD request to specified URL to test if link works
     * @throws MalformedURLException for invalid URLs
     * @throws ProtocolException for TCP errors
     * @throws IOException for connection errors
     * @param urlString string value for http request
     * @return int value to indicate successful response, returns -1 for any errors in retrieving
     *  a valid http status code
     */
    public static int getResponseCode(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();

            int code = connection.getResponseCode();
            return code;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
