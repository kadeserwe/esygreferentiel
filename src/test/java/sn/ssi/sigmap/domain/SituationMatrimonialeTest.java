package sn.ssi.sigmap.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.ssi.sigmap.web.rest.TestUtil;

public class SituationMatrimonialeTest {

    @Test
    public void equalsVerifier() throws Exception {
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
