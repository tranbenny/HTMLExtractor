package com.bennytran.helpers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

/**
 * HTTP Service Class to retrieve html web page response as a String
 */
public class GetHTMLService {

    /**
     *
     * @param url
     * @return
     */
    public static String getFromURL(String url) {
        HttpURLConnection connection = null;
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
     *
     * @param file
     * @return
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
     *
     * @param urlString
     * @return
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
