package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.SourcesFinancement;
import sn.ssi.sigmap.repository.SourcesFinancementRepository;

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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SourcesFinancementResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SourcesFinancementResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_CORBEILLE = "AAAAAAAAAA";
    private static final String UPDATED_CORBEILLE = "BBBBBBBBBB";

    @Autowired
    private SourcesFinancementRepository sourcesFinancementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSourcesFinancementMockMvc;

    private SourcesFinancement sourcesFinancement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourcesFinancement createEntity(EntityManager em) {
        SourcesFinancement sourcesFinancement = new SourcesFinancement()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .corbeille(DEFAULT_CORBEILLE);
        return sourcesFinancement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourcesFinancement createUpdatedEntity(EntityManager em) {
        SourcesFinancement sourcesFinancement = new SourcesFinancement()
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .corbeille(UPDATED_CORBEILLE);
        return sourcesFinancement;
    }

    @BeforeEach
    public void initTest() {
        sourcesFinancement = createEntity(em);
    }

    @Test
    @Transactional
    public void createSourcesFinancement() throws Exception {
        int databaseSizeBeforeCreate = sourcesFinancementRepository.findAll().size();
        // Create the SourcesFinancement
        restSourcesFinancementMockMvc.perform(post("/api/sources-financements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sourcesFinancement)))
            .andExpect(status().isCreated());

        // Validate the SourcesFinancement in the database
        List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
        assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeCreate + 1);
        SourcesFinancement testSourcesFinancement = sourcesFinancementList.get(sourcesFinancementList.size() - 1);
        assertThat(testSourcesFinancement.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSourcesFinancement.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testSourcesFinancement.getCorbeille()).isEqualTo(DEFAULT_CORBEILLE);
    }

    @Test
    @Transactional
    public void createSourcesFinancementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sourcesFinancementRepository.findAll().size();

        // Create the SourcesFinancement with an existing ID
        sourcesFinancement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourcesFinancementMockMvc.perform(post("/api/sources-financements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sourcesFinancement)))
            .andExpect(status().isBadRequest());

        // Validate the SourcesFinancement in the database
        List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
        assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourcesFinancementRepository.findAll().size();
        // set the field null
        sourcesFinancement.setCode(null);

        // Create the SourcesFinancement, which fails.


        restSourcesFinancementMockMvc.perform(post("/api/sources-financements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sourcesFinancement)))
            .andExpect(status().isBadRequest());

        List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
        assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourcesFinancementRepository.findAll().size();
        // set the field null
        sourcesFinancement.setLibelle(null);

        // Create the SourcesFinancement, which fails.


        restSourcesFinancementMockMvc.perform(post("/api/sources-financements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sourcesFinancement)))
            .andExpect(status().isBadRequest());

        List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
        assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSourcesFinancements() throws Exception {
        // Initialize the database
        sourcesFinancementRepository.saveAndFlush(sourcesFinancement);

        // Get all the sourcesFinancementList
        restSourcesFinancementMockMvc.perform(get("/api/sources-financements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourcesFinancement.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].corbeille").value(hasItem(DEFAULT_CORBEILLE)));
    }
    
    @Test
    @Transactional
    public void getSourcesFinancement() throws Exception {
        // Initialize the database
        sourcesFinancementRepository.saveAndFlush(sourcesFinancement);

        // Get the sourcesFinancement
        restSourcesFinancementMockMvc.perform(get("/api/sources-financements/{id}", sourcesFinancement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sourcesFinancement.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.corbeille").value(DEFAULT_CORBEILLE));
    }
    @Test
    @Transactional
    public void getNonExistingSourcesFinancement() throws Exception {
        // Get the sourcesFinancement
        restSourcesFinancementMockMvc.perform(get("/api/sources-financements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSourcesFinancement() throws Exception {
        // Initialize the database
        sourcesFinancementRepository.saveAndFlush(sourcesFinancement);

        int databaseSizeBeforeUpdate = sourcesFinancementRepository.findAll().size();

        // Update the sourcesFinancement
        SourcesFinancement updatedSourcesFinancement = sourcesFinancementRepository.findById(sourcesFinancement.getId()).get();
        // Disconnect from session so that the updates on updatedSourcesFinancement are not directly saved in db
        em.detach(updatedSourcesFinancement);
        updatedSourcesFinancement
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .corbeille(UPDATED_CORBEILLE);

        restSourcesFinancementMockMvc.perform(put("/api/sources-financements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSourcesFinancement)))
            .andExpect(status().isOk());

        // Validate the SourcesFinancement in the database
        List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
        assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeUpdate);
        SourcesFinancement testSourcesFinancement = sourcesFinancementList.get(sourcesFinancementList.size() - 1);
        assertThat(testSourcesFinancement.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSourcesFinancement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSourcesFinancement.getCorbeille()).isEqualTo(UPDATED_CORBEILLE);
    }

    @Test
    @Transactional
    public void updateNonExistingSourcesFinancement() throws Exception {
        int databaseSizeBeforeUpdate = sourcesFinancementRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourcesFinancementMockMvc.perform(put("/api/sources-financements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sourcesFinancement)))
            .andExpect(status().isBadRequest());

        // Validate the SourcesFinancement in the database
        List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
        assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSourcesFinancement() throws Exception {
        // Initialize the database
        sourcesFinancementRepository.saveAndFlush(sourcesFinancement);

        int databaseSizeBeforeDelete = sourcesFinancementRepository.findAll().size();

        // Delete the sourcesFinancement
        restSourcesFinancementMockMvc.perform(delete("/api/sources-financements/{id}", sourcesFinancement.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SourcesFinancement> sourcesFinancementList = sourcesFinancementRepository.findAll();
        assertThat(sourcesFinancementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
