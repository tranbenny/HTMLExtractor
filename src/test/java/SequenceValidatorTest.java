import com.bennytran.HTMLDocument;
import org.junit.*;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;

/**
 *
 */
public class SequenceValidatorTest {

    private final String[] urls = new String[] {
      "https://pitchbook.com/about-pitchbook"
    };

    private HTMLDocument htmlDocument;
    private ArrayList<String> sequences;

    @Before
    public void setUp() {
        htmlDocument = new HTMLDocument(urls[0]);
        sequences = htmlDocument.getSequences();
    }

    @Test
    public void testAllCapitalLetters() {
        for (String x: sequences) {
            // check the first character and every character after a space is capitalized
            Assert.assertEquals(x.charAt(0), Character.toUpperCase(x.charAt(0)));
            for (int i = 1; i < x.length(); i++) {
                if (x.charAt(i - 1) == ' ') {
                    Assert.assertEquals(x.charAt(i), Character.toUpperCase(x.charAt(i)));
                }
            }
        }
    }

    @Test
    public void testSequenceHasMinTwoWords() {
        sequences.stream().forEach(x -> Assert.assertTrue(x.contains(" ")));
    }

    @Test
    public void testSequenceLengthTwo() {
        sequences.stream().forEach(x -> Assert.assertTrue(x.split(" ").length > 1));
    }

    @Test
    @Ignore
    // TODO: make an assumption about repeated sequences, as repeated sequences can be found throughout web page
    public void testNoRepeats() {
        HashMap<String, Integer> seqCounts = new HashMap<>();
        sequences.stream().forEach(x -> {
            if (seqCounts.containsKey(x)) {
                seqCounts.put(x, seqCounts.get(x) + 1);
            } else {
                seqCounts.put(x, 1);
            }
        });
        seqCounts.keySet().stream().forEach(key -> {
                Assert.assertSame("Duplicate value found for: " + key, seqCounts.get(key), 1);
        });
    }

    @Test
    public void testNoPunctuation() {
        // remove all punctuation
        for (String seq : sequences) {
            String removedPunc = seq.replaceAll("\\p{P}", "");
            Assert.assertThat(seq, is(removedPunc));
        }
    }


}
