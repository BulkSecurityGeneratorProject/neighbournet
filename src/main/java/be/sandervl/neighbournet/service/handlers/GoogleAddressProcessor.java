package be.sandervl.neighbournet.service.handlers;

import be.sandervl.neighbournet.domain.Document;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: sander
 * @date: 4/12/2016
 */
@Slf4j
@Component
public class GoogleAddressProcessor implements SelectorProcessor {

    public static final String GOOGLE_GEOCODE_API_KEY = "google-geocode-api-key";

    @Autowired
    private Environment environment;

    public static final String DELIMITER_FOR_ADDRESS = " ";
    private GeoApiContext context;

    @PostConstruct
    public void setup() {
        if (environment.containsProperty(GOOGLE_GEOCODE_API_KEY)) {
            context = new GeoApiContext().setApiKey(environment.getProperty("google-geocode-api-key"));
        } else {
            log.warn("No property {} could be found as Geocode API key", GOOGLE_GEOCODE_API_KEY);
        }
    }

    @Override
    public String process(String value, Document document) {
        if (context == null) {
            log.warn("Geocontext is not correctly initialized, probably no Geocode API key could be found");
            return value;
        }
        String[] splitted = value.split("\\s");
        //create possible combinations to check for address
        Set<String> combinations = new HashSet<>();
        for (int i = 1; i <= splitted.length; i++) {
            combinations.add(createCombination(splitted, 0, i));
            combinations.add(createCombination(splitted, splitted.length - i, splitted.length));
        }
        log.debug("Created {} combinations for given value {}", combinations.size(), value);
        final String[] result = {value};
        //look for address matches in combinations
        combinations.stream()
                    .filter(this::findAndSaveAddress)
                    .findFirst()
                    .ifPresent(
                        match -> Stream.of(match.split(DELIMITER_FOR_ADDRESS)).forEach(matchelement -> result[0] = value.replace(matchelement, ""))
                    );
        return result[0].trim();
    }

    private String createCombination(String[] splitted, int startIndex, int endIndex) {
        return Arrays.asList(splitted).subList(startIndex, endIndex).stream().collect(Collectors.joining(DELIMITER_FOR_ADDRESS));
    }

    private boolean findAndSaveAddress(String address) {
        try {
            GeocodingResult[] results =
                GeocodingApi.geocode(context, address).await();
            if (results.length > 0 && !results[0].partialMatch) {
                LatLng location = results[0].geometry.location;
                log.debug("Found location {} for given value {}", location, address);
                //TODO insert new attributes for document
                return true;
            }
        } catch (Exception e) {
            log.error("error querying google geocoding API", e);
        }
        return false;
    }

}
