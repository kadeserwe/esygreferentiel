package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.ModeSelection;
import sn.ssi.sigmap.repository.ModeSelectionRepository;

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
 * Integration tests for the {@link ModeSelectionResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ModeSelectionResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ModeSelectionRepository modeSelectionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModeSelectionMockMvc;

    private ModeSelection modeSelection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModeSelection createEntity(EntityManager em) {
        ModeSelection modeSelection = new ModeSelection();
        modeSelection.setLibelle(DEFAULT_LIBELLE);
        modeSelection.setCode(DEFAULT_CODE);
        modeSelection.setDescription(DEFAULT_DESCRIPTION);
        return modeSelection;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModeSelection createUpdatedEntity(EntityManager em) {
        ModeSelection modeSelection = new ModeSelection();
        modeSelection.setLibelle(UPDATED_LIBELLE);
        modeSelection.setCode(UPDATED_CODE);
        modeSelection.setDescription(UPDATED_DESCRIPTION);
        return modeSelection;
    }

    @BeforeEach
    public void initTest() {
        modeSelection = createEntity(em);
    }

    @Test
    @Transactional
    public void createModeSelection() throws Exception {
        int databaseSizeBeforeCreate = modeSelectionRepository.findAll().size();
        // Create the ModeSelection
        restModeSelectionMockMvc.perform(post("/api/mode-selections").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modeSelection)))
            .andExpect(status().isCreated());

        // Validate the ModeSelection in the database
        List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
        assertThat(modeSelectionList).hasSize(databaseSizeBeforeCreate + 1);
        ModeSelection testModeSelection = modeSelectionList.get(modeSelectionList.size() - 1);
        assertThat(testModeSelection.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testModeSelection.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testModeSelection.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createModeSelectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modeSelectionRepository.findAll().size();

        // Create the ModeSelection with an existing ID
        modeSelection.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModeSelectionMockMvc.perform(post("/api/mode-selections").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modeSelection)))
            .andExpect(status().isBadRequest());

        // Validate the ModeSelection in the database
        List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
        assertThat(modeSelectionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = modeSelectionRepository.findAll().size();
        // set the field null
        modeSelection.setLibelle(null);

        // Create the ModeSelection, which fails.


        restModeSelectionMockMvc.perform(post("/api/mode-selections").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modeSelection)))
            .andExpect(status().isBadRequest());

        List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
        assertThat(modeSelectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = modeSelectionRepository.findAll().size();
        // set the field null
        modeSelection.setCode(null);

        // Create the ModeSelection, which fails.


        restModeSelectionMockMvc.perform(post("/api/mode-selections").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modeSelection)))
            .andExpect(status().isBadRequest());

        List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
        assertThat(modeSelectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllModeSelections() throws Exception {
        // Initialize the database
        modeSelectionRepository.saveAndFlush(modeSelection);

        // Get all the modeSelectionList
        restModeSelectionMockMvc.perform(get("/api/mode-selections?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modeSelection.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getModeSelection() throws Exception {
        // Initialize the database
        modeSelectionRepository.saveAndFlush(modeSelection);

        // Get the modeSelection
        restModeSelectionMockMvc.perform(get("/api/mode-selections/{id}", modeSelection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(modeSelection.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingModeSelection() throws Exception {
        // Get the modeSelection
        restModeSelectionMockMvc.perform(get("/api/mode-selections/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModeSelection() throws Exception {
        // Initialize the database
        modeSelectionRepository.saveAndFlush(modeSelection);

        int databaseSizeBeforeUpdate = modeSelectionRepository.findAll().size();

        // Update the modeSelection
        ModeSelection updatedModeSelection = modeSelectionRepository.findById(modeSelection.getId()).get();
        // Disconnect from session so that the updates on updatedModeSelection are not directly saved in db
        em.detach(updatedModeSelection);
        updatedModeSelection.setLibelle(UPDATED_LIBELLE);
        updatedModeSelection.setCode(UPDATED_CODE);
        updatedModeSelection.setDescription(UPDATED_DESCRIPTION);

        restModeSelectionMockMvc.perform(put("/api/mode-selections").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedModeSelection)))
            .andExpect(status().isOk());

        // Validate the ModeSelection in the database
        List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
        assertThat(modeSelectionList).hasSize(databaseSizeBeforeUpdate);
        ModeSelection testModeSelection = modeSelectionList.get(modeSelectionList.size() - 1);
        assertThat(testModeSelection.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testModeSelection.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testModeSelection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingModeSelection() throws Exception {
        int databaseSizeBeforeUpdate = modeSelectionRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModeSelectionMockMvc.perform(put("/api/mode-selections").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modeSelection)))
            .andExpect(status().isBadRequest());

        // Validate the ModeSelection in the database
        List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
        assertThat(modeSelectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteModeSelection() throws Exception {
        // Initialize the database
        modeSelectionRepository.saveAndFlush(modeSelection);

        int databaseSizeBeforeDelete = modeSelectionRepository.findAll().size();

        // Delete the modeSelection
        restModeSelectionMockMvc.perform(delete("/api/mode-selections/{id}", modeSelection.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ModeSelection> modeSelectionList = modeSelectionRepository.findAll();
        assertThat(modeSelectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
