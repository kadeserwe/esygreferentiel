package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.AvisGeneraux;
import sn.ssi.sigmap.repository.AvisGenerauxRepository;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link sn.ssi.sigmap.domain.AvisGeneraux}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AvisGenerauxResource {

    private final Logger log = LoggerFactory.getLogger(AvisGenerauxResource.class);

    private static final String ENTITY_NAME = "referentielmsAvisGeneraux";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AvisGenerauxRepository avisGenerauxRepository;

    public AvisGenerauxResource(AvisGenerauxRepository avisGenerauxRepository) {
        this.avisGenerauxRepository = avisGenerauxRepository;
    }

    /**
     * {@code POST  /avis-generauxes} : Create a new avisGeneraux.
     *
     * @param avisGeneraux the avisGeneraux to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new avisGeneraux, or with status {@code 400 (Bad Request)} if the avisGeneraux has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/avis-generauxes")
    public ResponseEntity<AvisGeneraux> createAvisGeneraux(@RequestBody AvisGeneraux avisGeneraux) throws URISyntaxException {
        log.debug("REST request to save AvisGeneraux : {}", avisGeneraux);
        if (avisGeneraux.getId() != null) {
            throw new BadRequestAlertException("A new avisGeneraux cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AvisGeneraux result = avisGenerauxRepository.save(avisGeneraux);
        return ResponseEntity.created(new URI("/api/avis-generauxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /avis-generauxes} : Updates an existing avisGeneraux.
     *
     * @param avisGeneraux the avisGeneraux to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avisGeneraux,
     * or with status {@code 400 (Bad Request)} if the avisGeneraux is not valid,
     * or with status {@code 500 (Internal Server Error)} if the avisGeneraux couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/avis-generauxes")
    public ResponseEntity<AvisGeneraux> updateAvisGeneraux(@RequestBody AvisGeneraux avisGeneraux) throws URISyntaxException {
        log.debug("REST request to update AvisGeneraux : {}", avisGeneraux);
        if (avisGeneraux.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AvisGeneraux result = avisGenerauxRepository.save(avisGeneraux);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, avisGeneraux.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /avis-generauxes} : get all the avisGenerauxes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of avisGenerauxes in body.
     */
    @GetMapping("/avis-generauxes")
    public ResponseEntity<List<AvisGeneraux>> getAllAvisGenerauxes(Pageable pageable) {
        log.debug("REST request to get a page of AvisGenerauxes");
        Page<AvisGeneraux> page = avisGenerauxRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /avis-generauxes/:id} : get the "id" avisGeneraux.
     *
     * @param id the id of the avisGeneraux to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the avisGeneraux, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/avis-generauxes/{id}")
    public ResponseEntity<AvisGeneraux> getAvisGeneraux(@PathVariable Long id) {
        log.debug("REST request to get AvisGeneraux : {}", id);
        Optional<AvisGeneraux> avisGeneraux = avisGenerauxRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(avisGeneraux);
    }

    /**
     * {@code DELETE  /avis-generauxes/:id} : delete the "id" avisGeneraux.
     *
     * @param id the id of the avisGeneraux to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/avis-generauxes/{id}")
    public ResponseEntity<Void> deleteAvisGeneraux(@PathVariable Long id) {
        log.debug("REST request to delete AvisGeneraux : {}", id);
        avisGenerauxRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
