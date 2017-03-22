import com.bennytran.Client;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by bennytran on 3/15/17.
 */
public class ClientTest {

    private Client client;

    @BeforeClass
    public static void setUp() {

    }

    @Test
    @Ignore
    public void testMainMakesCorrectOutput() {
        String[] arguments = new String[2];
        Client.main(arguments);
    }


}
