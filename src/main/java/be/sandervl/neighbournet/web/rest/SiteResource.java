package be.sandervl.neighbournet.web.rest;

import be.sandervl.neighbournet.domain.Site;
import be.sandervl.neighbournet.repository.SiteRepository;
import be.sandervl.neighbournet.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Site.
 */
@RestController
@RequestMapping("/api")
public class SiteResource {

    private final Logger log = LoggerFactory.getLogger(SiteResource.class);

    @Inject
    private SiteRepository siteRepository;

    /**
     * POST  /sites : Create a new site.
     *
     * @param site the site to create
     * @return the ResponseEntity with status 201 (Created) and with body the new site, or with status 400 (Bad Request) if the site has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sites")
    @Timed
    public ResponseEntity<Site> createSite(@Valid @RequestBody Site site) throws URISyntaxException {
        log.debug("REST request to save Site : {}", site);
        if (site.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("site", "idexists", "A new site cannot already have an ID")).body(null);
        }
        Site result = siteRepository.save(site);
        return ResponseEntity.created(new URI("/api/sites/" + result.getId()))
                             .headers(HeaderUtil.createEntityCreationAlert("site", result.getId().toString()))
                             .body(result);
    }

    /**
     * PUT  /sites : Updates an existing site.
     *
     * @param site the site to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated site,
     * or with status 400 (Bad Request) if the site is not valid,
     * or with status 500 (Internal Server Error) if the site couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sites")
    @Timed
    public ResponseEntity<Site> updateSite(@Valid @RequestBody Site site) throws URISyntaxException {
        log.debug("REST request to update Site : {}", site);
        if (site.getId() == null) {
            return createSite(site);
        }
        Site result = siteRepository.save(site);
        return ResponseEntity.ok()
                             .headers(HeaderUtil.createEntityUpdateAlert("site", site.getId().toString()))
                             .body(result);
    }

    /**
     * GET  /sites : get all the sites.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sites in body
     */
    @GetMapping("/sites")
    @Timed
    public List<Site> getAllSites() {
        log.debug("REST request to get all Sites");
        List<Site> sites = siteRepository.findAll();
        return sites;
    }

    /**
     * GET  /sites/:id : get the "id" site.
     *
     * @param id the id of the site to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the site, or with status 404 (Not Found)
     */
    @GetMapping("/sites/{id}")
    @Timed
    public ResponseEntity<Site> getSite(@PathVariable Long id) {
        log.debug("REST request to get Site : {}", id);
        Site site = siteRepository.findOne(id);
        return Optional.ofNullable(site)
                       .map(result -> new ResponseEntity<>(
                           result,
                           HttpStatus.OK))
                       .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sites/:id : delete the "id" site.
     *
     * @param id the id of the site to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sites/{id}")
    @Timed
    public ResponseEntity<Void> deleteSite(@PathVariable Long id) {
        log.debug("REST request to delete Site : {}", id);
        siteRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("site", id.toString())).build();
    }

}
