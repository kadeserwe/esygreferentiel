package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.ReferentielmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.Direction;
import sn.ssi.sigmap.repository.DirectionRepository;

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
 * Integration tests for the {@link DirectionResource} REST controller.
 */
@SpringBootTest(classes = { ReferentielmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class DirectionResourceIT {

    private static final String DEFAULT_SIGLE = "AAAAAAAAAA";
    private static final String UPDATED_SIGLE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDirectionMockMvc;

    private Direction direction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direction createEntity(EntityManager em) {
        Direction direction = new Direction();
        direction.setSigle(DEFAULT_SIGLE);
        direction.setLibelle(DEFAULT_LIBELLE);
        direction.setDescription(DEFAULT_DESCRIPTION);
        return direction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direction createUpdatedEntity(EntityManager em) {
        Direction direction = new Direction();
        direction.setSigle(UPDATED_SIGLE);
        direction.setLibelle(UPDATED_LIBELLE);
        direction.setDescription(UPDATED_DESCRIPTION);
        return direction;
    }

    @BeforeEach
    public void initTest() {
        direction = createEntity(em);
    }

    @Test
    @Transactional
    public void createDirection() throws Exception {
        int databaseSizeBeforeCreate = directionRepository.findAll().size();
        // Create the Direction
        restDirectionMockMvc.perform(post("/api/directions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(direction)))
            .andExpect(status().isCreated());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeCreate + 1);
        Direction testDirection = directionList.get(directionList.size() - 1);
        assertThat(testDirection.getSigle()).isEqualTo(DEFAULT_SIGLE);
        assertThat(testDirection.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testDirection.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDirectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = directionRepository.findAll().size();

        // Create the Direction with an existing ID
        direction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirectionMockMvc.perform(post("/api/directions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(direction)))
            .andExpect(status().isBadRequest());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSigleIsRequired() throws Exception {
        int databaseSizeBeforeTest = directionRepository.findAll().size();
        // set the field null
        direction.setSigle(null);

        // Create the Direction, which fails.


        restDirectionMockMvc.perform(post("/api/directions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(direction)))
            .andExpect(status().isBadRequest());

        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = directionRepository.findAll().size();
        // set the field null
        direction.setLibelle(null);

        // Create the Direction, which fails.


        restDirectionMockMvc.perform(post("/api/directions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(direction)))
            .andExpect(status().isBadRequest());

        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDirections() throws Exception {
        // Initialize the database
        directionRepository.saveAndFlush(direction);

        // Get all the directionList
        restDirectionMockMvc.perform(get("/api/directions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(direction.getId().intValue())))
            .andExpect(jsonPath("$.[*].sigle").value(hasItem(DEFAULT_SIGLE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getDirection() throws Exception {
        // Initialize the database
        directionRepository.saveAndFlush(direction);

        // Get the direction
        restDirectionMockMvc.perform(get("/api/directions/{id}", direction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(direction.getId().intValue()))
            .andExpect(jsonPath("$.sigle").value(DEFAULT_SIGLE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingDirection() throws Exception {
        // Get the direction
        restDirectionMockMvc.perform(get("/api/directions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDirection() throws Exception {
        // Initialize the database
        directionRepository.saveAndFlush(direction);

        int databaseSizeBeforeUpdate = directionRepository.findAll().size();

        // Update the direction
        Direction updatedDirection = directionRepository.findById(direction.getId()).get();
        // Disconnect from session so that the updates on updatedDirection are not directly saved in db
        em.detach(updatedDirection);
        updatedDirection.setSigle(UPDATED_SIGLE);
        updatedDirection.setLibelle(UPDATED_LIBELLE);
        updatedDirection.setDescription(UPDATED_DESCRIPTION);

        restDirectionMockMvc.perform(put("/api/directions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDirection)))
            .andExpect(status().isOk());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeUpdate);
        Direction testDirection = directionList.get(directionList.size() - 1);
        assertThat(testDirection.getSigle()).isEqualTo(UPDATED_SIGLE);
        assertThat(testDirection.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDirection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingDirection() throws Exception {
        int databaseSizeBeforeUpdate = directionRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectionMockMvc.perform(put("/api/directions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(direction)))
            .andExpect(status().isBadRequest());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDirection() throws Exception {
        // Initialize the database
        directionRepository.saveAndFlush(direction);

        int databaseSizeBeforeDelete = directionRepository.findAll().size();

        // Delete the direction
        restDirectionMockMvc.perform(delete("/api/directions/{id}", direction.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
