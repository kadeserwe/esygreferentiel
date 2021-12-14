package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.JoursFeries;
import sn.ssi.sigmap.repository.JoursFeriesRepository;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link JoursFeriesResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class JoursFeriesResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private JoursFeriesRepository joursFeriesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJoursFeriesMockMvc;

    private JoursFeries joursFeries;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JoursFeries createEntity(EntityManager em) {
        JoursFeries joursFeries = new JoursFeries()
            .date(DEFAULT_DATE)
            .description(DEFAULT_DESCRIPTION);
        return joursFeries;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JoursFeries createUpdatedEntity(EntityManager em) {
        JoursFeries joursFeries = new JoursFeries()
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION);
        return joursFeries;
    }

    @BeforeEach
    public void initTest() {
        joursFeries = createEntity(em);
    }

    @Test
    @Transactional
    public void createJoursFeries() throws Exception {
        int databaseSizeBeforeCreate = joursFeriesRepository.findAll().size();
        // Create the JoursFeries
        restJoursFeriesMockMvc.perform(post("/api/jours-feries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(joursFeries)))
            .andExpect(status().isCreated());

        // Validate the JoursFeries in the database
        List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
        assertThat(joursFeriesList).hasSize(databaseSizeBeforeCreate + 1);
        JoursFeries testJoursFeries = joursFeriesList.get(joursFeriesList.size() - 1);
        assertThat(testJoursFeries.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testJoursFeries.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createJoursFeriesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = joursFeriesRepository.findAll().size();

        // Create the JoursFeries with an existing ID
        joursFeries.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJoursFeriesMockMvc.perform(post("/api/jours-feries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(joursFeries)))
            .andExpect(status().isBadRequest());

        // Validate the JoursFeries in the database
        List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
        assertThat(joursFeriesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = joursFeriesRepository.findAll().size();
        // set the field null
        joursFeries.setDate(null);

        // Create the JoursFeries, which fails.


        restJoursFeriesMockMvc.perform(post("/api/jours-feries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(joursFeries)))
            .andExpect(status().isBadRequest());

        List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
        assertThat(joursFeriesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJoursFeries() throws Exception {
        // Initialize the database
        joursFeriesRepository.saveAndFlush(joursFeries);

        // Get all the joursFeriesList
        restJoursFeriesMockMvc.perform(get("/api/jours-feries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(joursFeries.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getJoursFeries() throws Exception {
        // Initialize the database
        joursFeriesRepository.saveAndFlush(joursFeries);

        // Get the joursFeries
        restJoursFeriesMockMvc.perform(get("/api/jours-feries/{id}", joursFeries.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(joursFeries.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingJoursFeries() throws Exception {
        // Get the joursFeries
        restJoursFeriesMockMvc.perform(get("/api/jours-feries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJoursFeries() throws Exception {
        // Initialize the database
        joursFeriesRepository.saveAndFlush(joursFeries);

        int databaseSizeBeforeUpdate = joursFeriesRepository.findAll().size();

        // Update the joursFeries
        JoursFeries updatedJoursFeries = joursFeriesRepository.findById(joursFeries.getId()).get();
        // Disconnect from session so that the updates on updatedJoursFeries are not directly saved in db
        em.detach(updatedJoursFeries);
        updatedJoursFeries
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION);

        restJoursFeriesMockMvc.perform(put("/api/jours-feries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedJoursFeries)))
            .andExpect(status().isOk());

        // Validate the JoursFeries in the database
        List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
        assertThat(joursFeriesList).hasSize(databaseSizeBeforeUpdate);
        JoursFeries testJoursFeries = joursFeriesList.get(joursFeriesList.size() - 1);
        assertThat(testJoursFeries.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testJoursFeries.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingJoursFeries() throws Exception {
        int databaseSizeBeforeUpdate = joursFeriesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJoursFeriesMockMvc.perform(put("/api/jours-feries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(joursFeries)))
            .andExpect(status().isBadRequest());

        // Validate the JoursFeries in the database
        List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
        assertThat(joursFeriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJoursFeries() throws Exception {
        // Initialize the database
        joursFeriesRepository.saveAndFlush(joursFeries);

        int databaseSizeBeforeDelete = joursFeriesRepository.findAll().size();

        // Delete the joursFeries
        restJoursFeriesMockMvc.perform(delete("/api/jours-feries/{id}", joursFeries.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JoursFeries> joursFeriesList = joursFeriesRepository.findAll();
        assertThat(joursFeriesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
