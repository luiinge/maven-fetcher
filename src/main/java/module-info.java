
open module maven.fetcher {

    exports maven.fetcher;

    requires slf4jansi;
    requires maven.resolver.provider;
    requires org.apache.maven.resolver;
    requires org.apache.maven.resolver.spi;
    requires org.apache.maven.resolver.util;
    requires org.apache.maven.resolver.impl;
    requires org.apache.maven.resolver.transport.file;
    requires org.apache.maven.resolver.transport.http;
    requires org.apache.maven.resolver.connector.basic;


}