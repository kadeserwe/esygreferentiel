package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.domain.PiecesAdministratives;
import sn.ssi.sigmap.repository.PiecesAdministrativesRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import sn.ssi.sigmap.domain.enumeration.enumLocalisation;
/**
 * Integration tests for the {@link PiecesAdministrativesResource} REST controller.
 */
@SpringBootTest(classes = ReferentielmsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PiecesAdministrativesResourceIT {

    private static final String DEFAULT_CODE_PIECE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_PIECE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final enumLocalisation DEFAULT_LOCALISATION = enumLocalisation.LOCALES;
    private static final enumLocalisation UPDATED_LOCALISATION = enumLocalisation.ETRANGERES;

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    @Autowired
    private PiecesAdministrativesRepository piecesAdministrativesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPiecesAdministrativesMockMvc;

    private PiecesAdministratives piecesAdministratives;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PiecesAdministratives createEntity(EntityManager em) {
        PiecesAdministratives piecesAdministratives = new PiecesAdministratives()
            .codePiece(DEFAULT_CODE_PIECE)
            .libelle(DEFAULT_LIBELLE)
            .localisation(DEFAULT_LOCALISATION)
            .type(DEFAULT_TYPE);
        return piecesAdministratives;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PiecesAdministratives createUpdatedEntity(EntityManager em) {
        PiecesAdministratives piecesAdministratives = new PiecesAdministratives()
            .codePiece(UPDATED_CODE_PIECE)
            .libelle(UPDATED_LIBELLE)
            .localisation(UPDATED_LOCALISATION)
            .type(UPDATED_TYPE);
        return piecesAdministratives;
    }

    @BeforeEach
    public void initTest() {
        piecesAdministratives = createEntity(em);
    }

    @Test
    @Transactional
    public void createPiecesAdministratives() throws Exception {
        int databaseSizeBeforeCreate = piecesAdministrativesRepository.findAll().size();
        // Create the PiecesAdministratives
        restPiecesAdministrativesMockMvc.perform(post("/api/pieces-administratives")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(piecesAdministratives)))
            .andExpect(status().isCreated());

        // Validate the PiecesAdministratives in the database
        List<PiecesAdministratives> piecesAdministrativesList = piecesAdministrativesRepository.findAll();
        assertThat(piecesAdministrativesList).hasSize(databaseSizeBeforeCreate + 1);
        PiecesAdministratives testPiecesAdministratives = piecesAdministrativesList.get(piecesAdministrativesList.size() - 1);
        assertThat(testPiecesAdministratives.getCodePiece()).isEqualTo(DEFAULT_CODE_PIECE);
        assertThat(testPiecesAdministratives.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testPiecesAdministratives.getLocalisation()).isEqualTo(DEFAULT_LOCALISATION);
        assertThat(testPiecesAdministratives.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createPiecesAdministrativesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = piecesAdministrativesRepository.findAll().size();

        // Create the PiecesAdministratives with an existing ID
        piecesAdministratives.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPiecesAdministrativesMockMvc.perform(post("/api/pieces-administratives")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(piecesAdministratives)))
            .andExpect(status().isBadRequest());

        // Validate the PiecesAdministratives in the database
        List<PiecesAdministratives> piecesAdministrativesList = piecesAdministrativesRepository.findAll();
        assertThat(piecesAdministrativesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodePieceIsRequired() throws Exception {
        int databaseSizeBeforeTest = piecesAdministrativesRepository.findAll().size();
        // set the field null
        piecesAdministratives.setCodePiece(null);

        // Create the PiecesAdministratives, which fails.


        restPiecesAdministrativesMockMvc.perform(post("/api/pieces-administratives")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(piecesAdministratives)))
            .andExpect(status().isBadRequest());

        List<PiecesAdministratives> piecesAdministrativesList = piecesAdministrativesRepository.findAll();
        assertThat(piecesAdministrativesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = piecesAdministrativesRepository.findAll().size();
        // set the field null
        piecesAdministratives.setLibelle(null);

        // Create the PiecesAdministratives, which fails.


        restPiecesAdministrativesMockMvc.perform(post("/api/pieces-administratives")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(piecesAdministratives)))
            .andExpect(status().isBadRequest());

        List<PiecesAdministratives> piecesAdministrativesList = piecesAdministrativesRepository.findAll();
        assertThat(piecesAdministrativesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocalisationIsRequired() throws Exception {
        int databaseSizeBeforeTest = piecesAdministrativesRepository.findAll().size();
        // set the field null
        piecesAdministratives.setLocalisation(null);

        // Create the PiecesAdministratives, which fails.


        restPiecesAdministrativesMockMvc.perform(post("/api/pieces-administratives")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(piecesAdministratives)))
            .andExpect(status().isBadRequest());

        List<PiecesAdministratives> piecesAdministrativesList = piecesAdministrativesRepository.findAll();
        assertThat(piecesAdministrativesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPiecesAdministratives() throws Exception {
        // Initialize the database
        piecesAdministrativesRepository.saveAndFlush(piecesAdministratives);

        // Get all the piecesAdministrativesList
        restPiecesAdministrativesMockMvc.perform(get("/api/pieces-administratives?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(piecesAdministratives.getId().intValue())))
            .andExpect(jsonPath("$.[*].codePiece").value(hasItem(DEFAULT_CODE_PIECE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].localisation").value(hasItem(DEFAULT_LOCALISATION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getPiecesAdministratives() throws Exception {
        // Initialize the database
        piecesAdministrativesRepository.saveAndFlush(piecesAdministratives);

        // Get the piecesAdministratives
        restPiecesAdministrativesMockMvc.perform(get("/api/pieces-administratives/{id}", piecesAdministratives.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(piecesAdministratives.getId().intValue()))
            .andExpect(jsonPath("$.codePiece").value(DEFAULT_CODE_PIECE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.localisation").value(DEFAULT_LOCALISATION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }
    @Test
    @Transactional
    public void getNonExistingPiecesAdministratives() throws Exception {
        // Get the piecesAdministratives
        restPiecesAdministrativesMockMvc.perform(get("/api/pieces-administratives/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePiecesAdministratives() throws Exception {
        // Initialize the database
        piecesAdministrativesRepository.saveAndFlush(piecesAdministratives);

        int databaseSizeBeforeUpdate = piecesAdministrativesRepository.findAll().size();

        // Update the piecesAdministratives
        PiecesAdministratives updatedPiecesAdministratives = piecesAdministrativesRepository.findById(piecesAdministratives.getId()).get();
        // Disconnect from session so that the updates on updatedPiecesAdministratives are not directly saved in db
        em.detach(updatedPiecesAdministratives);
        updatedPiecesAdministratives
            .codePiece(UPDATED_CODE_PIECE)
            .libelle(UPDATED_LIBELLE)
            .localisation(UPDATED_LOCALISATION)
            .type(UPDATED_TYPE);

        restPiecesAdministrativesMockMvc.perform(put("/api/pieces-administratives")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPiecesAdministratives)))
            .andExpect(status().isOk());

        // Validate the PiecesAdministratives in the database
        List<PiecesAdministratives> piecesAdministrativesList = piecesAdministrativesRepository.findAll();
        assertThat(piecesAdministrativesList).hasSize(databaseSizeBeforeUpdate);
        PiecesAdministratives testPiecesAdministratives = piecesAdministrativesList.get(piecesAdministrativesList.size() - 1);
        assertThat(testPiecesAdministratives.getCodePiece()).isEqualTo(UPDATED_CODE_PIECE);
        assertThat(testPiecesAdministratives.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testPiecesAdministratives.getLocalisation()).isEqualTo(UPDATED_LOCALISATION);
        assertThat(testPiecesAdministratives.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingPiecesAdministratives() throws Exception {
        int databaseSizeBeforeUpdate = piecesAdministrativesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPiecesAdministrativesMockMvc.perform(put("/api/pieces-administratives")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(piecesAdministratives)))
            .andExpect(status().isBadRequest());

        // Validate the PiecesAdministratives in the database
        List<PiecesAdministratives> piecesAdministrativesList = piecesAdministrativesRepository.findAll();
        assertThat(piecesAdministrativesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePiecesAdministratives() throws Exception {
        // Initialize the database
        piecesAdministrativesRepository.saveAndFlush(piecesAdministratives);

        int databaseSizeBeforeDelete = piecesAdministrativesRepository.findAll().size();

        // Delete the piecesAdministratives
        restPiecesAdministrativesMockMvc.perform(delete("/api/pieces-administratives/{id}", piecesAdministratives.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PiecesAdministratives> piecesAdministrativesList = piecesAdministrativesRepository.findAll();
        assertThat(piecesAdministrativesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
