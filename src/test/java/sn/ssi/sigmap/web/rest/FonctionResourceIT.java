package sn.ssi.sigmap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.ssi.sigmap.IntegrationTest;
import sn.ssi.sigmap.domain.Fonction;
import sn.ssi.sigmap.repository.FonctionRepository;

/**
 * Integration tests for the {@link FonctionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FonctionResourceIT {

  private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
  private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  private static final String ENTITY_API_URL = "/api/fonctions";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private FonctionRepository fonctionRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restFonctionMockMvc;

  private Fonction fonction;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Fonction createEntity(EntityManager em) {
    Fonction fonction = new Fonction().libelle(DEFAULT_LIBELLE).description(DEFAULT_DESCRIPTION);
    return fonction;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Fonction createUpdatedEntity(EntityManager em) {
    Fonction fonction = new Fonction().libelle(UPDATED_LIBELLE).description(UPDATED_DESCRIPTION);
    return fonction;
  }

  @BeforeEach
  public void initTest() {
    fonction = createEntity(em);
  }

  @Test
  @Transactional
  void createFonction() throws Exception {
    int databaseSizeBeforeCreate = fonctionRepository.findAll().size();
    // Create the Fonction
    restFonctionMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fonction)))
      .andExpect(status().isCreated());

    // Validate the Fonction in the database
    List<Fonction> fonctionList = fonctionRepository.findAll();
    assertThat(fonctionList).hasSize(databaseSizeBeforeCreate + 1);
    Fonction testFonction = fonctionList.get(fonctionList.size() - 1);
    assertThat(testFonction.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    assertThat(testFonction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
  }

  @Test
  @Transactional
  void createFonctionWithExistingId() throws Exception {
    // Create the Fonction with an existing ID
    fonction.setId(1L);

    int databaseSizeBeforeCreate = fonctionRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restFonctionMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fonction)))
      .andExpect(status().isBadRequest());

    // Validate the Fonction in the database
    List<Fonction> fonctionList = fonctionRepository.findAll();
    assertThat(fonctionList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkLibelleIsRequired() throws Exception {
    int databaseSizeBeforeTest = fonctionRepository.findAll().size();
    // set the field null
    fonction.setLibelle(null);

    // Create the Fonction, which fails.

    restFonctionMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fonction)))
      .andExpect(status().isBadRequest());

    List<Fonction> fonctionList = fonctionRepository.findAll();
    assertThat(fonctionList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllFonctions() throws Exception {
    // Initialize the database
    fonctionRepository.saveAndFlush(fonction);

    // Get all the fonctionList
    restFonctionMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(fonction.getId().intValue())))
      .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
      .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
  }

  @Test
  @Transactional
  void getFonction() throws Exception {
    // Initialize the database
    fonctionRepository.saveAndFlush(fonction);

    // Get the fonction
    restFonctionMockMvc
      .perform(get(ENTITY_API_URL_ID, fonction.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(fonction.getId().intValue()))
      .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
      .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
  }

  @Test
  @Transactional
  void getNonExistingFonction() throws Exception {
    // Get the fonction
    restFonctionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewFonction() throws Exception {
    // Initialize the database
    fonctionRepository.saveAndFlush(fonction);

    int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();

    // Update the fonction
    Fonction updatedFonction = fonctionRepository.findById(fonction.getId()).get();
    // Disconnect from session so that the updates on updatedFonction are not directly saved in db
    em.detach(updatedFonction);
    updatedFonction.libelle(UPDATED_LIBELLE).description(UPDATED_DESCRIPTION);

    restFonctionMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedFonction.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedFonction))
      )
      .andExpect(status().isOk());

    // Validate the Fonction in the database
    List<Fonction> fonctionList = fonctionRepository.findAll();
    assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
    Fonction testFonction = fonctionList.get(fonctionList.size() - 1);
    assertThat(testFonction.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    assertThat(testFonction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  void putNonExistingFonction() throws Exception {
    int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
    fonction.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restFonctionMockMvc
      .perform(
        put(ENTITY_API_URL_ID, fonction.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(fonction))
      )
      .andExpect(status().isBadRequest());

    // Validate the Fonction in the database
    List<Fonction> fonctionList = fonctionRepository.findAll();
    assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchFonction() throws Exception {
    int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
    fonction.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restFonctionMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(fonction))
      )
      .andExpect(status().isBadRequest());

    // Validate the Fonction in the database
    List<Fonction> fonctionList = fonctionRepository.findAll();
    assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamFonction() throws Exception {
    int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
    fonction.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restFonctionMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fonction)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Fonction in the database
    List<Fonction> fonctionList = fonctionRepository.findAll();
    assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateFonctionWithPatch() throws Exception {
    // Initialize the database
    fonctionRepository.saveAndFlush(fonction);

    int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();

    // Update the fonction using partial update
    Fonction partialUpdatedFonction = new Fonction();
    partialUpdatedFonction.setId(fonction.getId());

    restFonctionMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedFonction.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFonction))
      )
      .andExpect(status().isOk());

    // Validate the Fonction in the database
    List<Fonction> fonctionList = fonctionRepository.findAll();
    assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
    Fonction testFonction = fonctionList.get(fonctionList.size() - 1);
    assertThat(testFonction.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    assertThat(testFonction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
  }

  @Test
  @Transactional
  void fullUpdateFonctionWithPatch() throws Exception {
    // Initialize the database
    fonctionRepository.saveAndFlush(fonction);

    int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();

    // Update the fonction using partial update
    Fonction partialUpdatedFonction = new Fonction();
    partialUpdatedFonction.setId(fonction.getId());

    partialUpdatedFonction.libelle(UPDATED_LIBELLE).description(UPDATED_DESCRIPTION);

    restFonctionMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedFonction.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFonction))
      )
      .andExpect(status().isOk());

    // Validate the Fonction in the database
    List<Fonction> fonctionList = fonctionRepository.findAll();
    assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
    Fonction testFonction = fonctionList.get(fonctionList.size() - 1);
    assertThat(testFonction.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    assertThat(testFonction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  void patchNonExistingFonction() throws Exception {
    int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
    fonction.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restFonctionMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, fonction.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(fonction))
      )
      .andExpect(status().isBadRequest());

    // Validate the Fonction in the database
    List<Fonction> fonctionList = fonctionRepository.findAll();
    assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchFonction() throws Exception {
    int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
    fonction.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restFonctionMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(fonction))
      )
      .andExpect(status().isBadRequest());

    // Validate the Fonction in the database
    List<Fonction> fonctionList = fonctionRepository.findAll();
    assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamFonction() throws Exception {
    int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
    fonction.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restFonctionMockMvc
      .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fonction)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Fonction in the database
    List<Fonction> fonctionList = fonctionRepository.findAll();
    assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteFonction() throws Exception {
    // Initialize the database
    fonctionRepository.saveAndFlush(fonction);

    int databaseSizeBeforeDelete = fonctionRepository.findAll().size();

    // Delete the fonction
    restFonctionMockMvc
      .perform(delete(ENTITY_API_URL_ID, fonction.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Fonction> fonctionList = fonctionRepository.findAll();
    assertThat(fonctionList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
