package tetrominos;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import tetrominos.models.*;

public class TestPosition {
    @Test
    public void testNewPositionShouldReturnDocString() {
        Position p = new Position(4, 3);
        assertEquals("Block (4,3)\t", p.toString());
    }
    
}
