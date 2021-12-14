package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.GroupesImputation;
import sn.ssi.sigmap.repository.GroupesImputationRepository;

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
 * Integration tests for the {@link GroupesImputationResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class GroupesImputationResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private GroupesImputationRepository groupesImputationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGroupesImputationMockMvc;

    private GroupesImputation groupesImputation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupesImputation createEntity(EntityManager em) {
        GroupesImputation groupesImputation = new GroupesImputation();
        groupesImputation.setLibelle(DEFAULT_LIBELLE);
        groupesImputation.setDescription(DEFAULT_DESCRIPTION);
        return groupesImputation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupesImputation createUpdatedEntity(EntityManager em) {
        GroupesImputation groupesImputation = new GroupesImputation();
        groupesImputation.setLibelle(UPDATED_LIBELLE);
        groupesImputation.setDescription(UPDATED_DESCRIPTION);
        return groupesImputation;
    }

    @BeforeEach
    public void initTest() {
        groupesImputation = createEntity(em);
    }

    @Test
    @Transactional
    public void createGroupesImputation() throws Exception {
        int databaseSizeBeforeCreate = groupesImputationRepository.findAll().size();
        // Create the GroupesImputation
        restGroupesImputationMockMvc.perform(post("/api/groupes-imputations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(groupesImputation)))
            .andExpect(status().isCreated());

        // Validate the GroupesImputation in the database
        List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
        assertThat(groupesImputationList).hasSize(databaseSizeBeforeCreate + 1);
        GroupesImputation testGroupesImputation = groupesImputationList.get(groupesImputationList.size() - 1);
        assertThat(testGroupesImputation.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testGroupesImputation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createGroupesImputationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = groupesImputationRepository.findAll().size();

        // Create the GroupesImputation with an existing ID
        groupesImputation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupesImputationMockMvc.perform(post("/api/groupes-imputations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(groupesImputation)))
            .andExpect(status().isBadRequest());

        // Validate the GroupesImputation in the database
        List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
        assertThat(groupesImputationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupesImputationRepository.findAll().size();
        // set the field null
        groupesImputation.setLibelle(null);

        // Create the GroupesImputation, which fails.


        restGroupesImputationMockMvc.perform(post("/api/groupes-imputations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(groupesImputation)))
            .andExpect(status().isBadRequest());

        List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
        assertThat(groupesImputationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGroupesImputations() throws Exception {
        // Initialize the database
        groupesImputationRepository.saveAndFlush(groupesImputation);

        // Get all the groupesImputationList
        restGroupesImputationMockMvc.perform(get("/api/groupes-imputations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupesImputation.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getGroupesImputation() throws Exception {
        // Initialize the database
        groupesImputationRepository.saveAndFlush(groupesImputation);

        // Get the groupesImputation
        restGroupesImputationMockMvc.perform(get("/api/groupes-imputations/{id}", groupesImputation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(groupesImputation.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingGroupesImputation() throws Exception {
        // Get the groupesImputation
        restGroupesImputationMockMvc.perform(get("/api/groupes-imputations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroupesImputation() throws Exception {
        // Initialize the database
        groupesImputationRepository.saveAndFlush(groupesImputation);

        int databaseSizeBeforeUpdate = groupesImputationRepository.findAll().size();

        // Update the groupesImputation
        GroupesImputation updatedGroupesImputation = groupesImputationRepository.findById(groupesImputation.getId()).get();
        // Disconnect from session so that the updates on updatedGroupesImputation are not directly saved in db
        em.detach(updatedGroupesImputation);
        updatedGroupesImputation.setLibelle(UPDATED_LIBELLE);
        updatedGroupesImputation.setDescription(UPDATED_DESCRIPTION);

        restGroupesImputationMockMvc.perform(put("/api/groupes-imputations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedGroupesImputation)))
            .andExpect(status().isOk());

        // Validate the GroupesImputation in the database
        List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
        assertThat(groupesImputationList).hasSize(databaseSizeBeforeUpdate);
        GroupesImputation testGroupesImputation = groupesImputationList.get(groupesImputationList.size() - 1);
        assertThat(testGroupesImputation.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testGroupesImputation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingGroupesImputation() throws Exception {
        int databaseSizeBeforeUpdate = groupesImputationRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupesImputationMockMvc.perform(put("/api/groupes-imputations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(groupesImputation)))
            .andExpect(status().isBadRequest());

        // Validate the GroupesImputation in the database
        List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
        assertThat(groupesImputationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGroupesImputation() throws Exception {
        // Initialize the database
        groupesImputationRepository.saveAndFlush(groupesImputation);

        int databaseSizeBeforeDelete = groupesImputationRepository.findAll().size();

        // Delete the groupesImputation
        restGroupesImputationMockMvc.perform(delete("/api/groupes-imputations/{id}", groupesImputation.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GroupesImputation> groupesImputationList = groupesImputationRepository.findAll();
        assertThat(groupesImputationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
