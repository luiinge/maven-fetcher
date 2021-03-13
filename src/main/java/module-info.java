
open module maven.fetcher {

    exports maven.fetcher;

    requires aether.api;
    requires aether.connector.basic;
    requires aether.impl;
    requires aether.spi;
    requires aether.transport.file;
    requires aether.transport.http;
    requires aether.util;
    requires org.apache.commons.lang3;
    requires transitive org.slf4j;
    requires transitive java.instrument;
    requires plexus.utils;
    requires plexus.component.annotations;
    requires plexus.interpolation;
    requires javax.inject;
    requires com.google.guice;
    requires com.google.common;
    requires slf4jansi;



}