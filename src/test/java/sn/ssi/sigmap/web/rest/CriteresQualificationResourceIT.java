package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.CriteresQualification;
import sn.ssi.sigmap.repository.CriteresQualificationRepository;

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
 * Integration tests for the {@link CriteresQualificationResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class CriteresQualificationResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private CriteresQualificationRepository criteresQualificationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCriteresQualificationMockMvc;

    private CriteresQualification criteresQualification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CriteresQualification createEntity(EntityManager em) {
        CriteresQualification criteresQualification = new CriteresQualification();
        criteresQualification.setLibelle(DEFAULT_LIBELLE);
        return criteresQualification;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CriteresQualification createUpdatedEntity(EntityManager em) {
        CriteresQualification criteresQualification = new CriteresQualification();
        criteresQualification.setLibelle(UPDATED_LIBELLE);
        return criteresQualification;
    }

    @BeforeEach
    public void initTest() {
        criteresQualification = createEntity(em);
    }

    @Test
    @Transactional
    public void createCriteresQualification() throws Exception {
        int databaseSizeBeforeCreate = criteresQualificationRepository.findAll().size();
        // Create the CriteresQualification
        restCriteresQualificationMockMvc.perform(post("/api/criteres-qualifications").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(criteresQualification)))
            .andExpect(status().isCreated());

        // Validate the CriteresQualification in the database
        List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
        assertThat(criteresQualificationList).hasSize(databaseSizeBeforeCreate + 1);
        CriteresQualification testCriteresQualification = criteresQualificationList.get(criteresQualificationList.size() - 1);
        assertThat(testCriteresQualification.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createCriteresQualificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = criteresQualificationRepository.findAll().size();

        // Create the CriteresQualification with an existing ID
        criteresQualification.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCriteresQualificationMockMvc.perform(post("/api/criteres-qualifications").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(criteresQualification)))
            .andExpect(status().isBadRequest());

        // Validate the CriteresQualification in the database
        List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
        assertThat(criteresQualificationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCriteresQualifications() throws Exception {
        // Initialize the database
        criteresQualificationRepository.saveAndFlush(criteresQualification);

        // Get all the criteresQualificationList
        restCriteresQualificationMockMvc.perform(get("/api/criteres-qualifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(criteresQualification.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
    
    @Test
    @Transactional
    public void getCriteresQualification() throws Exception {
        // Initialize the database
        criteresQualificationRepository.saveAndFlush(criteresQualification);

        // Get the criteresQualification
        restCriteresQualificationMockMvc.perform(get("/api/criteres-qualifications/{id}", criteresQualification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(criteresQualification.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }
    @Test
    @Transactional
    public void getNonExistingCriteresQualification() throws Exception {
        // Get the criteresQualification
        restCriteresQualificationMockMvc.perform(get("/api/criteres-qualifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCriteresQualification() throws Exception {
        // Initialize the database
        criteresQualificationRepository.saveAndFlush(criteresQualification);

        int databaseSizeBeforeUpdate = criteresQualificationRepository.findAll().size();

        // Update the criteresQualification
        CriteresQualification updatedCriteresQualification = criteresQualificationRepository.findById(criteresQualification.getId()).get();
        // Disconnect from session so that the updates on updatedCriteresQualification are not directly saved in db
        em.detach(updatedCriteresQualification);
        updatedCriteresQualification.setLibelle(UPDATED_LIBELLE);

        restCriteresQualificationMockMvc.perform(put("/api/criteres-qualifications").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCriteresQualification)))
            .andExpect(status().isOk());

        // Validate the CriteresQualification in the database
        List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
        assertThat(criteresQualificationList).hasSize(databaseSizeBeforeUpdate);
        CriteresQualification testCriteresQualification = criteresQualificationList.get(criteresQualificationList.size() - 1);
        assertThat(testCriteresQualification.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void updateNonExistingCriteresQualification() throws Exception {
        int databaseSizeBeforeUpdate = criteresQualificationRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCriteresQualificationMockMvc.perform(put("/api/criteres-qualifications").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(criteresQualification)))
            .andExpect(status().isBadRequest());

        // Validate the CriteresQualification in the database
        List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
        assertThat(criteresQualificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCriteresQualification() throws Exception {
        // Initialize the database
        criteresQualificationRepository.saveAndFlush(criteresQualification);

        int databaseSizeBeforeDelete = criteresQualificationRepository.findAll().size();

        // Delete the criteresQualification
        restCriteresQualificationMockMvc.perform(delete("/api/criteres-qualifications/{id}", criteresQualification.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CriteresQualification> criteresQualificationList = criteresQualificationRepository.findAll();
        assertThat(criteresQualificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
