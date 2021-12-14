package sn.ssi.sigmap.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.ssi.sigmap.web.rest.TestUtil;

public class SpecialitesPersonnelTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialitesPersonnel.class);
        SpecialitesPersonnel specialitesPersonnel1 = new SpecialitesPersonnel();
        specialitesPersonnel1.setId(1L);
        SpecialitesPersonnel specialitesPersonnel2 = new SpecialitesPersonnel();
        specialitesPersonnel2.setId(specialitesPersonnel1.getId());
        assertThat(specialitesPersonnel1).isEqualTo(specialitesPersonnel2);
        specialitesPersonnel2.setId(2L);
        assertThat(specialitesPersonnel1).isNotEqualTo(specialitesPersonnel2);
        specialitesPersonnel1.setId(null);
        assertThat(specialitesPersonnel1).isNotEqualTo(specialitesPersonnel2);
    }
}
