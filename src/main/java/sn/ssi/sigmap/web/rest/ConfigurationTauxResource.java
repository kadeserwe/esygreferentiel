package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.ConfigurationTaux;
import sn.ssi.sigmap.repository.ConfigurationTauxRepository;
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
 * REST controller for managing {@link sn.ssi.sigmap.domain.ConfigurationTaux}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ConfigurationTauxResource {

    private final Logger log = LoggerFactory.getLogger(ConfigurationTauxResource.class);

    private static final String ENTITY_NAME = "referentielmsConfigurationTaux";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigurationTauxRepository configurationTauxRepository;

    public ConfigurationTauxResource(ConfigurationTauxRepository configurationTauxRepository) {
        this.configurationTauxRepository = configurationTauxRepository;
    }

    /**
     * {@code POST  /configuration-tauxes} : Create a new configurationTaux.
     *
     * @param configurationTaux the configurationTaux to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configurationTaux, or with status {@code 400 (Bad Request)} if the configurationTaux has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/configuration-tauxes")
    public ResponseEntity<ConfigurationTaux> createConfigurationTaux(@Valid @RequestBody ConfigurationTaux configurationTaux) throws URISyntaxException {
        log.debug("REST request to save ConfigurationTaux : {}", configurationTaux);
        if (configurationTaux.getId() != null) {
            throw new BadRequestAlertException("A new configurationTaux cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigurationTaux result = configurationTauxRepository.save(configurationTaux);
        return ResponseEntity.created(new URI("/api/configuration-tauxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /configuration-tauxes} : Updates an existing configurationTaux.
     *
     * @param configurationTaux the configurationTaux to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configurationTaux,
     * or with status {@code 400 (Bad Request)} if the configurationTaux is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configurationTaux couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/configuration-tauxes")
    public ResponseEntity<ConfigurationTaux> updateConfigurationTaux(@Valid @RequestBody ConfigurationTaux configurationTaux) throws URISyntaxException {
        log.debug("REST request to update ConfigurationTaux : {}", configurationTaux);
        if (configurationTaux.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigurationTaux result = configurationTauxRepository.save(configurationTaux);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configurationTaux.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /configuration-tauxes} : get all the configurationTauxes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configurationTauxes in body.
     */
    @GetMapping("/configuration-tauxes")
    public ResponseEntity<List<ConfigurationTaux>> getAllConfigurationTauxes(Pageable pageable) {
        log.debug("REST request to get a page of ConfigurationTauxes");
        Page<ConfigurationTaux> page = configurationTauxRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /configuration-tauxes/:id} : get the "id" configurationTaux.
     *
     * @param id the id of the configurationTaux to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configurationTaux, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/configuration-tauxes/{id}")
    public ResponseEntity<ConfigurationTaux> getConfigurationTaux(@PathVariable Long id) {
        log.debug("REST request to get ConfigurationTaux : {}", id);
        Optional<ConfigurationTaux> configurationTaux = configurationTauxRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(configurationTaux);
    }

    /**
     * {@code DELETE  /configuration-tauxes/:id} : delete the "id" configurationTaux.
     *
     * @param id the id of the configurationTaux to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/configuration-tauxes/{id}")
    public ResponseEntity<Void> deleteConfigurationTaux(@PathVariable Long id) {
        log.debug("REST request to delete ConfigurationTaux : {}", id);
        configurationTauxRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
