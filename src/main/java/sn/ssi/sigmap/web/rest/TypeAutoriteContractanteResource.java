package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.TypeAutoriteContractante;
import sn.ssi.sigmap.repository.TypeAutoriteContractanteRepository;
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
 * REST controller for managing {@link sn.ssi.sigmap.domain.TypeAutoriteContractante}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TypeAutoriteContractanteResource {

    private final Logger log = LoggerFactory.getLogger(TypeAutoriteContractanteResource.class);

    private static final String ENTITY_NAME = "referentielmsTypeAutoriteContractante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeAutoriteContractanteRepository typeAutoriteContractanteRepository;

    public TypeAutoriteContractanteResource(TypeAutoriteContractanteRepository typeAutoriteContractanteRepository) {
        this.typeAutoriteContractanteRepository = typeAutoriteContractanteRepository;
    }

    /**
     * {@code POST  /type-autorite-contractantes} : Create a new typeAutoriteContractante.
     *
     * @param typeAutoriteContractante the typeAutoriteContractante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeAutoriteContractante, or with status {@code 400 (Bad Request)} if the typeAutoriteContractante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-autorite-contractantes")
    public ResponseEntity<TypeAutoriteContractante> createTypeAutoriteContractante(@Valid @RequestBody TypeAutoriteContractante typeAutoriteContractante) throws URISyntaxException {
        log.debug("REST request to save TypeAutoriteContractante : {}", typeAutoriteContractante);
        if (typeAutoriteContractante.getId() != null) {
            throw new BadRequestAlertException("A new typeAutoriteContractante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeAutoriteContractante result = typeAutoriteContractanteRepository.save(typeAutoriteContractante);
        return ResponseEntity.created(new URI("/api/type-autorite-contractantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-autorite-contractantes} : Updates an existing typeAutoriteContractante.
     *
     * @param typeAutoriteContractante the typeAutoriteContractante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeAutoriteContractante,
     * or with status {@code 400 (Bad Request)} if the typeAutoriteContractante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeAutoriteContractante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-autorite-contractantes")
    public ResponseEntity<TypeAutoriteContractante> updateTypeAutoriteContractante(@Valid @RequestBody TypeAutoriteContractante typeAutoriteContractante) throws URISyntaxException {
        log.debug("REST request to update TypeAutoriteContractante : {}", typeAutoriteContractante);
        if (typeAutoriteContractante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeAutoriteContractante result = typeAutoriteContractanteRepository.save(typeAutoriteContractante);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeAutoriteContractante.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /type-autorite-contractantes} : get all the typeAutoriteContractantes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeAutoriteContractantes in body.
     */
    @GetMapping("/type-autorite-contractantes")
    public ResponseEntity<List<TypeAutoriteContractante>> getAllTypeAutoriteContractantes(Pageable pageable) {
        log.debug("REST request to get a page of TypeAutoriteContractantes");
        Page<TypeAutoriteContractante> page = typeAutoriteContractanteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-autorite-contractantes/:id} : get the "id" typeAutoriteContractante.
     *
     * @param id the id of the typeAutoriteContractante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeAutoriteContractante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-autorite-contractantes/{id}")
    public ResponseEntity<TypeAutoriteContractante> getTypeAutoriteContractante(@PathVariable Long id) {
        log.debug("REST request to get TypeAutoriteContractante : {}", id);
        Optional<TypeAutoriteContractante> typeAutoriteContractante = typeAutoriteContractanteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(typeAutoriteContractante);
    }

    /**
     * {@code DELETE  /type-autorite-contractantes/:id} : delete the "id" typeAutoriteContractante.
     *
     * @param id the id of the typeAutoriteContractante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-autorite-contractantes/{id}")
    public ResponseEntity<Void> deleteTypeAutoriteContractante(@PathVariable Long id) {
        log.debug("REST request to delete TypeAutoriteContractante : {}", id);
        typeAutoriteContractanteRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
