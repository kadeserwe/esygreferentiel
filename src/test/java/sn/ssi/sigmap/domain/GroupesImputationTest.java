package sn.ssi.sigmap.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.ssi.sigmap.web.rest.TestUtil;

public class GroupesImputationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupesImputation.class);
        GroupesImputation groupesImputation1 = new GroupesImputation();
        groupesImputation1.setId(1L);
        GroupesImputation groupesImputation2 = new GroupesImputation();
        groupesImputation2.setId(groupesImputation1.getId());
        assertThat(groupesImputation1).isEqualTo(groupesImputation2);
        groupesImputation2.setId(2L);
        assertThat(groupesImputation1).isNotEqualTo(groupesImputation2);
        groupesImputation1.setId(null);
        assertThat(groupesImputation1).isNotEqualTo(groupesImputation2);
    }
}
