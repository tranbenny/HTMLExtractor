import com.bennytran.HTMLDocument;
import com.bennytran.LinkValidator;
import com.bennytran.helpers.GetHTMLService;
import org.junit.Ignore;
import org.junit.Test;


import java.net.MalformedURLException;
import java.util.ArrayList;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 *
 */
public class LinkValidatorTest {

    private final String[] validLinks = new String[] {
        "https://pitchbook.com/about-pitchbook",
        "https://pitchbook.com/platform-data/deals",
        "https://drive.google.com/drive/my-drive",
        "pitchbook.com/about-pitchbook",
        "www.mkyong.com/maven"
    };

    private final String[] validBaseLinks = new String[] {
        "https://www.google.com",
        "https://pitchbook.com"
    };


    @Test
    @Ignore
    public void testAllFoundLinksWork() throws MalformedURLException, InterruptedException {
        for (int i = 0; i < validLinks.length; i++) {
            HTMLDocument htmlDocument = new HTMLDocument(validLinks[i]);
            ArrayList<String> validLinks = htmlDocument.getLinks();
            for (String link : validLinks) {
                int responseCode = GetHTMLService.getResponseCode(link);
                if (responseCode == 999) {
                    // wait 15 seconds before sending another request
                    Thread.sleep(15000);
                    // retry http head request
                    responseCode = GetHTMLService.getResponseCode(link);

                }
                assertThat(responseCode, is(200));
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
            "ftp://google.com",
            "//pitchbook.com/about-pitchbook"
        };
        for (String url : invalidURLS) {
            LinkValidator linkValidator = new LinkValidator(url);
        }
    }

    @Test
    public void testGrabsBaseUriFromNestedURLs() throws MalformedURLException {
        for (String link : validLinks) {
            LinkValidator linkValidator = new LinkValidator(link);
            assertThat("Input URL should not match base URI", linkValidator.getBaseUri(), is(not(link)));
            assertThat("Base URI is a valid url", linkValidator.isValidLink(linkValidator.getBaseUri()), is(true));

            assertThat("Returned Base URI should have a protocol", linkValidator.getBaseUri().startsWith("http://") ||
                linkValidator.getBaseUri().startsWith("https://"), is(true)
            );
        }
    }




}
