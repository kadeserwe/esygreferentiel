package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.SpecialitesPersonnel;
import sn.ssi.sigmap.repository.SpecialitesPersonnelRepository;
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
 * REST controller for managing {@link sn.ssi.sigmap.domain.SpecialitesPersonnel}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SpecialitesPersonnelResource {

    private final Logger log = LoggerFactory.getLogger(SpecialitesPersonnelResource.class);

    private static final String ENTITY_NAME = "referentielmsSpecialitesPersonnel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialitesPersonnelRepository specialitesPersonnelRepository;

    public SpecialitesPersonnelResource(SpecialitesPersonnelRepository specialitesPersonnelRepository) {
        this.specialitesPersonnelRepository = specialitesPersonnelRepository;
    }

    /**
     * {@code POST  /specialites-personnels} : Create a new specialitesPersonnel.
     *
     * @param specialitesPersonnel the specialitesPersonnel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialitesPersonnel, or with status {@code 400 (Bad Request)} if the specialitesPersonnel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/specialites-personnels")
    public ResponseEntity<SpecialitesPersonnel> createSpecialitesPersonnel(@Valid @RequestBody SpecialitesPersonnel specialitesPersonnel) throws URISyntaxException {
        log.debug("REST request to save SpecialitesPersonnel : {}", specialitesPersonnel);
        if (specialitesPersonnel.getId() != null) {
            throw new BadRequestAlertException("A new specialitesPersonnel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpecialitesPersonnel result = specialitesPersonnelRepository.save(specialitesPersonnel);
        return ResponseEntity.created(new URI("/api/specialites-personnels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /specialites-personnels} : Updates an existing specialitesPersonnel.
     *
     * @param specialitesPersonnel the specialitesPersonnel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialitesPersonnel,
     * or with status {@code 400 (Bad Request)} if the specialitesPersonnel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialitesPersonnel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/specialites-personnels")
    public ResponseEntity<SpecialitesPersonnel> updateSpecialitesPersonnel(@Valid @RequestBody SpecialitesPersonnel specialitesPersonnel) throws URISyntaxException {
        log.debug("REST request to update SpecialitesPersonnel : {}", specialitesPersonnel);
        if (specialitesPersonnel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SpecialitesPersonnel result = specialitesPersonnelRepository.save(specialitesPersonnel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialitesPersonnel.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /specialites-personnels} : get all the specialitesPersonnels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialitesPersonnels in body.
     */
    @GetMapping("/specialites-personnels")
    public ResponseEntity<List<SpecialitesPersonnel>> getAllSpecialitesPersonnels(Pageable pageable) {
        log.debug("REST request to get a page of SpecialitesPersonnels");
        Page<SpecialitesPersonnel> page = specialitesPersonnelRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /specialites-personnels/:id} : get the "id" specialitesPersonnel.
     *
     * @param id the id of the specialitesPersonnel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialitesPersonnel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/specialites-personnels/{id}")
    public ResponseEntity<SpecialitesPersonnel> getSpecialitesPersonnel(@PathVariable Long id) {
        log.debug("REST request to get SpecialitesPersonnel : {}", id);
        Optional<SpecialitesPersonnel> specialitesPersonnel = specialitesPersonnelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(specialitesPersonnel);
    }

    /**
     * {@code DELETE  /specialites-personnels/:id} : delete the "id" specialitesPersonnel.
     *
     * @param id the id of the specialitesPersonnel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/specialites-personnels/{id}")
    public ResponseEntity<Void> deleteSpecialitesPersonnel(@PathVariable Long id) {
        log.debug("REST request to delete SpecialitesPersonnel : {}", id);
        specialitesPersonnelRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
