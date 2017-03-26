import com.bennytran.HTMLDocument;
import com.bennytran.LinkValidator;
import com.bennytran.helpers.GetHTMLService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sun.awt.image.ImageWatched;

import java.net.MalformedURLException;
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

    private final String[] validBaseLinks = new String[] {
        "https://www.google.com",
        "https://pitchbook.com"
    };


    @Test
    @Ignore
    public void testAllFoundLinksWork() {
        for (int i = 0; i < validLinks.length; i++) {
            HTMLDocument htmlDocument = new HTMLDocument(validLinks[i]);
            ArrayList<String> validLinks = htmlDocument.getLinks();
            for (String link : validLinks) {
                assertThat(GetHTMLService.getResponseCode(link), is(200));
            }
        }
    }

    @Test
    public void testHandleNonProtocolURLs() throws MalformedURLException {
        String[] nonProtcolUrls = new String[] {
            "google.com",
            "pitchbook.com",
            "amazon.com"
        };
        LinkValidator linkValidator = new LinkValidator(nonProtcolUrls[0]);
        assertThat("Non protocol urls should be resolved to contain protocol", linkValidator.getUrl(),
                is("http://" + nonProtcolUrls[0]));
    }

    @Test
    public void testBaseUrlsEqualBaseUriValue() throws MalformedURLException {
        for (String url : validBaseLinks) {
            LinkValidator linkValidator = new LinkValidator(url);
            assertThat("URLs should match", linkValidator.getBaseUri(), is(url));
        }
    }

    @Test(expected = MalformedURLException.class)
    public void testInvalidURLs() throws MalformedURLException {
        String[] invalidURLS = new String[] {
            "dfklndlf",
            "ft://pitchbook.com",
            "hello",
            "ftp://google.com"
        };
        for (String url : invalidURLS) {
            LinkValidator linkValidator = new LinkValidator(url);
        }
    }

    @Test
    public void testGrabsBaseUriFromNestedURLs() {
        
    }



}
