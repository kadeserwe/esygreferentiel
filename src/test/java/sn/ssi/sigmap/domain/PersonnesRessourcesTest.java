package sn.ssi.sigmap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ssi.sigmap.web.rest.TestUtil;

class PersonnesRessourcesTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(PersonnesRessources.class);
    PersonnesRessources personnesRessources1 = new PersonnesRessources();
    personnesRessources1.setId(1L);
    PersonnesRessources personnesRessources2 = new PersonnesRessources();
    personnesRessources2.setId(personnesRessources1.getId());
    assertThat(personnesRessources1).isEqualTo(personnesRessources2);
    personnesRessources2.setId(2L);
    assertThat(personnesRessources1).isNotEqualTo(personnesRessources2);
    personnesRessources1.setId(null);
    assertThat(personnesRessources1).isNotEqualTo(personnesRessources2);
  }
}
