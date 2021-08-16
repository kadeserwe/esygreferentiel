package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.SourcesFinancement;
import sn.ssi.sigmap.repository.SourcesFinancementRepository;
import sn.ssi.sigmap.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link sn.ssi.sigmap.domain.SourcesFinancement}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SourcesFinancementResource {

    private final Logger log = LoggerFactory.getLogger(SourcesFinancementResource.class);

    private static final String ENTITY_NAME = "referentielmsSourcesFinancement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SourcesFinancementRepository sourcesFinancementRepository;

    public SourcesFinancementResource(SourcesFinancementRepository sourcesFinancementRepository) {
        this.sourcesFinancementRepository = sourcesFinancementRepository;
    }

    /**
     * {@code POST  /sources-financements} : Create a new sourcesFinancement.
     *
     * @param sourcesFinancement the sourcesFinancement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sourcesFinancement, or with status {@code 400 (Bad Request)} if the sourcesFinancement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sources-financements")
    public ResponseEntity<SourcesFinancement> createSourcesFinancement(@Valid @RequestBody SourcesFinancement sourcesFinancement) throws URISyntaxException {
        log.debug("REST request to save SourcesFinancement : {}", sourcesFinancement);
        if (sourcesFinancement.getId() != null) {
            throw new BadRequestAlertException("A new sourcesFinancement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SourcesFinancement result = sourcesFinancementRepository.save(sourcesFinancement);
        return ResponseEntity.created(new URI("/api/sources-financements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sources-financements} : Updates an existing sourcesFinancement.
     *
     * @param sourcesFinancement the sourcesFinancement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourcesFinancement,
     * or with status {@code 400 (Bad Request)} if the sourcesFinancement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sourcesFinancement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sources-financements")
    public ResponseEntity<SourcesFinancement> updateSourcesFinancement(@Valid @RequestBody SourcesFinancement sourcesFinancement) throws URISyntaxException {
        log.debug("REST request to update SourcesFinancement : {}", sourcesFinancement);
        if (sourcesFinancement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SourcesFinancement result = sourcesFinancementRepository.save(sourcesFinancement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourcesFinancement.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sources-financements} : get all the sourcesFinancements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sourcesFinancements in body.
     */
    @GetMapping("/sources-financements")
    public ResponseEntity<List<SourcesFinancement>> getAllSourcesFinancements(Pageable pageable) {
        log.debug("REST request to get a page of SourcesFinancements");
        Page<SourcesFinancement> page = sourcesFinancementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sources-financements/:id} : get the "id" sourcesFinancement.
     *
     * @param id the id of the sourcesFinancement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sourcesFinancement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sources-financements/{id}")
    public ResponseEntity<SourcesFinancement> getSourcesFinancement(@PathVariable Long id) {
        log.debug("REST request to get SourcesFinancement : {}", id);
        Optional<SourcesFinancement> sourcesFinancement = sourcesFinancementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sourcesFinancement);
    }

    /**
     * {@code DELETE  /sources-financements/:id} : delete the "id" sourcesFinancement.
     *
     * @param id the id of the sourcesFinancement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sources-financements/{id}")
    public ResponseEntity<Void> deleteSourcesFinancement(@PathVariable Long id) {
        log.debug("REST request to delete SourcesFinancement : {}", id);
        sourcesFinancementRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
