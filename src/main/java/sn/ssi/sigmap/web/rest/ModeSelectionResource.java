package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.ModeSelection;
import sn.ssi.sigmap.repository.ModeSelectionRepository;
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
 * REST controller for managing {@link sn.ssi.sigmap.domain.ModeSelection}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ModeSelectionResource {

    private final Logger log = LoggerFactory.getLogger(ModeSelectionResource.class);

    private static final String ENTITY_NAME = "referentielmsModeSelection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModeSelectionRepository modeSelectionRepository;

    public ModeSelectionResource(ModeSelectionRepository modeSelectionRepository) {
        this.modeSelectionRepository = modeSelectionRepository;
    }

    /**
     * {@code POST  /mode-selections} : Create a new modeSelection.
     *
     * @param modeSelection the modeSelection to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new modeSelection, or with status {@code 400 (Bad Request)} if the modeSelection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mode-selections")
    public ResponseEntity<ModeSelection> createModeSelection(@Valid @RequestBody ModeSelection modeSelection) throws URISyntaxException {
        log.debug("REST request to save ModeSelection : {}", modeSelection);
        if (modeSelection.getId() != null) {
            throw new BadRequestAlertException("A new modeSelection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ModeSelection result = modeSelectionRepository.save(modeSelection);
        return ResponseEntity.created(new URI("/api/mode-selections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mode-selections} : Updates an existing modeSelection.
     *
     * @param modeSelection the modeSelection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modeSelection,
     * or with status {@code 400 (Bad Request)} if the modeSelection is not valid,
     * or with status {@code 500 (Internal Server Error)} if the modeSelection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mode-selections")
    public ResponseEntity<ModeSelection> updateModeSelection(@Valid @RequestBody ModeSelection modeSelection) throws URISyntaxException {
        log.debug("REST request to update ModeSelection : {}", modeSelection);
        if (modeSelection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ModeSelection result = modeSelectionRepository.save(modeSelection);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modeSelection.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /mode-selections} : get all the modeSelections.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of modeSelections in body.
     */
    @GetMapping("/mode-selections")
    public ResponseEntity<List<ModeSelection>> getAllModeSelections(Pageable pageable) {
        log.debug("REST request to get a page of ModeSelections");
        Page<ModeSelection> page = modeSelectionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mode-selections/:id} : get the "id" modeSelection.
     *
     * @param id the id of the modeSelection to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the modeSelection, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mode-selections/{id}")
    public ResponseEntity<ModeSelection> getModeSelection(@PathVariable Long id) {
        log.debug("REST request to get ModeSelection : {}", id);
        Optional<ModeSelection> modeSelection = modeSelectionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(modeSelection);
    }

    /**
     * {@code DELETE  /mode-selections/:id} : delete the "id" modeSelection.
     *
     * @param id the id of the modeSelection to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mode-selections/{id}")
    public ResponseEntity<Void> deleteModeSelection(@PathVariable Long id) {
        log.debug("REST request to delete ModeSelection : {}", id);
        modeSelectionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
