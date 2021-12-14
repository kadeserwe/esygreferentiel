package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.CriteresQualification;
import sn.ssi.sigmap.repository.CriteresQualificationRepository;
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
 * REST controller for managing {@link sn.ssi.sigmap.domain.CriteresQualification}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CriteresQualificationResource {

    private final Logger log = LoggerFactory.getLogger(CriteresQualificationResource.class);

    private static final String ENTITY_NAME = "referentielmsCriteresQualification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CriteresQualificationRepository criteresQualificationRepository;

    public CriteresQualificationResource(CriteresQualificationRepository criteresQualificationRepository) {
        this.criteresQualificationRepository = criteresQualificationRepository;
    }

    /**
     * {@code POST  /criteres-qualifications} : Create a new criteresQualification.
     *
     * @param criteresQualification the criteresQualification to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new criteresQualification, or with status {@code 400 (Bad Request)} if the criteresQualification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/criteres-qualifications")
    public ResponseEntity<CriteresQualification> createCriteresQualification(@RequestBody CriteresQualification criteresQualification) throws URISyntaxException {
        log.debug("REST request to save CriteresQualification : {}", criteresQualification);
        if (criteresQualification.getId() != null) {
            throw new BadRequestAlertException("A new criteresQualification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CriteresQualification result = criteresQualificationRepository.save(criteresQualification);
        return ResponseEntity.created(new URI("/api/criteres-qualifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /criteres-qualifications} : Updates an existing criteresQualification.
     *
     * @param criteresQualification the criteresQualification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated criteresQualification,
     * or with status {@code 400 (Bad Request)} if the criteresQualification is not valid,
     * or with status {@code 500 (Internal Server Error)} if the criteresQualification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/criteres-qualifications")
    public ResponseEntity<CriteresQualification> updateCriteresQualification(@RequestBody CriteresQualification criteresQualification) throws URISyntaxException {
        log.debug("REST request to update CriteresQualification : {}", criteresQualification);
        if (criteresQualification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CriteresQualification result = criteresQualificationRepository.save(criteresQualification);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, criteresQualification.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /criteres-qualifications} : get all the criteresQualifications.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of criteresQualifications in body.
     */
    @GetMapping("/criteres-qualifications")
    public ResponseEntity<List<CriteresQualification>> getAllCriteresQualifications(Pageable pageable) {
        log.debug("REST request to get a page of CriteresQualifications");
        Page<CriteresQualification> page = criteresQualificationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /criteres-qualifications/:id} : get the "id" criteresQualification.
     *
     * @param id the id of the criteresQualification to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the criteresQualification, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/criteres-qualifications/{id}")
    public ResponseEntity<CriteresQualification> getCriteresQualification(@PathVariable Long id) {
        log.debug("REST request to get CriteresQualification : {}", id);
        Optional<CriteresQualification> criteresQualification = criteresQualificationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(criteresQualification);
    }

    /**
     * {@code DELETE  /criteres-qualifications/:id} : delete the "id" criteresQualification.
     *
     * @param id the id of the criteresQualification to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/criteres-qualifications/{id}")
    public ResponseEntity<Void> deleteCriteresQualification(@PathVariable Long id) {
        log.debug("REST request to delete CriteresQualification : {}", id);
        criteresQualificationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
