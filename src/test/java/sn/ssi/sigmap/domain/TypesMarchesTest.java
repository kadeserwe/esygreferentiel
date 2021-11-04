package sn.ssi.sigmap.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.ssi.sigmap.web.rest.TestUtil;

public class TypesMarchesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypesMarches.class);
        TypesMarches typesMarches1 = new TypesMarches();
        typesMarches1.setId(1L);
        TypesMarches typesMarches2 = new TypesMarches();
        typesMarches2.setId(typesMarches1.getId());
        assertThat(typesMarches1).isEqualTo(typesMarches2);
        typesMarches2.setId(2L);
        assertThat(typesMarches1).isNotEqualTo(typesMarches2);
        typesMarches1.setId(null);
        assertThat(typesMarches1).isNotEqualTo(typesMarches2);
    }
}
