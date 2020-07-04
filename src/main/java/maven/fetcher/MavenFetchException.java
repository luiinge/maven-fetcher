package maven.fetcher;

/**
 * Simple runtime exception that wraps other errors occurred during a fetching operation
 */
public class MavenFetchException extends RuntimeException{

    private static final long serialVersionUID = 1L;


    public MavenFetchException(Throwable e) {
        super(e);
    }

}
