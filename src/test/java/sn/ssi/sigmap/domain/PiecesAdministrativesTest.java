package sn.ssi.sigmap.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.ssi.sigmap.web.rest.TestUtil;

public class PiecesAdministrativesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PiecesAdministratives.class);
        PiecesAdministratives piecesAdministratives1 = new PiecesAdministratives();
        piecesAdministratives1.setId(1L);
        PiecesAdministratives piecesAdministratives2 = new PiecesAdministratives();
        piecesAdministratives2.setId(piecesAdministratives1.getId());
        assertThat(piecesAdministratives1).isEqualTo(piecesAdministratives2);
        piecesAdministratives2.setId(2L);
        assertThat(piecesAdministratives1).isNotEqualTo(piecesAdministratives2);
        piecesAdministratives1.setId(null);
        assertThat(piecesAdministratives1).isNotEqualTo(piecesAdministratives2);
    }
}
