package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.PersonnesRessources;
import sn.ssi.sigmap.repository.PersonnesRessourcesRepository;
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
 * REST controller for managing {@link sn.ssi.sigmap.domain.PersonnesRessources}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PersonnesRessourcesResource {

    private final Logger log = LoggerFactory.getLogger(PersonnesRessourcesResource.class);

    private static final String ENTITY_NAME = "referentielmsPersonnesRessources";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonnesRessourcesRepository personnesRessourcesRepository;

    public PersonnesRessourcesResource(PersonnesRessourcesRepository personnesRessourcesRepository) {
        this.personnesRessourcesRepository = personnesRessourcesRepository;
    }

    /**
     * {@code POST  /personnes-ressources} : Create a new personnesRessources.
     *
     * @param personnesRessources the personnesRessources to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personnesRessources, or with status {@code 400 (Bad Request)} if the personnesRessources has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/personnes-ressources")
    public ResponseEntity<PersonnesRessources> createPersonnesRessources(@Valid @RequestBody PersonnesRessources personnesRessources) throws URISyntaxException {
        log.debug("REST request to save PersonnesRessources : {}", personnesRessources);
        if (personnesRessources.getId() != null) {
            throw new BadRequestAlertException("A new personnesRessources cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonnesRessources result = personnesRessourcesRepository.save(personnesRessources);
        return ResponseEntity.created(new URI("/api/personnes-ressources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /personnes-ressources} : Updates an existing personnesRessources.
     *
     * @param personnesRessources the personnesRessources to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personnesRessources,
     * or with status {@code 400 (Bad Request)} if the personnesRessources is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personnesRessources couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/personnes-ressources")
    public ResponseEntity<PersonnesRessources> updatePersonnesRessources(@Valid @RequestBody PersonnesRessources personnesRessources) throws URISyntaxException {
        log.debug("REST request to update PersonnesRessources : {}", personnesRessources);
        if (personnesRessources.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PersonnesRessources result = personnesRessourcesRepository.save(personnesRessources);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personnesRessources.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /personnes-ressources} : get all the personnesRessources.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personnesRessources in body.
     */
    @GetMapping("/personnes-ressources")
    public ResponseEntity<List<PersonnesRessources>> getAllPersonnesRessources(Pageable pageable) {
        log.debug("REST request to get a page of PersonnesRessources");
        Page<PersonnesRessources> page = personnesRessourcesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /personnes-ressources/:id} : get the "id" personnesRessources.
     *
     * @param id the id of the personnesRessources to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personnesRessources, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/personnes-ressources/{id}")
    public ResponseEntity<PersonnesRessources> getPersonnesRessources(@PathVariable Long id) {
        log.debug("REST request to get PersonnesRessources : {}", id);
        Optional<PersonnesRessources> personnesRessources = personnesRessourcesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(personnesRessources);
    }

    /**
     * {@code DELETE  /personnes-ressources/:id} : delete the "id" personnesRessources.
     *
     * @param id the id of the personnesRessources to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/personnes-ressources/{id}")
    public ResponseEntity<Void> deletePersonnesRessources(@PathVariable Long id) {
        log.debug("REST request to delete PersonnesRessources : {}", id);
        personnesRessourcesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
