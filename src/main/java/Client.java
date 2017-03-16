/**
 * Created by bennytran on 3/15/17.
 */
public class Client {

    public static void main(String[] args) {
        String url1 = "http://www.google.com";
        URLValidator urlValidator = new URLValidator(url1);
        boolean results = urlValidator.validateUrl();
        System.out.println(results);
    }

}
