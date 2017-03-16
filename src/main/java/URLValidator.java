import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bennytran on 3/15/17.
 */
public class URLValidator {

    private String url;
    private boolean isValid;

    public URLValidator(String url) {
        this.url = url;
//        this.isValid = this.validateUrl();
    }

//    public boolean validateURL() {
//        if (!this.url.startsWith("http://") && !this.url.startsWith("https://")) {
//            System.out.println("Bad url validation");
//            return false;
//        }
//
//        HttpURLConnection connection;
//        try {
//            URL requestUrl = new URL(this.url);
//            connection = (HttpURLConnection) requestUrl.openConnection();
//
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Accept-Charset", "UTF-8");
//
//            System.out.println("Sending request to " + this.url);
//            System.out.println("Response Code: " + connection.getResponseCode());
//            System.out.println("Response Message: " + connection.getResponseMessage());
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String line;
//            StringBuffer response = new StringBuffer();
//
//            while ((line = in.readLine()) != null) {
//                response.append(line);
//            }
//            in.close();
//
//            System.out.println(response.toString());
//
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            System.out.println("MALFORMED URL EXCEPTION");
//            return false;
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("IOException");
//            return false;
//        }
//
//
//        return true;
//    }
//
//
//    public

}
