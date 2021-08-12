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

import sn.ssi.sigmap.domain.PersonnesRessources;
import sn.ssi.sigmap.repository.PersonnesRessourcesRepository;

/**
 * Integration tests for the {@link PersonnesRessourcesResource} REST controller.
 */

@AutoConfigureMockMvc
@WithMockUser
class PersonnesRessourcesResourceIT {

  private static final String DEFAULT_NOM = "AAAAAAAAAA";
  private static final String UPDATED_NOM = "BBBBBBBBBB";

  private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
  private static final String UPDATED_PRENOM = "BBBBBBBBBB";

  private static final Long DEFAULT_TELEPHONE = 1L;
  private static final Long UPDATED_TELEPHONE = 2L;

  private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
  private static final String UPDATED_EMAIL = "BBBBBBBBBB";

  private static final String DEFAULT_FONCTION = "AAAAAAAAAA";
  private static final String UPDATED_FONCTION = "BBBBBBBBBB";

  private static final String DEFAULT_COMMENTAIRES = "AAAAAAAAAA";
  private static final String UPDATED_COMMENTAIRES = "BBBBBBBBBB";

  private static final String ENTITY_API_URL = "/api/personnes-ressources";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
      .nom(DEFAULT_NOM)
      .prenom(DEFAULT_PRENOM)
      .telephone(DEFAULT_TELEPHONE)
      .email(DEFAULT_EMAIL)
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
      .nom(UPDATED_NOM)
      .prenom(UPDATED_PRENOM)
      .telephone(UPDATED_TELEPHONE)
      .email(UPDATED_EMAIL)
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
  void createPersonnesRessources() throws Exception {
    int databaseSizeBeforeCreate = personnesRessourcesRepository.findAll().size();
    // Create the PersonnesRessources
    restPersonnesRessourcesMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnesRessources)))
      .andExpect(status().isCreated());

    // Validate the PersonnesRessources in the database
    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeCreate + 1);
    PersonnesRessources testPersonnesRessources = personnesRessourcesList.get(personnesRessourcesList.size() - 1);
    assertThat(testPersonnesRessources.getNom()).isEqualTo(DEFAULT_NOM);
    assertThat(testPersonnesRessources.getPrenom()).isEqualTo(DEFAULT_PRENOM);
    assertThat(testPersonnesRessources.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
    assertThat(testPersonnesRessources.getEmail()).isEqualTo(DEFAULT_EMAIL);
    assertThat(testPersonnesRessources.getFonction()).isEqualTo(DEFAULT_FONCTION);
    assertThat(testPersonnesRessources.getCommentaires()).isEqualTo(DEFAULT_COMMENTAIRES);
  }

  @Test
  @Transactional
  void createPersonnesRessourcesWithExistingId() throws Exception {
    // Create the PersonnesRessources with an existing ID
    personnesRessources.setId(1L);

    int databaseSizeBeforeCreate = personnesRessourcesRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restPersonnesRessourcesMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnesRessources)))
      .andExpect(status().isBadRequest());

    // Validate the PersonnesRessources in the database
    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkNomIsRequired() throws Exception {
    int databaseSizeBeforeTest = personnesRessourcesRepository.findAll().size();
    // set the field null
    personnesRessources.setNom(null);

    // Create the PersonnesRessources, which fails.

    restPersonnesRessourcesMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnesRessources)))
      .andExpect(status().isBadRequest());

    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void checkPrenomIsRequired() throws Exception {
    int databaseSizeBeforeTest = personnesRessourcesRepository.findAll().size();
    // set the field null
    personnesRessources.setPrenom(null);

    // Create the PersonnesRessources, which fails.

    restPersonnesRessourcesMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnesRessources)))
      .andExpect(status().isBadRequest());

    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void checkTelephoneIsRequired() throws Exception {
    int databaseSizeBeforeTest = personnesRessourcesRepository.findAll().size();
    // set the field null
    personnesRessources.setTelephone(null);

    // Create the PersonnesRessources, which fails.

    restPersonnesRessourcesMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnesRessources)))
      .andExpect(status().isBadRequest());

    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void checkEmailIsRequired() throws Exception {
    int databaseSizeBeforeTest = personnesRessourcesRepository.findAll().size();
    // set the field null
    personnesRessources.setEmail(null);

    // Create the PersonnesRessources, which fails.

    restPersonnesRessourcesMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnesRessources)))
      .andExpect(status().isBadRequest());

    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void checkFonctionIsRequired() throws Exception {
    int databaseSizeBeforeTest = personnesRessourcesRepository.findAll().size();
    // set the field null
    personnesRessources.setFonction(null);

    // Create the PersonnesRessources, which fails.

    restPersonnesRessourcesMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnesRessources)))
      .andExpect(status().isBadRequest());

    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllPersonnesRessources() throws Exception {
    // Initialize the database
    personnesRessourcesRepository.saveAndFlush(personnesRessources);

    // Get all the personnesRessourcesList
    restPersonnesRessourcesMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(personnesRessources.getId().intValue())))
      .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
      .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
      .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.intValue())))
      .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
      .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION)))
      .andExpect(jsonPath("$.[*].commentaires").value(hasItem(DEFAULT_COMMENTAIRES)));
  }

  @Test
  @Transactional
  void getPersonnesRessources() throws Exception {
    // Initialize the database
    personnesRessourcesRepository.saveAndFlush(personnesRessources);

    // Get the personnesRessources
    restPersonnesRessourcesMockMvc
      .perform(get(ENTITY_API_URL_ID, personnesRessources.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(personnesRessources.getId().intValue()))
      .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
      .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
      .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.intValue()))
      .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
      .andExpect(jsonPath("$.fonction").value(DEFAULT_FONCTION))
      .andExpect(jsonPath("$.commentaires").value(DEFAULT_COMMENTAIRES));
  }

  @Test
  @Transactional
  void getNonExistingPersonnesRessources() throws Exception {
    // Get the personnesRessources
    restPersonnesRessourcesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewPersonnesRessources() throws Exception {
    // Initialize the database
    personnesRessourcesRepository.saveAndFlush(personnesRessources);

    int databaseSizeBeforeUpdate = personnesRessourcesRepository.findAll().size();

    // Update the personnesRessources
    PersonnesRessources updatedPersonnesRessources = personnesRessourcesRepository.findById(personnesRessources.getId()).get();
    // Disconnect from session so that the updates on updatedPersonnesRessources are not directly saved in db
    em.detach(updatedPersonnesRessources);
    updatedPersonnesRessources
      .nom(UPDATED_NOM)
      .prenom(UPDATED_PRENOM)
      .telephone(UPDATED_TELEPHONE)
      .email(UPDATED_EMAIL)
      .fonction(UPDATED_FONCTION)
      .commentaires(UPDATED_COMMENTAIRES);

    restPersonnesRessourcesMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedPersonnesRessources.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedPersonnesRessources))
      )
      .andExpect(status().isOk());

    // Validate the PersonnesRessources in the database
    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeUpdate);
    PersonnesRessources testPersonnesRessources = personnesRessourcesList.get(personnesRessourcesList.size() - 1);
    assertThat(testPersonnesRessources.getNom()).isEqualTo(UPDATED_NOM);
    assertThat(testPersonnesRessources.getPrenom()).isEqualTo(UPDATED_PRENOM);
    assertThat(testPersonnesRessources.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    assertThat(testPersonnesRessources.getEmail()).isEqualTo(UPDATED_EMAIL);
    assertThat(testPersonnesRessources.getFonction()).isEqualTo(UPDATED_FONCTION);
    assertThat(testPersonnesRessources.getCommentaires()).isEqualTo(UPDATED_COMMENTAIRES);
  }

  @Test
  @Transactional
  void putNonExistingPersonnesRessources() throws Exception {
    int databaseSizeBeforeUpdate = personnesRessourcesRepository.findAll().size();
    personnesRessources.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restPersonnesRessourcesMockMvc
      .perform(
        put(ENTITY_API_URL_ID, personnesRessources.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(personnesRessources))
      )
      .andExpect(status().isBadRequest());

    // Validate the PersonnesRessources in the database
    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchPersonnesRessources() throws Exception {
    int databaseSizeBeforeUpdate = personnesRessourcesRepository.findAll().size();
    personnesRessources.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restPersonnesRessourcesMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(personnesRessources))
      )
      .andExpect(status().isBadRequest());

    // Validate the PersonnesRessources in the database
    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamPersonnesRessources() throws Exception {
    int databaseSizeBeforeUpdate = personnesRessourcesRepository.findAll().size();
    personnesRessources.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restPersonnesRessourcesMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnesRessources)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the PersonnesRessources in the database
    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdatePersonnesRessourcesWithPatch() throws Exception {
    // Initialize the database
    personnesRessourcesRepository.saveAndFlush(personnesRessources);

    int databaseSizeBeforeUpdate = personnesRessourcesRepository.findAll().size();

    // Update the personnesRessources using partial update
    PersonnesRessources partialUpdatedPersonnesRessources = new PersonnesRessources();
    partialUpdatedPersonnesRessources.setId(personnesRessources.getId());

    partialUpdatedPersonnesRessources.telephone(UPDATED_TELEPHONE).fonction(UPDATED_FONCTION);

    restPersonnesRessourcesMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedPersonnesRessources.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonnesRessources))
      )
      .andExpect(status().isOk());

    // Validate the PersonnesRessources in the database
    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeUpdate);
    PersonnesRessources testPersonnesRessources = personnesRessourcesList.get(personnesRessourcesList.size() - 1);
    assertThat(testPersonnesRessources.getNom()).isEqualTo(DEFAULT_NOM);
    assertThat(testPersonnesRessources.getPrenom()).isEqualTo(DEFAULT_PRENOM);
    assertThat(testPersonnesRessources.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    assertThat(testPersonnesRessources.getEmail()).isEqualTo(DEFAULT_EMAIL);
    assertThat(testPersonnesRessources.getFonction()).isEqualTo(UPDATED_FONCTION);
    assertThat(testPersonnesRessources.getCommentaires()).isEqualTo(DEFAULT_COMMENTAIRES);
  }

  @Test
  @Transactional
  void fullUpdatePersonnesRessourcesWithPatch() throws Exception {
    // Initialize the database
    personnesRessourcesRepository.saveAndFlush(personnesRessources);

    int databaseSizeBeforeUpdate = personnesRessourcesRepository.findAll().size();

    // Update the personnesRessources using partial update
    PersonnesRessources partialUpdatedPersonnesRessources = new PersonnesRessources();
    partialUpdatedPersonnesRessources.setId(personnesRessources.getId());

    partialUpdatedPersonnesRessources
      .nom(UPDATED_NOM)
      .prenom(UPDATED_PRENOM)
      .telephone(UPDATED_TELEPHONE)
      .email(UPDATED_EMAIL)
      .fonction(UPDATED_FONCTION)
      .commentaires(UPDATED_COMMENTAIRES);

    restPersonnesRessourcesMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedPersonnesRessources.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonnesRessources))
      )
      .andExpect(status().isOk());

    // Validate the PersonnesRessources in the database
    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeUpdate);
    PersonnesRessources testPersonnesRessources = personnesRessourcesList.get(personnesRessourcesList.size() - 1);
    assertThat(testPersonnesRessources.getNom()).isEqualTo(UPDATED_NOM);
    assertThat(testPersonnesRessources.getPrenom()).isEqualTo(UPDATED_PRENOM);
    assertThat(testPersonnesRessources.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    assertThat(testPersonnesRessources.getEmail()).isEqualTo(UPDATED_EMAIL);
    assertThat(testPersonnesRessources.getFonction()).isEqualTo(UPDATED_FONCTION);
    assertThat(testPersonnesRessources.getCommentaires()).isEqualTo(UPDATED_COMMENTAIRES);
  }

  @Test
  @Transactional
  void patchNonExistingPersonnesRessources() throws Exception {
    int databaseSizeBeforeUpdate = personnesRessourcesRepository.findAll().size();
    personnesRessources.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restPersonnesRessourcesMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, personnesRessources.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(personnesRessources))
      )
      .andExpect(status().isBadRequest());

    // Validate the PersonnesRessources in the database
    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchPersonnesRessources() throws Exception {
    int databaseSizeBeforeUpdate = personnesRessourcesRepository.findAll().size();
    personnesRessources.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restPersonnesRessourcesMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(personnesRessources))
      )
      .andExpect(status().isBadRequest());

    // Validate the PersonnesRessources in the database
    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamPersonnesRessources() throws Exception {
    int databaseSizeBeforeUpdate = personnesRessourcesRepository.findAll().size();
    personnesRessources.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restPersonnesRessourcesMockMvc
      .perform(
        patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personnesRessources))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the PersonnesRessources in the database
    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deletePersonnesRessources() throws Exception {
    // Initialize the database
    personnesRessourcesRepository.saveAndFlush(personnesRessources);

    int databaseSizeBeforeDelete = personnesRessourcesRepository.findAll().size();

    // Delete the personnesRessources
    restPersonnesRessourcesMockMvc
      .perform(delete(ENTITY_API_URL_ID, personnesRessources.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<PersonnesRessources> personnesRessourcesList = personnesRessourcesRepository.findAll();
    assertThat(personnesRessourcesList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
