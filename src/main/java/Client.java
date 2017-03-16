import java.util.ArrayList;

/**
 * Created by bennytran on 3/15/17.
 */
public class Client {

    public static void main(String[] args) {
        String url1 = "http://www.google.com";
        String url2 = "https://pitchbook.com/about-pitchbook";

        HTMLDocument htmlObject = new HTMLDocument(url2);
//        System.out.println(htmlObject.getTitle());
//        htmlObject.printAllTags();
//        System.out.println(htmlObject.getLinks());
//        System.out.println(htmlObject.getTags());
//        ArrayList<String> sequences = htmlObject.getSequences();
//        sequences.stream().forEach(x -> System.out.println(x));
        htmlObject.compareSequences();
    }

}
