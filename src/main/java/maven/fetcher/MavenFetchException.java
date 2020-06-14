package maven.fetcher;

public class MavenFetchException extends RuntimeException{

    private static final long serialVersionUID = 1L;


    public MavenFetchException(Throwable e) {
        super(e);
    }

}
