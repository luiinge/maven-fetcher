module maven.fetcher.test {
    requires maven.fetcher;
    requires org.assertj.core;
    requires org.slf4j;
    requires junit;
    exports maven.fetcher.test to junit;
}