package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.JoursFeries;
import sn.ssi.sigmap.repository.JoursFeriesRepository;
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
 * REST controller for managing {@link sn.ssi.sigmap.domain.JoursFeries}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class JoursFeriesResource {

    private final Logger log = LoggerFactory.getLogger(JoursFeriesResource.class);

    private static final String ENTITY_NAME = "referentielmsJoursFeries";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JoursFeriesRepository joursFeriesRepository;

    public JoursFeriesResource(JoursFeriesRepository joursFeriesRepository) {
        this.joursFeriesRepository = joursFeriesRepository;
    }

    /**
     * {@code POST  /jours-feries} : Create a new joursFeries.
     *
     * @param joursFeries the joursFeries to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new joursFeries, or with status {@code 400 (Bad Request)} if the joursFeries has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/jours-feries")
    public ResponseEntity<JoursFeries> createJoursFeries(@Valid @RequestBody JoursFeries joursFeries) throws URISyntaxException {
        log.debug("REST request to save JoursFeries : {}", joursFeries);
        if (joursFeries.getId() != null) {
            throw new BadRequestAlertException("A new joursFeries cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JoursFeries result = joursFeriesRepository.save(joursFeries);
        return ResponseEntity.created(new URI("/api/jours-feries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /jours-feries} : Updates an existing joursFeries.
     *
     * @param joursFeries the joursFeries to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated joursFeries,
     * or with status {@code 400 (Bad Request)} if the joursFeries is not valid,
     * or with status {@code 500 (Internal Server Error)} if the joursFeries couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/jours-feries")
    public ResponseEntity<JoursFeries> updateJoursFeries(@Valid @RequestBody JoursFeries joursFeries) throws URISyntaxException {
        log.debug("REST request to update JoursFeries : {}", joursFeries);
        if (joursFeries.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JoursFeries result = joursFeriesRepository.save(joursFeries);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, joursFeries.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /jours-feries} : get all the joursFeries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of joursFeries in body.
     */
    @GetMapping("/jours-feries")
    public ResponseEntity<List<JoursFeries>> getAllJoursFeries(Pageable pageable) {
        log.debug("REST request to get a page of JoursFeries");
        Page<JoursFeries> page = joursFeriesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /jours-feries/:id} : get the "id" joursFeries.
     *
     * @param id the id of the joursFeries to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the joursFeries, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/jours-feries/{id}")
    public ResponseEntity<JoursFeries> getJoursFeries(@PathVariable Long id) {
        log.debug("REST request to get JoursFeries : {}", id);
        Optional<JoursFeries> joursFeries = joursFeriesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(joursFeries);
    }

    /**
     * {@code DELETE  /jours-feries/:id} : delete the "id" joursFeries.
     *
     * @param id the id of the joursFeries to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/jours-feries/{id}")
    public ResponseEntity<Void> deleteJoursFeries(@PathVariable Long id) {
        log.debug("REST request to delete JoursFeries : {}", id);
        joursFeriesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
