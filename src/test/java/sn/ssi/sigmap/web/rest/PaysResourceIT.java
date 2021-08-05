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
import sn.ssi.sigmap.domain.Pays;
import sn.ssi.sigmap.repository.PaysRepository;

/**
 * Integration tests for the {@link PaysResource} REST controller.
 */

@AutoConfigureMockMvc
@WithMockUser
class PaysResourceIT {

  private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
  private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

  private static final String DEFAULT_CODEPAYS = "AAAAAAAAAA";
  private static final String UPDATED_CODEPAYS = "BBBBBBBBBB";

  private static final String ENTITY_API_URL = "/api/pays";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private PaysRepository paysRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restPaysMockMvc;

  private Pays pays;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Pays createEntity(EntityManager em) {
    Pays pays = new Pays().libelle(DEFAULT_LIBELLE).codepays(DEFAULT_CODEPAYS);
    return pays;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Pays createUpdatedEntity(EntityManager em) {
    Pays pays = new Pays().libelle(UPDATED_LIBELLE).codepays(UPDATED_CODEPAYS);
    return pays;
  }

  @BeforeEach
  public void initTest() {
    pays = createEntity(em);
  }

  @Test
  @Transactional
  void createPays() throws Exception {
    int databaseSizeBeforeCreate = paysRepository.findAll().size();
    // Create the Pays
    restPaysMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pays)))
      .andExpect(status().isCreated());

    // Validate the Pays in the database
    List<Pays> paysList = paysRepository.findAll();
    assertThat(paysList).hasSize(databaseSizeBeforeCreate + 1);
    Pays testPays = paysList.get(paysList.size() - 1);
    assertThat(testPays.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    assertThat(testPays.getCodepays()).isEqualTo(DEFAULT_CODEPAYS);
  }

  @Test
  @Transactional
  void createPaysWithExistingId() throws Exception {
    // Create the Pays with an existing ID
    pays.setId(1L);

    int databaseSizeBeforeCreate = paysRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restPaysMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pays)))
      .andExpect(status().isBadRequest());

    // Validate the Pays in the database
    List<Pays> paysList = paysRepository.findAll();
    assertThat(paysList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void getAllPays() throws Exception {
    // Initialize the database
    paysRepository.saveAndFlush(pays);

    // Get all the paysList
    restPaysMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(pays.getId().intValue())))
      .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
      .andExpect(jsonPath("$.[*].codepays").value(hasItem(DEFAULT_CODEPAYS)));
  }

  @Test
  @Transactional
  void getPays() throws Exception {
    // Initialize the database
    paysRepository.saveAndFlush(pays);

    // Get the pays
    restPaysMockMvc
      .perform(get(ENTITY_API_URL_ID, pays.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(pays.getId().intValue()))
      .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
      .andExpect(jsonPath("$.codepays").value(DEFAULT_CODEPAYS));
  }

  @Test
  @Transactional
  void getNonExistingPays() throws Exception {
    // Get the pays
    restPaysMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewPays() throws Exception {
    // Initialize the database
    paysRepository.saveAndFlush(pays);

    int databaseSizeBeforeUpdate = paysRepository.findAll().size();

    // Update the pays
    Pays updatedPays = paysRepository.findById(pays.getId()).get();
    // Disconnect from session so that the updates on updatedPays are not directly saved in db
    em.detach(updatedPays);
    updatedPays.libelle(UPDATED_LIBELLE).codepays(UPDATED_CODEPAYS);

    restPaysMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedPays.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedPays))
      )
      .andExpect(status().isOk());

    // Validate the Pays in the database
    List<Pays> paysList = paysRepository.findAll();
    assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
    Pays testPays = paysList.get(paysList.size() - 1);
    assertThat(testPays.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    assertThat(testPays.getCodepays()).isEqualTo(UPDATED_CODEPAYS);
  }

  @Test
  @Transactional
  void putNonExistingPays() throws Exception {
    int databaseSizeBeforeUpdate = paysRepository.findAll().size();
    pays.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restPaysMockMvc
      .perform(
        put(ENTITY_API_URL_ID, pays.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pays))
      )
      .andExpect(status().isBadRequest());

    // Validate the Pays in the database
    List<Pays> paysList = paysRepository.findAll();
    assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchPays() throws Exception {
    int databaseSizeBeforeUpdate = paysRepository.findAll().size();
    pays.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restPaysMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(pays))
      )
      .andExpect(status().isBadRequest());

    // Validate the Pays in the database
    List<Pays> paysList = paysRepository.findAll();
    assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamPays() throws Exception {
    int databaseSizeBeforeUpdate = paysRepository.findAll().size();
    pays.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restPaysMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pays)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Pays in the database
    List<Pays> paysList = paysRepository.findAll();
    assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdatePaysWithPatch() throws Exception {
    // Initialize the database
    paysRepository.saveAndFlush(pays);

    int databaseSizeBeforeUpdate = paysRepository.findAll().size();

    // Update the pays using partial update
    Pays partialUpdatedPays = new Pays();
    partialUpdatedPays.setId(pays.getId());

    partialUpdatedPays.libelle(UPDATED_LIBELLE);

    restPaysMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedPays.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPays))
      )
      .andExpect(status().isOk());

    // Validate the Pays in the database
    List<Pays> paysList = paysRepository.findAll();
    assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
    Pays testPays = paysList.get(paysList.size() - 1);
    assertThat(testPays.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    assertThat(testPays.getCodepays()).isEqualTo(DEFAULT_CODEPAYS);
  }

  @Test
  @Transactional
  void fullUpdatePaysWithPatch() throws Exception {
    // Initialize the database
    paysRepository.saveAndFlush(pays);

    int databaseSizeBeforeUpdate = paysRepository.findAll().size();

    // Update the pays using partial update
    Pays partialUpdatedPays = new Pays();
    partialUpdatedPays.setId(pays.getId());

    partialUpdatedPays.libelle(UPDATED_LIBELLE).codepays(UPDATED_CODEPAYS);

    restPaysMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedPays.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPays))
      )
      .andExpect(status().isOk());

    // Validate the Pays in the database
    List<Pays> paysList = paysRepository.findAll();
    assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
    Pays testPays = paysList.get(paysList.size() - 1);
    assertThat(testPays.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    assertThat(testPays.getCodepays()).isEqualTo(UPDATED_CODEPAYS);
  }

  @Test
  @Transactional
  void patchNonExistingPays() throws Exception {
    int databaseSizeBeforeUpdate = paysRepository.findAll().size();
    pays.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restPaysMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, pays.getId()).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pays))
      )
      .andExpect(status().isBadRequest());

    // Validate the Pays in the database
    List<Pays> paysList = paysRepository.findAll();
    assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchPays() throws Exception {
    int databaseSizeBeforeUpdate = paysRepository.findAll().size();
    pays.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restPaysMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(pays))
      )
      .andExpect(status().isBadRequest());

    // Validate the Pays in the database
    List<Pays> paysList = paysRepository.findAll();
    assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamPays() throws Exception {
    int databaseSizeBeforeUpdate = paysRepository.findAll().size();
    pays.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restPaysMockMvc
      .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pays)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Pays in the database
    List<Pays> paysList = paysRepository.findAll();
    assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deletePays() throws Exception {
    // Initialize the database
    paysRepository.saveAndFlush(pays);

    int databaseSizeBeforeDelete = paysRepository.findAll().size();

    // Delete the pays
    restPaysMockMvc.perform(delete(ENTITY_API_URL_ID, pays.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Pays> paysList = paysRepository.findAll();
    assertThat(paysList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
