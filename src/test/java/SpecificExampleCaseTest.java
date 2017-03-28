import com.bennytran.HTMLDocument;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 */
public class SpecificExampleCaseTest {

    private String homePath = System.getProperty("user.dir");

    @Test
    public void testHTMLOutputTestCase() {

        String fullPath = homePath + "/src/main/resources/HTMLTestFiles/Test3.html";
        HTMLDocument htmlDocument = new HTMLDocument(new File(fullPath), "pitchbook.com");

        StringBuilder testCaseAnswer = new StringBuilder();
        String answerPath = homePath + "/src/main/resources/HTMLTestFiles/Test3Answer.html";
        try {
            Scanner answerScanner = new Scanner(new File(answerPath));
            boolean firstLine = true;
            while (answerScanner.hasNextLine()) {
                testCaseAnswer.append(answerScanner.nextLine());
                if (firstLine) {
                    testCaseAnswer.append("\n");
                    firstLine = false;
                }
            }
            answerScanner.close();
            assertThat("html output should match the answer file text", testCaseAnswer.toString(), is(htmlDocument.getOutputString()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSequenceOutputTestCase() {
        String fullPath = homePath + "/src/main/resources/HTMLTestFiles/Test2.html";
        HTMLDocument htmlDocument = new HTMLDocument(new File(fullPath), "pitchbook.com");

        ArrayList<String> sequences = htmlDocument.getSequences();
        ArrayList<String> seqAnswers = new ArrayList<String>();
        String answerPath = homePath + "/src/main/resources/HTMLTestFiles/Test2Answer.txt";
        try {
            Scanner scanner = new Scanner(new File(answerPath));
            while (scanner.hasNextLine()) {
                seqAnswers.add(scanner.nextLine().trim());
            }
            scanner.close();
            assertThat("sequence answers should match in size", sequences.size(), is(seqAnswers.size()));
            for (int i = 0; i < seqAnswers.size(); i++) {
                assertThat("sequence should exactly match", sequences.get(i), is(seqAnswers.get(i)));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


}
