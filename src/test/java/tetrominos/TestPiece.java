package tetrominos;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import org.junit.*;

public class TestPiece {
    private Piece p;
    @Before
    public void init() {
        p = new Piece();
    }

    @Test
    public void testGenerateNewPiece() {  
        Block[] blocks = p.getBlocks();
        assertTrue("New piece must have 4 blocks", blocks.length == 4);
    }

    @Test
    public void testShiftDown() {
        Block b1 = new Block(0, 0, null);
        Block b2 = new Block(0, 1, null);
        Block b3 = new Block(0, 2, null);
        Block b4 = new Block(0, 3, null);
        Block[] blocks= {b1,b2,b3,b4};
        p.setBlocks(blocks);

        for (int i=0;i<10;i++) {
            p.shift(0, 1);
        }
        
        Block lastBlock = p.getBlocks()[3];
        assertTrue("After shifting down 10 steps, the y coordinate of last block must be 13", lastBlock.column==13);
    }
}