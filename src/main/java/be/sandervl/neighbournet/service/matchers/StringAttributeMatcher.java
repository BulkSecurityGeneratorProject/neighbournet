package be.sandervl.neighbournet.service.matchers;

import be.sandervl.neighbournet.domain.Attribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author: sander
 * @date: 12/12/2016
 */
@Component
public class StringAttributeMatcher implements AttributeMatcher {

    public static final int MAXIMUM_LEVENSHTEIN_DISTANCE = 3;

    @Override
    public Boolean match(Attribute a, Attribute b) {
        return StringUtils.getLevenshteinDistance(a.getValue(), b.getValue()) < MAXIMUM_LEVENSHTEIN_DISTANCE;
    }
}
