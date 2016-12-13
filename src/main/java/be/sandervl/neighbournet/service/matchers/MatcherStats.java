package be.sandervl.neighbournet.service.matchers;


import lombok.Getter;
import lombok.Setter;

/**
 * @author: sander
 * @date: 15/11/2016
 */
public class MatcherStats {

    @Getter
    @Setter
    private int matchesFound = 0;

    public void incMatchesFound() {
        this.matchesFound++;
    }
}
