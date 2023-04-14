
open module maven.fetcher {

    exports maven.fetcher;

    requires slf4jansi;
    requires plexus.utils;
    requires javax.inject;
    requires org.apache.commons.lang3;
    requires plexus.interpolation;
    requires org.eclipse.sisu.inject;
    requires com.google.guice;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;


}