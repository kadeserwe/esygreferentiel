package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.PersonnesRessources;
import sn.ssi.sigmap.repository.PersonnesRessourcesRepository;

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
 * Integration tests for the {@link PersonnesRessourcesResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class PersonnesRessourcesResourceIT {

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Long DEFAULT_TELEPHONE = 1L;
    private static final Long UPDATED_TELEPHONE = 2L;

    private static final String DEFAULT_FONCTION = "AAAAAAAAAA";
    private static final String UPDATED_FONCTION = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRES = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRES = "BBBBBBBBBB";

    @Autowired
    private PersonnesRessourcesRepository personnesRessourcesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonnesRessourcesMockMvc;

    private PersonnesRessources personnesRessources;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonnesRessources createEntity(EntityManager em) {
        PersonnesRessources personnesRessources = new PersonnesRessources()
            .prenom(DEFAULT_PRENOM)
            .nom(DEFAULT_NOM)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .fonction(DEFAULT_FONCTION)
            .commentaires(DEFAULT_COMMENTAIRES);
        return personnesRessources;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonnesRessources createUpdatedEntity(EntityManager em) {
        PersonnesRessources personnesRessources = new PersonnesRessources()
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .fonction(UPDATED_FONCTION)
            .commentaires(UPDATED_COMMENTAIRES);
        return personnesRessources;
    }

    @BeforeEach
    public void initTest() {
        personnesRessources = createEntity(em);
    }

    @Test
    @Transactional
    public void createPersonnesRessources() throws Exception {
        int databaseSizeBeforeCreate = personnesRessourcesRepository.findAll().size();
        // Create the PersonnesRessources
        restPersonnesRessourcesMockMvc.perform(post("/api/personnes-ressources").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personnesRessources)))
            .andExpect(status().isCreated());

        // Validate the PersonnesRessources in the database
        List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
        assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeCreate + 1);
        PersonnesRessources testPersonnesRessources = personnesRessourcesList.get(personnesRessourcesList.size() - 1);
        assertThat(testPersonnesRessources.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testPersonnesRessources.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testPersonnesRessources.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPersonnesRessources.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testPersonnesRessources.getFonction()).isEqualTo(DEFAULT_FONCTION);
        assertThat(testPersonnesRessources.getCommentaires()).isEqualTo(DEFAULT_COMMENTAIRES);
    }

    @Test
    @Transactional
    public void createPersonnesRessourcesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = personnesRessourcesRepository.findAll().size();

        // Create the PersonnesRessources with an existing ID
        personnesRessources.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonnesRessourcesMockMvc.perform(post("/api/personnes-ressources").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personnesRessources)))
            .andExpect(status().isBadRequest());

        // Validate the PersonnesRessources in the database
        List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
        assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = personnesRessourcesRepository.findAll().size();
        // set the field null
        personnesRessources.setPrenom(null);

        // Create the PersonnesRessources, which fails.


        restPersonnesRessourcesMockMvc.perform(post("/api/personnes-ressources").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personnesRessources)))
            .andExpect(status().isBadRequest());

        List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
        assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = personnesRessourcesRepository.findAll().size();
        // set the field null
        personnesRessources.setNom(null);

        // Create the PersonnesRessources, which fails.


        restPersonnesRessourcesMockMvc.perform(post("/api/personnes-ressources").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personnesRessources)))
            .andExpect(status().isBadRequest());

        List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
        assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = personnesRessourcesRepository.findAll().size();
        // set the field null
        personnesRessources.setEmail(null);

        // Create the PersonnesRessources, which fails.


        restPersonnesRessourcesMockMvc.perform(post("/api/personnes-ressources").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personnesRessources)))
            .andExpect(status().isBadRequest());

        List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
        assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = personnesRessourcesRepository.findAll().size();
        // set the field null
        personnesRessources.setTelephone(null);

        // Create the PersonnesRessources, which fails.


        restPersonnesRessourcesMockMvc.perform(post("/api/personnes-ressources").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personnesRessources)))
            .andExpect(status().isBadRequest());

        List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
        assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFonctionIsRequired() throws Exception {
        int databaseSizeBeforeTest = personnesRessourcesRepository.findAll().size();
        // set the field null
        personnesRessources.setFonction(null);

        // Create the PersonnesRessources, which fails.


        restPersonnesRessourcesMockMvc.perform(post("/api/personnes-ressources").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personnesRessources)))
            .andExpect(status().isBadRequest());

        List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
        assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPersonnesRessources() throws Exception {
        // Initialize the database
        personnesRessourcesRepository.saveAndFlush(personnesRessources);

        // Get all the personnesRessourcesList
        restPersonnesRessourcesMockMvc.perform(get("/api/personnes-ressources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personnesRessources.getId().intValue())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.intValue())))
            .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION)))
            .andExpect(jsonPath("$.[*].commentaires").value(hasItem(DEFAULT_COMMENTAIRES)));
    }
    
    @Test
    @Transactional
    public void getPersonnesRessources() throws Exception {
        // Initialize the database
        personnesRessourcesRepository.saveAndFlush(personnesRessources);

        // Get the personnesRessources
        restPersonnesRessourcesMockMvc.perform(get("/api/personnes-ressources/{id}", personnesRessources.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personnesRessources.getId().intValue()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.intValue()))
            .andExpect(jsonPath("$.fonction").value(DEFAULT_FONCTION))
            .andExpect(jsonPath("$.commentaires").value(DEFAULT_COMMENTAIRES));
    }
    @Test
    @Transactional
    public void getNonExistingPersonnesRessources() throws Exception {
        // Get the personnesRessources
        restPersonnesRessourcesMockMvc.perform(get("/api/personnes-ressources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonnesRessources() throws Exception {
        // Initialize the database
        personnesRessourcesRepository.saveAndFlush(personnesRessources);

        int databaseSizeBeforeUpdate = personnesRessourcesRepository.findAll().size();

        // Update the personnesRessources
        PersonnesRessources updatedPersonnesRessources = personnesRessourcesRepository.findById(personnesRessources.getId()).get();
        // Disconnect from session so that the updates on updatedPersonnesRessources are not directly saved in db
        em.detach(updatedPersonnesRessources);
        updatedPersonnesRessources
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .fonction(UPDATED_FONCTION)
            .commentaires(UPDATED_COMMENTAIRES);

        restPersonnesRessourcesMockMvc.perform(put("/api/personnes-ressources").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPersonnesRessources)))
            .andExpect(status().isOk());

        // Validate the PersonnesRessources in the database
        List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
        assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeUpdate);
        PersonnesRessources testPersonnesRessources = personnesRessourcesList.get(personnesRessourcesList.size() - 1);
        assertThat(testPersonnesRessources.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPersonnesRessources.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPersonnesRessources.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPersonnesRessources.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testPersonnesRessources.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testPersonnesRessources.getCommentaires()).isEqualTo(UPDATED_COMMENTAIRES);
    }

    @Test
    @Transactional
    public void updateNonExistingPersonnesRessources() throws Exception {
        int databaseSizeBeforeUpdate = personnesRessourcesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonnesRessourcesMockMvc.perform(put("/api/personnes-ressources").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personnesRessources)))
            .andExpect(status().isBadRequest());

        // Validate the PersonnesRessources in the database
        List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
        assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePersonnesRessources() throws Exception {
        // Initialize the database
        personnesRessourcesRepository.saveAndFlush(personnesRessources);

        int databaseSizeBeforeDelete = personnesRessourcesRepository.findAll().size();

        // Delete the personnesRessources
        restPersonnesRessourcesMockMvc.perform(delete("/api/personnes-ressources/{id}", personnesRessources.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
        assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
