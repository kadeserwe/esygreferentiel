package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.TypesMarches;
import sn.ssi.sigmap.repository.TypesMarchesRepository;
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
 * REST controller for managing {@link sn.ssi.sigmap.domain.TypesMarches}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TypesMarchesResource {

    private final Logger log = LoggerFactory.getLogger(TypesMarchesResource.class);

    private static final String ENTITY_NAME = "referentielmsTypesMarches";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypesMarchesRepository typesMarchesRepository;

    public TypesMarchesResource(TypesMarchesRepository typesMarchesRepository) {
        this.typesMarchesRepository = typesMarchesRepository;
    }

    /**
     * {@code POST  /types-marches} : Create a new typesMarches.
     *
     * @param typesMarches the typesMarches to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typesMarches, or with status {@code 400 (Bad Request)} if the typesMarches has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/types-marches")
    public ResponseEntity<TypesMarches> createTypesMarches(@Valid @RequestBody TypesMarches typesMarches) throws URISyntaxException {
        log.debug("REST request to save TypesMarches : {}", typesMarches);
        if (typesMarches.getId() != null) {
            throw new BadRequestAlertException("A new typesMarches cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypesMarches result = typesMarchesRepository.save(typesMarches);
        return ResponseEntity.created(new URI("/api/types-marches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /types-marches} : Updates an existing typesMarches.
     *
     * @param typesMarches the typesMarches to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typesMarches,
     * or with status {@code 400 (Bad Request)} if the typesMarches is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typesMarches couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/types-marches")
    public ResponseEntity<TypesMarches> updateTypesMarches(@Valid @RequestBody TypesMarches typesMarches) throws URISyntaxException {
        log.debug("REST request to update TypesMarches : {}", typesMarches);
        if (typesMarches.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypesMarches result = typesMarchesRepository.save(typesMarches);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typesMarches.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /types-marches} : get all the typesMarches.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typesMarches in body.
     */
    @GetMapping("/types-marches")
    public ResponseEntity<List<TypesMarches>> getAllTypesMarches(Pageable pageable) {
        log.debug("REST request to get a page of TypesMarches");
        Page<TypesMarches> page = typesMarchesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /types-marches/:id} : get the "id" typesMarches.
     *
     * @param id the id of the typesMarches to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typesMarches, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/types-marches/{id}")
    public ResponseEntity<TypesMarches> getTypesMarches(@PathVariable Long id) {
        log.debug("REST request to get TypesMarches : {}", id);
        Optional<TypesMarches> typesMarches = typesMarchesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(typesMarches);
    }

    /**
     * {@code DELETE  /types-marches/:id} : delete the "id" typesMarches.
     *
     * @param id the id of the typesMarches to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/types-marches/{id}")
    public ResponseEntity<Void> deleteTypesMarches(@PathVariable Long id) {
        log.debug("REST request to delete TypesMarches : {}", id);
        typesMarchesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
