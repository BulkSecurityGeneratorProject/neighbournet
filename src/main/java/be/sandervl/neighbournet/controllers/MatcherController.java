package be.sandervl.neighbournet.controllers;

import be.sandervl.neighbournet.domain.Document;
import be.sandervl.neighbournet.service.matchers.DocumentMatcher;
import be.sandervl.neighbournet.service.matchers.MatcherStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

/**
 * @author: sander
 * @date: 13/12/2016
 */
@Controller
@Slf4j
public class MatcherController {

    @Inject
    private DocumentMatcher documentMatcher;

    @RequestMapping(method = RequestMethod.PUT, value = "/matcher")
    @ResponseBody
    public MatcherStats startCrawler(@RequestParam(name = "id") Document document) {
        log.debug("Finding matches for document {}", document);
        MatcherStats response = new MatcherStats();
        try {
            documentMatcher.findMatches(document);
        } catch (Exception e) {
            log.error("Exception occured while finding matches for document {}", document, e);
        }
        return response;
    }
}
