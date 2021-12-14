package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.SpecialitesPersonnel;
import sn.ssi.sigmap.repository.SpecialitesPersonnelRepository;

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
 * Integration tests for the {@link SpecialitesPersonnelResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SpecialitesPersonnelResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private SpecialitesPersonnelRepository specialitesPersonnelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialitesPersonnelMockMvc;

    private SpecialitesPersonnel specialitesPersonnel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialitesPersonnel createEntity(EntityManager em) {
        SpecialitesPersonnel specialitesPersonnel = new SpecialitesPersonnel();
        specialitesPersonnel.setLibelle(DEFAULT_LIBELLE);
        return specialitesPersonnel;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialitesPersonnel createUpdatedEntity(EntityManager em) {
        SpecialitesPersonnel specialitesPersonnel = new SpecialitesPersonnel();
        specialitesPersonnel.setLibelle(UPDATED_LIBELLE);
        return specialitesPersonnel;
    }

    @BeforeEach
    public void initTest() {
        specialitesPersonnel = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpecialitesPersonnel() throws Exception {
        int databaseSizeBeforeCreate = specialitesPersonnelRepository.findAll().size();
        // Create the SpecialitesPersonnel
        restSpecialitesPersonnelMockMvc.perform(post("/api/specialites-personnels").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialitesPersonnel)))
            .andExpect(status().isCreated());

        // Validate the SpecialitesPersonnel in the database
        List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
        assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeCreate + 1);
        SpecialitesPersonnel testSpecialitesPersonnel = specialitesPersonnelList.get(specialitesPersonnelList.size() - 1);
        assertThat(testSpecialitesPersonnel.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createSpecialitesPersonnelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = specialitesPersonnelRepository.findAll().size();

        // Create the SpecialitesPersonnel with an existing ID
        specialitesPersonnel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialitesPersonnelMockMvc.perform(post("/api/specialites-personnels").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialitesPersonnel)))
            .andExpect(status().isBadRequest());

        // Validate the SpecialitesPersonnel in the database
        List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
        assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialitesPersonnelRepository.findAll().size();
        // set the field null
        specialitesPersonnel.setLibelle(null);

        // Create the SpecialitesPersonnel, which fails.


        restSpecialitesPersonnelMockMvc.perform(post("/api/specialites-personnels").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialitesPersonnel)))
            .andExpect(status().isBadRequest());

        List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
        assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSpecialitesPersonnels() throws Exception {
        // Initialize the database
        specialitesPersonnelRepository.saveAndFlush(specialitesPersonnel);

        // Get all the specialitesPersonnelList
        restSpecialitesPersonnelMockMvc.perform(get("/api/specialites-personnels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialitesPersonnel.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
    
    @Test
    @Transactional
    public void getSpecialitesPersonnel() throws Exception {
        // Initialize the database
        specialitesPersonnelRepository.saveAndFlush(specialitesPersonnel);

        // Get the specialitesPersonnel
        restSpecialitesPersonnelMockMvc.perform(get("/api/specialites-personnels/{id}", specialitesPersonnel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialitesPersonnel.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }
    @Test
    @Transactional
    public void getNonExistingSpecialitesPersonnel() throws Exception {
        // Get the specialitesPersonnel
        restSpecialitesPersonnelMockMvc.perform(get("/api/specialites-personnels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpecialitesPersonnel() throws Exception {
        // Initialize the database
        specialitesPersonnelRepository.saveAndFlush(specialitesPersonnel);

        int databaseSizeBeforeUpdate = specialitesPersonnelRepository.findAll().size();

        // Update the specialitesPersonnel
        SpecialitesPersonnel updatedSpecialitesPersonnel = specialitesPersonnelRepository.findById(specialitesPersonnel.getId()).get();
        // Disconnect from session so that the updates on updatedSpecialitesPersonnel are not directly saved in db
        em.detach(updatedSpecialitesPersonnel);
        updatedSpecialitesPersonnel.setLibelle(UPDATED_LIBELLE);

        restSpecialitesPersonnelMockMvc.perform(put("/api/specialites-personnels").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSpecialitesPersonnel)))
            .andExpect(status().isOk());

        // Validate the SpecialitesPersonnel in the database
        List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
        assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeUpdate);
        SpecialitesPersonnel testSpecialitesPersonnel = specialitesPersonnelList.get(specialitesPersonnelList.size() - 1);
        assertThat(testSpecialitesPersonnel.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void updateNonExistingSpecialitesPersonnel() throws Exception {
        int databaseSizeBeforeUpdate = specialitesPersonnelRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialitesPersonnelMockMvc.perform(put("/api/specialites-personnels").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialitesPersonnel)))
            .andExpect(status().isBadRequest());

        // Validate the SpecialitesPersonnel in the database
        List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
        assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSpecialitesPersonnel() throws Exception {
        // Initialize the database
        specialitesPersonnelRepository.saveAndFlush(specialitesPersonnel);

        int databaseSizeBeforeDelete = specialitesPersonnelRepository.findAll().size();

        // Delete the specialitesPersonnel
        restSpecialitesPersonnelMockMvc.perform(delete("/api/specialites-personnels/{id}", specialitesPersonnel.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SpecialitesPersonnel> specialitesPersonnelList = specialitesPersonnelRepository.findAll();
        assertThat(specialitesPersonnelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
