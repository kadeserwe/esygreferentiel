package sn.ssi.sigmap.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.ssi.sigmap.web.rest.TestUtil;

public class CriteresQualificationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CriteresQualification.class);
        CriteresQualification criteresQualification1 = new CriteresQualification();
        criteresQualification1.setId(1L);
        CriteresQualification criteresQualification2 = new CriteresQualification();
        criteresQualification2.setId(criteresQualification1.getId());
        assertThat(criteresQualification1).isEqualTo(criteresQualification2);
        criteresQualification2.setId(2L);
        assertThat(criteresQualification1).isNotEqualTo(criteresQualification2);
        criteresQualification1.setId(null);
        assertThat(criteresQualification1).isNotEqualTo(criteresQualification2);
    }
}
