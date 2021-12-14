package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.TypesMarches;
import sn.ssi.sigmap.repository.TypesMarchesRepository;

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
 * Integration tests for the {@link TypesMarchesResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class TypesMarchesResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private TypesMarchesRepository typesMarchesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypesMarchesMockMvc;

    private TypesMarches typesMarches;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypesMarches createEntity(EntityManager em) {
        TypesMarches typesMarches = new TypesMarches()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION);
        return typesMarches;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypesMarches createUpdatedEntity(EntityManager em) {
        TypesMarches typesMarches = new TypesMarches()
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION);
        return typesMarches;
    }

    @BeforeEach
    public void initTest() {
        typesMarches = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypesMarches() throws Exception {
        int databaseSizeBeforeCreate = typesMarchesRepository.findAll().size();
        // Create the TypesMarches
        restTypesMarchesMockMvc.perform(post("/api/types-marches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typesMarches)))
            .andExpect(status().isCreated());

        // Validate the TypesMarches in the database
        List<TypesMarches> typesMarchesList = typesMarchesRepository.findAll();
        assertThat(typesMarchesList).hasSize(databaseSizeBeforeCreate + 1);
        TypesMarches testTypesMarches = typesMarchesList.get(typesMarchesList.size() - 1);
        assertThat(testTypesMarches.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTypesMarches.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testTypesMarches.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTypesMarchesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typesMarchesRepository.findAll().size();

        // Create the TypesMarches with an existing ID
        typesMarches.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypesMarchesMockMvc.perform(post("/api/types-marches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typesMarches)))
            .andExpect(status().isBadRequest());

        // Validate the TypesMarches in the database
        List<TypesMarches> typesMarchesList = typesMarchesRepository.findAll();
        assertThat(typesMarchesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = typesMarchesRepository.findAll().size();
        // set the field null
        typesMarches.setCode(null);

        // Create the TypesMarches, which fails.


        restTypesMarchesMockMvc.perform(post("/api/types-marches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typesMarches)))
            .andExpect(status().isBadRequest());

        List<TypesMarches> typesMarchesList = typesMarchesRepository.findAll();
        assertThat(typesMarchesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = typesMarchesRepository.findAll().size();
        // set the field null
        typesMarches.setLibelle(null);

        // Create the TypesMarches, which fails.


        restTypesMarchesMockMvc.perform(post("/api/types-marches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typesMarches)))
            .andExpect(status().isBadRequest());

        List<TypesMarches> typesMarchesList = typesMarchesRepository.findAll();
        assertThat(typesMarchesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = typesMarchesRepository.findAll().size();
        // set the field null
        typesMarches.setDescription(null);

        // Create the TypesMarches, which fails.


        restTypesMarchesMockMvc.perform(post("/api/types-marches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typesMarches)))
            .andExpect(status().isBadRequest());

        List<TypesMarches> typesMarchesList = typesMarchesRepository.findAll();
        assertThat(typesMarchesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTypesMarches() throws Exception {
        // Initialize the database
        typesMarchesRepository.saveAndFlush(typesMarches);

        // Get all the typesMarchesList
        restTypesMarchesMockMvc.perform(get("/api/types-marches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typesMarches.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getTypesMarches() throws Exception {
        // Initialize the database
        typesMarchesRepository.saveAndFlush(typesMarches);

        // Get the typesMarches
        restTypesMarchesMockMvc.perform(get("/api/types-marches/{id}", typesMarches.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typesMarches.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingTypesMarches() throws Exception {
        // Get the typesMarches
        restTypesMarchesMockMvc.perform(get("/api/types-marches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypesMarches() throws Exception {
        // Initialize the database
        typesMarchesRepository.saveAndFlush(typesMarches);

        int databaseSizeBeforeUpdate = typesMarchesRepository.findAll().size();

        // Update the typesMarches
        TypesMarches updatedTypesMarches = typesMarchesRepository.findById(typesMarches.getId()).get();
        // Disconnect from session so that the updates on updatedTypesMarches are not directly saved in db
        em.detach(updatedTypesMarches);
        updatedTypesMarches
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION);

        restTypesMarchesMockMvc.perform(put("/api/types-marches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTypesMarches)))
            .andExpect(status().isOk());

        // Validate the TypesMarches in the database
        List<TypesMarches> typesMarchesList = typesMarchesRepository.findAll();
        assertThat(typesMarchesList).hasSize(databaseSizeBeforeUpdate);
        TypesMarches testTypesMarches = typesMarchesList.get(typesMarchesList.size() - 1);
        assertThat(testTypesMarches.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTypesMarches.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTypesMarches.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingTypesMarches() throws Exception {
        int databaseSizeBeforeUpdate = typesMarchesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypesMarchesMockMvc.perform(put("/api/types-marches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typesMarches)))
            .andExpect(status().isBadRequest());

        // Validate the TypesMarches in the database
        List<TypesMarches> typesMarchesList = typesMarchesRepository.findAll();
        assertThat(typesMarchesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTypesMarches() throws Exception {
        // Initialize the database
        typesMarchesRepository.saveAndFlush(typesMarches);

        int databaseSizeBeforeDelete = typesMarchesRepository.findAll().size();

        // Delete the typesMarches
        restTypesMarchesMockMvc.perform(delete("/api/types-marches/{id}", typesMarches.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypesMarches> typesMarchesList = typesMarchesRepository.findAll();
        assertThat(typesMarchesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
