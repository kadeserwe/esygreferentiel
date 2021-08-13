package sn.ssi.sigmap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ssi.sigmap.web.rest.TestUtil;

class ModeSelectionTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ModeSelection.class);
    ModeSelection modeSelection1 = new ModeSelection();
    modeSelection1.setId(1L);
    ModeSelection modeSelection2 = new ModeSelection();
    modeSelection2.setId(modeSelection1.getId());
    assertThat(modeSelection1).isEqualTo(modeSelection2);
    modeSelection2.setId(2L);
    assertThat(modeSelection1).isNotEqualTo(modeSelection2);
    modeSelection1.setId(null);
    assertThat(modeSelection1).isNotEqualTo(modeSelection2);
  }
}
