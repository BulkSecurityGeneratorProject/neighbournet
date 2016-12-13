package be.sandervl.neighbournet.service.matchers;

import be.sandervl.neighbournet.domain.Attribute;

/**
 * @author: sander
 * @date: 12/12/2016
 */
public class GeolocationAttributeMatcher implements AttributeMatcher {

    public static final double MAX_GEOLOCATION_DISTANCE = 0.01;

    @Override
    public Boolean match(Attribute a, Attribute b) {
        return Math.abs(Double.parseDouble(a.getValue()) - Double.parseDouble(b.getValue())) < MAX_GEOLOCATION_DISTANCE;
    }
}
