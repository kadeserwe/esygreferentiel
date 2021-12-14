package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.CategorieFournisseur;
import sn.ssi.sigmap.repository.CategorieFournisseurRepository;

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
 * Integration tests for the {@link CategorieFournisseurResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class CategorieFournisseurResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private CategorieFournisseurRepository categorieFournisseurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategorieFournisseurMockMvc;

    private CategorieFournisseur categorieFournisseur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategorieFournisseur createEntity(EntityManager em) {
        CategorieFournisseur categorieFournisseur = new CategorieFournisseur()
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION);
        return categorieFournisseur;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategorieFournisseur createUpdatedEntity(EntityManager em) {
        CategorieFournisseur categorieFournisseur = new CategorieFournisseur()
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION);
        return categorieFournisseur;
    }

    @BeforeEach
    public void initTest() {
        categorieFournisseur = createEntity(em);
    }

    @Test
    @Transactional
    public void createCategorieFournisseur() throws Exception {
        int databaseSizeBeforeCreate = categorieFournisseurRepository.findAll().size();
        // Create the CategorieFournisseur
        restCategorieFournisseurMockMvc.perform(post("/api/categorie-fournisseurs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categorieFournisseur)))
            .andExpect(status().isCreated());

        // Validate the CategorieFournisseur in the database
        List<CategorieFournisseur> categorieFournisseurList = categorieFournisseurRepository.findAll();
        assertThat(categorieFournisseurList).hasSize(databaseSizeBeforeCreate + 1);
        CategorieFournisseur testCategorieFournisseur = categorieFournisseurList.get(categorieFournisseurList.size() - 1);
        assertThat(testCategorieFournisseur.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testCategorieFournisseur.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createCategorieFournisseurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = categorieFournisseurRepository.findAll().size();

        // Create the CategorieFournisseur with an existing ID
        categorieFournisseur.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategorieFournisseurMockMvc.perform(post("/api/categorie-fournisseurs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categorieFournisseur)))
            .andExpect(status().isBadRequest());

        // Validate the CategorieFournisseur in the database
        List<CategorieFournisseur> categorieFournisseurList = categorieFournisseurRepository.findAll();
        assertThat(categorieFournisseurList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = categorieFournisseurRepository.findAll().size();
        // set the field null
        categorieFournisseur.setLibelle(null);

        // Create the CategorieFournisseur, which fails.


        restCategorieFournisseurMockMvc.perform(post("/api/categorie-fournisseurs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categorieFournisseur)))
            .andExpect(status().isBadRequest());

        List<CategorieFournisseur> categorieFournisseurList = categorieFournisseurRepository.findAll();
        assertThat(categorieFournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = categorieFournisseurRepository.findAll().size();
        // set the field null
        categorieFournisseur.setDescription(null);

        // Create the CategorieFournisseur, which fails.


        restCategorieFournisseurMockMvc.perform(post("/api/categorie-fournisseurs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categorieFournisseur)))
            .andExpect(status().isBadRequest());

        List<CategorieFournisseur> categorieFournisseurList = categorieFournisseurRepository.findAll();
        assertThat(categorieFournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategorieFournisseurs() throws Exception {
        // Initialize the database
        categorieFournisseurRepository.saveAndFlush(categorieFournisseur);

        // Get all the categorieFournisseurList
        restCategorieFournisseurMockMvc.perform(get("/api/categorie-fournisseurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorieFournisseur.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getCategorieFournisseur() throws Exception {
        // Initialize the database
        categorieFournisseurRepository.saveAndFlush(categorieFournisseur);

        // Get the categorieFournisseur
        restCategorieFournisseurMockMvc.perform(get("/api/categorie-fournisseurs/{id}", categorieFournisseur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categorieFournisseur.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingCategorieFournisseur() throws Exception {
        // Get the categorieFournisseur
        restCategorieFournisseurMockMvc.perform(get("/api/categorie-fournisseurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategorieFournisseur() throws Exception {
        // Initialize the database
        categorieFournisseurRepository.saveAndFlush(categorieFournisseur);

        int databaseSizeBeforeUpdate = categorieFournisseurRepository.findAll().size();

        // Update the categorieFournisseur
        CategorieFournisseur updatedCategorieFournisseur = categorieFournisseurRepository.findById(categorieFournisseur.getId()).get();
        // Disconnect from session so that the updates on updatedCategorieFournisseur are not directly saved in db
        em.detach(updatedCategorieFournisseur);
        updatedCategorieFournisseur
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION);

        restCategorieFournisseurMockMvc.perform(put("/api/categorie-fournisseurs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCategorieFournisseur)))
            .andExpect(status().isOk());

        // Validate the CategorieFournisseur in the database
        List<CategorieFournisseur> categorieFournisseurList = categorieFournisseurRepository.findAll();
        assertThat(categorieFournisseurList).hasSize(databaseSizeBeforeUpdate);
        CategorieFournisseur testCategorieFournisseur = categorieFournisseurList.get(categorieFournisseurList.size() - 1);
        assertThat(testCategorieFournisseur.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCategorieFournisseur.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingCategorieFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = categorieFournisseurRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorieFournisseurMockMvc.perform(put("/api/categorie-fournisseurs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categorieFournisseur)))
            .andExpect(status().isBadRequest());

        // Validate the CategorieFournisseur in the database
        List<CategorieFournisseur> categorieFournisseurList = categorieFournisseurRepository.findAll();
        assertThat(categorieFournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCategorieFournisseur() throws Exception {
        // Initialize the database
        categorieFournisseurRepository.saveAndFlush(categorieFournisseur);

        int databaseSizeBeforeDelete = categorieFournisseurRepository.findAll().size();

        // Delete the categorieFournisseur
        restCategorieFournisseurMockMvc.perform(delete("/api/categorie-fournisseurs/{id}", categorieFournisseur.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CategorieFournisseur> categorieFournisseurList = categorieFournisseurRepository.findAll();
        assertThat(categorieFournisseurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
