package sn.ssi.sigmap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ssi.sigmap.web.rest.TestUtil;

class SituationMatrimonialeTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(SituationMatrimoniale.class);
    SituationMatrimoniale situationMatrimoniale1 = new SituationMatrimoniale();
    situationMatrimoniale1.setId(1L);
    SituationMatrimoniale situationMatrimoniale2 = new SituationMatrimoniale();
    situationMatrimoniale2.setId(situationMatrimoniale1.getId());
    assertThat(situationMatrimoniale1).isEqualTo(situationMatrimoniale2);
    situationMatrimoniale2.setId(2L);
    assertThat(situationMatrimoniale1).isNotEqualTo(situationMatrimoniale2);
    situationMatrimoniale1.setId(null);
    assertThat(situationMatrimoniale1).isNotEqualTo(situationMatrimoniale2);
  }
}
