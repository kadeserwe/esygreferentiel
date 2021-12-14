package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.Hierarchie;
import sn.ssi.sigmap.repository.HierarchieRepository;
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
 * REST controller for managing {@link sn.ssi.sigmap.domain.Hierarchie}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HierarchieResource {

    private final Logger log = LoggerFactory.getLogger(HierarchieResource.class);

    private static final String ENTITY_NAME = "referentielmsHierarchie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HierarchieRepository hierarchieRepository;

    public HierarchieResource(HierarchieRepository hierarchieRepository) {
        this.hierarchieRepository = hierarchieRepository;
    }

    /**
     * {@code POST  /hierarchies} : Create a new hierarchie.
     *
     * @param hierarchie the hierarchie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hierarchie, or with status {@code 400 (Bad Request)} if the hierarchie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hierarchies")
    public ResponseEntity<Hierarchie> createHierarchie(@Valid @RequestBody Hierarchie hierarchie) throws URISyntaxException {
        log.debug("REST request to save Hierarchie : {}", hierarchie);
        if (hierarchie.getId() != null) {
            throw new BadRequestAlertException("A new hierarchie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hierarchie result = hierarchieRepository.save(hierarchie);
        return ResponseEntity.created(new URI("/api/hierarchies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hierarchies} : Updates an existing hierarchie.
     *
     * @param hierarchie the hierarchie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hierarchie,
     * or with status {@code 400 (Bad Request)} if the hierarchie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hierarchie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hierarchies")
    public ResponseEntity<Hierarchie> updateHierarchie(@Valid @RequestBody Hierarchie hierarchie) throws URISyntaxException {
        log.debug("REST request to update Hierarchie : {}", hierarchie);
        if (hierarchie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Hierarchie result = hierarchieRepository.save(hierarchie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hierarchie.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /hierarchies} : get all the hierarchies.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hierarchies in body.
     */
    @GetMapping("/hierarchies")
    public ResponseEntity<List<Hierarchie>> getAllHierarchies(Pageable pageable) {
        log.debug("REST request to get a page of Hierarchies");
        Page<Hierarchie> page = hierarchieRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hierarchies/:id} : get the "id" hierarchie.
     *
     * @param id the id of the hierarchie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hierarchie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hierarchies/{id}")
    public ResponseEntity<Hierarchie> getHierarchie(@PathVariable Long id) {
        log.debug("REST request to get Hierarchie : {}", id);
        Optional<Hierarchie> hierarchie = hierarchieRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(hierarchie);
    }

    /**
     * {@code DELETE  /hierarchies/:id} : delete the "id" hierarchie.
     *
     * @param id the id of the hierarchie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hierarchies/{id}")
    public ResponseEntity<Void> deleteHierarchie(@PathVariable Long id) {
        log.debug("REST request to delete Hierarchie : {}", id);
        hierarchieRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
