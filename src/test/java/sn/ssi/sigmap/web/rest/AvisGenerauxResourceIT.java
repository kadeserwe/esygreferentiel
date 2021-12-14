package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.AvisGeneraux;
import sn.ssi.sigmap.repository.AvisGenerauxRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
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
 * Integration tests for the {@link AvisGenerauxResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class AvisGenerauxResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_ANNEE = "AAAAAAAAAA";
    private static final String UPDATED_ANNEE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FICHIER_AVIS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER_AVIS = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FICHIER_AVIS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_AVIS_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final Integer DEFAULT_LAST_VERSION_VALID = 1;
    private static final Integer UPDATED_LAST_VERSION_VALID = 2;

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_PUBLICATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_PUBLICATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AvisGenerauxRepository avisGenerauxRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAvisGenerauxMockMvc;

    private AvisGeneraux avisGeneraux;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AvisGeneraux createEntity(EntityManager em) {
        AvisGeneraux avisGeneraux = new AvisGeneraux()
            .numero(DEFAULT_NUMERO)
            .annee(DEFAULT_ANNEE)
            .description(DEFAULT_DESCRIPTION)
            .fichierAvis(DEFAULT_FICHIER_AVIS)
            .fichierAvisContentType(DEFAULT_FICHIER_AVIS_CONTENT_TYPE)
            .version(DEFAULT_VERSION)
            .lastVersionValid(DEFAULT_LAST_VERSION_VALID)
            .etat(DEFAULT_ETAT)
            .datePublication(DEFAULT_DATE_PUBLICATION);
        return avisGeneraux;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AvisGeneraux createUpdatedEntity(EntityManager em) {
        AvisGeneraux avisGeneraux = new AvisGeneraux()
            .numero(UPDATED_NUMERO)
            .annee(UPDATED_ANNEE)
            .description(UPDATED_DESCRIPTION)
            .fichierAvis(UPDATED_FICHIER_AVIS)
            .fichierAvisContentType(UPDATED_FICHIER_AVIS_CONTENT_TYPE)
            .version(UPDATED_VERSION)
            .lastVersionValid(UPDATED_LAST_VERSION_VALID)
            .etat(UPDATED_ETAT)
            .datePublication(UPDATED_DATE_PUBLICATION);
        return avisGeneraux;
    }

    @BeforeEach
    public void initTest() {
        avisGeneraux = createEntity(em);
    }

    @Test
    @Transactional
    public void createAvisGeneraux() throws Exception {
        int databaseSizeBeforeCreate = avisGenerauxRepository.findAll().size();
        // Create the AvisGeneraux
        restAvisGenerauxMockMvc.perform(post("/api/avis-generauxes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(avisGeneraux)))
            .andExpect(status().isCreated());

        // Validate the AvisGeneraux in the database
        List<AvisGeneraux> avisGenerauxList = avisGenerauxRepository.findAll();
        assertThat(avisGenerauxList).hasSize(databaseSizeBeforeCreate + 1);
        AvisGeneraux testAvisGeneraux = avisGenerauxList.get(avisGenerauxList.size() - 1);
        assertThat(testAvisGeneraux.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testAvisGeneraux.getAnnee()).isEqualTo(DEFAULT_ANNEE);
        assertThat(testAvisGeneraux.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAvisGeneraux.getFichierAvis()).isEqualTo(DEFAULT_FICHIER_AVIS);
        assertThat(testAvisGeneraux.getFichierAvisContentType()).isEqualTo(DEFAULT_FICHIER_AVIS_CONTENT_TYPE);
        assertThat(testAvisGeneraux.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testAvisGeneraux.getLastVersionValid()).isEqualTo(DEFAULT_LAST_VERSION_VALID);
        assertThat(testAvisGeneraux.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testAvisGeneraux.getDatePublication()).isEqualTo(DEFAULT_DATE_PUBLICATION);
    }

    @Test
    @Transactional
    public void createAvisGenerauxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = avisGenerauxRepository.findAll().size();

        // Create the AvisGeneraux with an existing ID
        avisGeneraux.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvisGenerauxMockMvc.perform(post("/api/avis-generauxes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(avisGeneraux)))
            .andExpect(status().isBadRequest());

        // Validate the AvisGeneraux in the database
        List<AvisGeneraux> avisGenerauxList = avisGenerauxRepository.findAll();
        assertThat(avisGenerauxList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAvisGenerauxes() throws Exception {
        // Initialize the database
        avisGenerauxRepository.saveAndFlush(avisGeneraux);

        // Get all the avisGenerauxList
        restAvisGenerauxMockMvc.perform(get("/api/avis-generauxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avisGeneraux.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fichierAvisContentType").value(hasItem(DEFAULT_FICHIER_AVIS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichierAvis").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER_AVIS))))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].lastVersionValid").value(hasItem(DEFAULT_LAST_VERSION_VALID)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].datePublication").value(hasItem(DEFAULT_DATE_PUBLICATION.toString())));
    }
    
    @Test
    @Transactional
    public void getAvisGeneraux() throws Exception {
        // Initialize the database
        avisGenerauxRepository.saveAndFlush(avisGeneraux);

        // Get the avisGeneraux
        restAvisGenerauxMockMvc.perform(get("/api/avis-generauxes/{id}", avisGeneraux.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(avisGeneraux.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fichierAvisContentType").value(DEFAULT_FICHIER_AVIS_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichierAvis").value(Base64Utils.encodeToString(DEFAULT_FICHIER_AVIS)))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.lastVersionValid").value(DEFAULT_LAST_VERSION_VALID))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT))
            .andExpect(jsonPath("$.datePublication").value(DEFAULT_DATE_PUBLICATION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingAvisGeneraux() throws Exception {
        // Get the avisGeneraux
        restAvisGenerauxMockMvc.perform(get("/api/avis-generauxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAvisGeneraux() throws Exception {
        // Initialize the database
        avisGenerauxRepository.saveAndFlush(avisGeneraux);

        int databaseSizeBeforeUpdate = avisGenerauxRepository.findAll().size();

        // Update the avisGeneraux
        AvisGeneraux updatedAvisGeneraux = avisGenerauxRepository.findById(avisGeneraux.getId()).get();
        // Disconnect from session so that the updates on updatedAvisGeneraux are not directly saved in db
        em.detach(updatedAvisGeneraux);
        updatedAvisGeneraux
            .numero(UPDATED_NUMERO)
            .annee(UPDATED_ANNEE)
            .description(UPDATED_DESCRIPTION)
            .fichierAvis(UPDATED_FICHIER_AVIS)
            .fichierAvisContentType(UPDATED_FICHIER_AVIS_CONTENT_TYPE)
            .version(UPDATED_VERSION)
            .lastVersionValid(UPDATED_LAST_VERSION_VALID)
            .etat(UPDATED_ETAT)
            .datePublication(UPDATED_DATE_PUBLICATION);

        restAvisGenerauxMockMvc.perform(put("/api/avis-generauxes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAvisGeneraux)))
            .andExpect(status().isOk());

        // Validate the AvisGeneraux in the database
        List<AvisGeneraux> avisGenerauxList = avisGenerauxRepository.findAll();
        assertThat(avisGenerauxList).hasSize(databaseSizeBeforeUpdate);
        AvisGeneraux testAvisGeneraux = avisGenerauxList.get(avisGenerauxList.size() - 1);
        assertThat(testAvisGeneraux.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testAvisGeneraux.getAnnee()).isEqualTo(UPDATED_ANNEE);
        assertThat(testAvisGeneraux.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAvisGeneraux.getFichierAvis()).isEqualTo(UPDATED_FICHIER_AVIS);
        assertThat(testAvisGeneraux.getFichierAvisContentType()).isEqualTo(UPDATED_FICHIER_AVIS_CONTENT_TYPE);
        assertThat(testAvisGeneraux.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testAvisGeneraux.getLastVersionValid()).isEqualTo(UPDATED_LAST_VERSION_VALID);
        assertThat(testAvisGeneraux.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testAvisGeneraux.getDatePublication()).isEqualTo(UPDATED_DATE_PUBLICATION);
    }

    @Test
    @Transactional
    public void updateNonExistingAvisGeneraux() throws Exception {
        int databaseSizeBeforeUpdate = avisGenerauxRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvisGenerauxMockMvc.perform(put("/api/avis-generauxes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(avisGeneraux)))
            .andExpect(status().isBadRequest());

        // Validate the AvisGeneraux in the database
        List<AvisGeneraux> avisGenerauxList = avisGenerauxRepository.findAll();
        assertThat(avisGenerauxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAvisGeneraux() throws Exception {
        // Initialize the database
        avisGenerauxRepository.saveAndFlush(avisGeneraux);

        int databaseSizeBeforeDelete = avisGenerauxRepository.findAll().size();

        // Delete the avisGeneraux
        restAvisGenerauxMockMvc.perform(delete("/api/avis-generauxes/{id}", avisGeneraux.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AvisGeneraux> avisGenerauxList = avisGenerauxRepository.findAll();
        assertThat(avisGenerauxList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
