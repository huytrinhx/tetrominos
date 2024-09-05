package tetrominos;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.awt.Color;

public class TestBlock {
    @Test
    public void testNewBlockShouldMatchColor() {
        Block b = new Block(4, 3, 0);
        assertEquals(Color.RED, b.getColor());
    }
    
}
