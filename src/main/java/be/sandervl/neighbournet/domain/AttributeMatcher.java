package be.sandervl.neighbournet.domain;

import be.sandervl.neighbournet.service.matchers.GeolocationAttributeMatcher;
import be.sandervl.neighbournet.service.matchers.StringAttributeMatcher;

/**
 * @author: sander
 * @date: 12/12/2016
 */
public enum AttributeMatcher {
    STRING(StringAttributeMatcher.class),
    GEOLOCATION(GeolocationAttributeMatcher.class),
    UNKNOWN(null);

    AttributeMatcher(Class matcherClass) {
        this.matcherClass = matcherClass;
    }

    private Class matcherClass;

    public Class getMatcherClass() {
        return matcherClass;
    }

    public void setMatcherClass(Class matcherClass) {
        this.matcherClass = matcherClass;
    }
}
