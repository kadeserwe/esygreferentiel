package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.Hierarchie;
import sn.ssi.sigmap.repository.HierarchieRepository;

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
 * Integration tests for the {@link HierarchieResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class HierarchieResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private HierarchieRepository hierarchieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHierarchieMockMvc;

    private Hierarchie hierarchie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hierarchie createEntity(EntityManager em) {
        Hierarchie hierarchie = new Hierarchie();
        hierarchie.setLibelle(DEFAULT_LIBELLE);
        return hierarchie;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hierarchie createUpdatedEntity(EntityManager em) {
        Hierarchie hierarchie = new Hierarchie();
        hierarchie.setLibelle(UPDATED_LIBELLE);
        return hierarchie;
    }

    @BeforeEach
    public void initTest() {
        hierarchie = createEntity(em);
    }

    @Test
    @Transactional
    public void createHierarchie() throws Exception {
        int databaseSizeBeforeCreate = hierarchieRepository.findAll().size();
        // Create the Hierarchie
        restHierarchieMockMvc.perform(post("/api/hierarchies").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hierarchie)))
            .andExpect(status().isCreated());

        // Validate the Hierarchie in the database
        List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
        assertThat(hierarchieList).hasSize(databaseSizeBeforeCreate + 1);
        Hierarchie testHierarchie = hierarchieList.get(hierarchieList.size() - 1);
        assertThat(testHierarchie.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createHierarchieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hierarchieRepository.findAll().size();

        // Create the Hierarchie with an existing ID
        hierarchie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHierarchieMockMvc.perform(post("/api/hierarchies").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hierarchie)))
            .andExpect(status().isBadRequest());

        // Validate the Hierarchie in the database
        List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
        assertThat(hierarchieList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = hierarchieRepository.findAll().size();
        // set the field null
        hierarchie.setLibelle(null);

        // Create the Hierarchie, which fails.


        restHierarchieMockMvc.perform(post("/api/hierarchies").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hierarchie)))
            .andExpect(status().isBadRequest());

        List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
        assertThat(hierarchieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHierarchies() throws Exception {
        // Initialize the database
        hierarchieRepository.saveAndFlush(hierarchie);

        // Get all the hierarchieList
        restHierarchieMockMvc.perform(get("/api/hierarchies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hierarchie.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
    
    @Test
    @Transactional
    public void getHierarchie() throws Exception {
        // Initialize the database
        hierarchieRepository.saveAndFlush(hierarchie);

        // Get the hierarchie
        restHierarchieMockMvc.perform(get("/api/hierarchies/{id}", hierarchie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hierarchie.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }
    @Test
    @Transactional
    public void getNonExistingHierarchie() throws Exception {
        // Get the hierarchie
        restHierarchieMockMvc.perform(get("/api/hierarchies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHierarchie() throws Exception {
        // Initialize the database
        hierarchieRepository.saveAndFlush(hierarchie);

        int databaseSizeBeforeUpdate = hierarchieRepository.findAll().size();

        // Update the hierarchie
        Hierarchie updatedHierarchie = hierarchieRepository.findById(hierarchie.getId()).get();
        // Disconnect from session so that the updates on updatedHierarchie are not directly saved in db
        em.detach(updatedHierarchie);
        updatedHierarchie.setLibelle(UPDATED_LIBELLE);

        restHierarchieMockMvc.perform(put("/api/hierarchies").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedHierarchie)))
            .andExpect(status().isOk());

        // Validate the Hierarchie in the database
        List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
        assertThat(hierarchieList).hasSize(databaseSizeBeforeUpdate);
        Hierarchie testHierarchie = hierarchieList.get(hierarchieList.size() - 1);
        assertThat(testHierarchie.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void updateNonExistingHierarchie() throws Exception {
        int databaseSizeBeforeUpdate = hierarchieRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHierarchieMockMvc.perform(put("/api/hierarchies").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hierarchie)))
            .andExpect(status().isBadRequest());

        // Validate the Hierarchie in the database
        List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
        assertThat(hierarchieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHierarchie() throws Exception {
        // Initialize the database
        hierarchieRepository.saveAndFlush(hierarchie);

        int databaseSizeBeforeDelete = hierarchieRepository.findAll().size();

        // Delete the hierarchie
        restHierarchieMockMvc.perform(delete("/api/hierarchies/{id}", hierarchie.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hierarchie> hierarchieList = hierarchieRepository.findAll();
        assertThat(hierarchieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
