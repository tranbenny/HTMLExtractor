/**
 * Created by bennytran on 3/15/17.
 */
public class Client {

    public static void main(String[] args) {
        String url1 = "http://www.google.com";

        HTMLDocument htmlObject = new HTMLDocument(url1);
//        System.out.println(htmlObject.getTitle());
        htmlObject.printAllTags();
    }

}
