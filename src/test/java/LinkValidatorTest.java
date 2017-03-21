import com.bennytran.HTMLDocument;
import com.bennytran.LinkValidator;
import com.bennytran.helpers.GetHTMLService;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 *
 */
public class LinkValidatorTest {

    private final String[] validLinks = new String[] {
        "https://pitchbook.com/about-pitchbook"
    };


    // GOT HTTP REQUEST RESPONSE 999: ACCESS DENIED :(
    @Test
    @Ignore
    public void testAllFoundLinksWork() {
        for (int i = 0; i < validLinks.length; i++) {
            HTMLDocument htmlDocument = new HTMLDocument(validLinks[i]);
            ArrayList<String> validLinks = htmlDocument.getLinks();
            validLinks.stream().forEach(link -> {
                assertThat(GetHTMLService.getResponseCode(link), is(200));
            });
        }
    }

}
