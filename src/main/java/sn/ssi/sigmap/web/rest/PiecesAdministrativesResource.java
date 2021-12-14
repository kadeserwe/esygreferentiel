package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.PiecesAdministratives;
import sn.ssi.sigmap.repository.PiecesAdministrativesRepository;
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
 * REST controller for managing {@link sn.ssi.sigmap.domain.PiecesAdministratives}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PiecesAdministrativesResource {

    private final Logger log = LoggerFactory.getLogger(PiecesAdministrativesResource.class);

    private static final String ENTITY_NAME = "referentielmsPiecesAdministratives";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PiecesAdministrativesRepository piecesAdministrativesRepository;

    public PiecesAdministrativesResource(PiecesAdministrativesRepository piecesAdministrativesRepository) {
        this.piecesAdministrativesRepository = piecesAdministrativesRepository;
    }

    /**
     * {@code POST  /pieces-administratives} : Create a new piecesAdministratives.
     *
     * @param piecesAdministratives the piecesAdministratives to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new piecesAdministratives, or with status {@code 400 (Bad Request)} if the piecesAdministratives has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pieces-administratives")
    public ResponseEntity<PiecesAdministratives> createPiecesAdministratives(@Valid @RequestBody PiecesAdministratives piecesAdministratives) throws URISyntaxException {
        log.debug("REST request to save PiecesAdministratives : {}", piecesAdministratives);
        if (piecesAdministratives.getId() != null) {
            throw new BadRequestAlertException("A new piecesAdministratives cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PiecesAdministratives result = piecesAdministrativesRepository.save(piecesAdministratives);
        return ResponseEntity.created(new URI("/api/pieces-administratives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pieces-administratives} : Updates an existing piecesAdministratives.
     *
     * @param piecesAdministratives the piecesAdministratives to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated piecesAdministratives,
     * or with status {@code 400 (Bad Request)} if the piecesAdministratives is not valid,
     * or with status {@code 500 (Internal Server Error)} if the piecesAdministratives couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pieces-administratives")
    public ResponseEntity<PiecesAdministratives> updatePiecesAdministratives(@Valid @RequestBody PiecesAdministratives piecesAdministratives) throws URISyntaxException {
        log.debug("REST request to update PiecesAdministratives : {}", piecesAdministratives);
        if (piecesAdministratives.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PiecesAdministratives result = piecesAdministrativesRepository.save(piecesAdministratives);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, piecesAdministratives.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pieces-administratives} : get all the piecesAdministratives.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of piecesAdministratives in body.
     */
    @GetMapping("/pieces-administratives")
    public ResponseEntity<List<PiecesAdministratives>> getAllPiecesAdministratives(Pageable pageable) {
        log.debug("REST request to get a page of PiecesAdministratives");
        Page<PiecesAdministratives> page = piecesAdministrativesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pieces-administratives/:id} : get the "id" piecesAdministratives.
     *
     * @param id the id of the piecesAdministratives to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the piecesAdministratives, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pieces-administratives/{id}")
    public ResponseEntity<PiecesAdministratives> getPiecesAdministratives(@PathVariable Long id) {
        log.debug("REST request to get PiecesAdministratives : {}", id);
        Optional<PiecesAdministratives> piecesAdministratives = piecesAdministrativesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(piecesAdministratives);
    }

    /**
     * {@code DELETE  /pieces-administratives/:id} : delete the "id" piecesAdministratives.
     *
     * @param id the id of the piecesAdministratives to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pieces-administratives/{id}")
    public ResponseEntity<Void> deletePiecesAdministratives(@PathVariable Long id) {
        log.debug("REST request to delete PiecesAdministratives : {}", id);
        piecesAdministrativesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
