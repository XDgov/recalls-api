package recallsapi2.ingester;

/**
 *
 * @author ferru001
 */
public class IngesterMain {

    public static void main(String[] args) {
        CPSCIngester cpscIngester = new CPSCIngester();
        cpscIngester.ingestData();;
    }
}
