package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.TypeAutoriteContractante;
import sn.ssi.sigmap.repository.TypeAutoriteContractanteRepository;

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
 * Integration tests for the {@link TypeAutoriteContractanteResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class TypeAutoriteContractanteResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDRE = 1;
    private static final Integer UPDATED_ORDRE = 2;

    private static final String DEFAULT_CHAPITRE = "AAAAAAAAAA";
    private static final String UPDATED_CHAPITRE = "BBBBBBBBBB";

    @Autowired
    private TypeAutoriteContractanteRepository typeAutoriteContractanteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeAutoriteContractanteMockMvc;

    private TypeAutoriteContractante typeAutoriteContractante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeAutoriteContractante createEntity(EntityManager em) {
        TypeAutoriteContractante typeAutoriteContractante = new TypeAutoriteContractante();
        typeAutoriteContractante.setLibelle(DEFAULT_LIBELLE);
        typeAutoriteContractante.setCode(DEFAULT_CODE);
        typeAutoriteContractante.setDescription(DEFAULT_DESCRIPTION);
        typeAutoriteContractante.setOrdre(DEFAULT_ORDRE);
        typeAutoriteContractante.setChapitre(DEFAULT_CHAPITRE);
        return typeAutoriteContractante;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeAutoriteContractante createUpdatedEntity(EntityManager em) {
        TypeAutoriteContractante typeAutoriteContractante = new TypeAutoriteContractante();
        typeAutoriteContractante.setLibelle(UPDATED_LIBELLE);
        typeAutoriteContractante.setCode(UPDATED_CODE);
        typeAutoriteContractante.setDescription(UPDATED_DESCRIPTION);
        typeAutoriteContractante.setOrdre(UPDATED_ORDRE);
        typeAutoriteContractante.setChapitre(UPDATED_CHAPITRE);
        return typeAutoriteContractante;
    }

    @BeforeEach
    public void initTest() {
        typeAutoriteContractante = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeAutoriteContractante() throws Exception {
        int databaseSizeBeforeCreate = typeAutoriteContractanteRepository.findAll().size();
        // Create the TypeAutoriteContractante
        restTypeAutoriteContractanteMockMvc.perform(post("/api/type-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeAutoriteContractante)))
            .andExpect(status().isCreated());

        // Validate the TypeAutoriteContractante in the database
        List<TypeAutoriteContractante> typeAutoriteContractanteList = typeAutoriteContractanteRepository.findAll();
        assertThat(typeAutoriteContractanteList).hasSize(databaseSizeBeforeCreate + 1);
        TypeAutoriteContractante testTypeAutoriteContractante = typeAutoriteContractanteList.get(typeAutoriteContractanteList.size() - 1);
        assertThat(testTypeAutoriteContractante.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testTypeAutoriteContractante.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTypeAutoriteContractante.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTypeAutoriteContractante.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testTypeAutoriteContractante.getChapitre()).isEqualTo(DEFAULT_CHAPITRE);
    }

    @Test
    @Transactional
    public void createTypeAutoriteContractanteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeAutoriteContractanteRepository.findAll().size();

        // Create the TypeAutoriteContractante with an existing ID
        typeAutoriteContractante.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeAutoriteContractanteMockMvc.perform(post("/api/type-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeAutoriteContractante)))
            .andExpect(status().isBadRequest());

        // Validate the TypeAutoriteContractante in the database
        List<TypeAutoriteContractante> typeAutoriteContractanteList = typeAutoriteContractanteRepository.findAll();
        assertThat(typeAutoriteContractanteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeAutoriteContractanteRepository.findAll().size();
        // set the field null
        typeAutoriteContractante.setLibelle(null);

        // Create the TypeAutoriteContractante, which fails.


        restTypeAutoriteContractanteMockMvc.perform(post("/api/type-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeAutoriteContractante)))
            .andExpect(status().isBadRequest());

        List<TypeAutoriteContractante> typeAutoriteContractanteList = typeAutoriteContractanteRepository.findAll();
        assertThat(typeAutoriteContractanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeAutoriteContractanteRepository.findAll().size();
        // set the field null
        typeAutoriteContractante.setCode(null);

        // Create the TypeAutoriteContractante, which fails.


        restTypeAutoriteContractanteMockMvc.perform(post("/api/type-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeAutoriteContractante)))
            .andExpect(status().isBadRequest());

        List<TypeAutoriteContractante> typeAutoriteContractanteList = typeAutoriteContractanteRepository.findAll();
        assertThat(typeAutoriteContractanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeAutoriteContractanteRepository.findAll().size();
        // set the field null
        typeAutoriteContractante.setOrdre(null);

        // Create the TypeAutoriteContractante, which fails.


        restTypeAutoriteContractanteMockMvc.perform(post("/api/type-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeAutoriteContractante)))
            .andExpect(status().isBadRequest());

        List<TypeAutoriteContractante> typeAutoriteContractanteList = typeAutoriteContractanteRepository.findAll();
        assertThat(typeAutoriteContractanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChapitreIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeAutoriteContractanteRepository.findAll().size();
        // set the field null
        typeAutoriteContractante.setChapitre(null);

        // Create the TypeAutoriteContractante, which fails.


        restTypeAutoriteContractanteMockMvc.perform(post("/api/type-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeAutoriteContractante)))
            .andExpect(status().isBadRequest());

        List<TypeAutoriteContractante> typeAutoriteContractanteList = typeAutoriteContractanteRepository.findAll();
        assertThat(typeAutoriteContractanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTypeAutoriteContractantes() throws Exception {
        // Initialize the database
        typeAutoriteContractanteRepository.saveAndFlush(typeAutoriteContractante);

        // Get all the typeAutoriteContractanteList
        restTypeAutoriteContractanteMockMvc.perform(get("/api/type-autorite-contractantes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeAutoriteContractante.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].chapitre").value(hasItem(DEFAULT_CHAPITRE)));
    }
    
    @Test
    @Transactional
    public void getTypeAutoriteContractante() throws Exception {
        // Initialize the database
        typeAutoriteContractanteRepository.saveAndFlush(typeAutoriteContractante);

        // Get the typeAutoriteContractante
        restTypeAutoriteContractanteMockMvc.perform(get("/api/type-autorite-contractantes/{id}", typeAutoriteContractante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeAutoriteContractante.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.chapitre").value(DEFAULT_CHAPITRE));
    }
    @Test
    @Transactional
    public void getNonExistingTypeAutoriteContractante() throws Exception {
        // Get the typeAutoriteContractante
        restTypeAutoriteContractanteMockMvc.perform(get("/api/type-autorite-contractantes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeAutoriteContractante() throws Exception {
        // Initialize the database
        typeAutoriteContractanteRepository.saveAndFlush(typeAutoriteContractante);

        int databaseSizeBeforeUpdate = typeAutoriteContractanteRepository.findAll().size();

        // Update the typeAutoriteContractante
        TypeAutoriteContractante updatedTypeAutoriteContractante = typeAutoriteContractanteRepository.findById(typeAutoriteContractante.getId()).get();
        // Disconnect from session so that the updates on updatedTypeAutoriteContractante are not directly saved in db
        em.detach(updatedTypeAutoriteContractante);
        updatedTypeAutoriteContractante.setLibelle(UPDATED_LIBELLE);
        updatedTypeAutoriteContractante.setCode(UPDATED_CODE);
        updatedTypeAutoriteContractante.setDescription(UPDATED_DESCRIPTION);
        updatedTypeAutoriteContractante.setOrdre(UPDATED_ORDRE);
        updatedTypeAutoriteContractante.setChapitre(UPDATED_CHAPITRE);

        restTypeAutoriteContractanteMockMvc.perform(put("/api/type-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTypeAutoriteContractante)))
            .andExpect(status().isOk());

        // Validate the TypeAutoriteContractante in the database
        List<TypeAutoriteContractante> typeAutoriteContractanteList = typeAutoriteContractanteRepository.findAll();
        assertThat(typeAutoriteContractanteList).hasSize(databaseSizeBeforeUpdate);
        TypeAutoriteContractante testTypeAutoriteContractante = typeAutoriteContractanteList.get(typeAutoriteContractanteList.size() - 1);
        assertThat(testTypeAutoriteContractante.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTypeAutoriteContractante.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTypeAutoriteContractante.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTypeAutoriteContractante.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testTypeAutoriteContractante.getChapitre()).isEqualTo(UPDATED_CHAPITRE);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeAutoriteContractante() throws Exception {
        int databaseSizeBeforeUpdate = typeAutoriteContractanteRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeAutoriteContractanteMockMvc.perform(put("/api/type-autorite-contractantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeAutoriteContractante)))
            .andExpect(status().isBadRequest());

        // Validate the TypeAutoriteContractante in the database
        List<TypeAutoriteContractante> typeAutoriteContractanteList = typeAutoriteContractanteRepository.findAll();
        assertThat(typeAutoriteContractanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTypeAutoriteContractante() throws Exception {
        // Initialize the database
        typeAutoriteContractanteRepository.saveAndFlush(typeAutoriteContractante);

        int databaseSizeBeforeDelete = typeAutoriteContractanteRepository.findAll().size();

        // Delete the typeAutoriteContractante
        restTypeAutoriteContractanteMockMvc.perform(delete("/api/type-autorite-contractantes/{id}", typeAutoriteContractante.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeAutoriteContractante> typeAutoriteContractanteList = typeAutoriteContractanteRepository.findAll();
        assertThat(typeAutoriteContractanteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
