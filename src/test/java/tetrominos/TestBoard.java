package tetrominos;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;;

public class TestBoard {
    private Board board;

    @BeforeEach
    public void init() {
        board = new Board(16, 32);
    }

    @Test
    public void whenRotatePieceLeftBlockCoordinatesShouldMatch() {
        Piece middlePiece = new Piece(new Block[] {
                new Block(8, 0, 0),
                new Block(8, 1, 0),
                new Block(8, 2, 0),
                new Block(9, 2, 0)
        });
        board.setActivePiece(middlePiece);
        Piece expected = new Piece(
                new Block[] {
                        new Block(8, 1, 0),
                        new Block(9, 1, 0),
                        new Block(10, 1, 0),
                        new Block(10, 0, 0)
                });

        board.getActivePiece().rotateLeft();
        board.alignActivePiece();

        assertArrayEquals(board.getActivePiece().getCurrentCordinates(), expected.getCurrentCordinates());

    }

    @Test
    public void whenRotatePieceRightBlockCoordinatesShouldMatch() {
        Piece middlePiece = new Piece(new Block[] {
                new Block(8, 0, 0),
                new Block(8, 1, 0),
                new Block(8, 2, 0),
                new Block(9, 2, 0)
        });
        board.setActivePiece(middlePiece);
        Piece expected = new Piece(
                new Block[] {
                        new Block(8, 0, 0),
                        new Block(7, 0, 0),
                        new Block(6, 0, 0),
                        new Block(6, 1, 0),
                });

        board.getActivePiece().rotateRight();
        board.alignActivePiece();

        assertArrayEquals(board.getActivePiece().getCurrentCordinates(), expected.getCurrentCordinates());

    }

    @Test
    public void whenRotateRightInLeftMostColumnMinXShouldBNotBeNegative() {
        Piece leftPiece = new Piece(new Block[] {
                new Block(0, 0, 0),
                new Block(0, 1, 0),
                new Block(0, 2, 0),
                new Block(1, 2, 0)
        });
        board.setActivePiece(leftPiece);

        board.getActivePiece().rotateRight();
        board.alignActivePiece();

        assertThat(board.getActivePiece().minX, equalTo(0));
    }

    @Test
    public void whenRotateLeftInRightMostColumnMaxXShouldNotExceedBoardColumns() {
        Piece rightPiece = new Piece(new Block[] {
                new Block(14, 0, 0),
                new Block(14, 1, 0),
                new Block(14, 2, 0),
                new Block(15, 2, 0)
        });
        board.setActivePiece(rightPiece);

        board.getActivePiece().rotateLeft();
        board.alignActivePiece();

        assertThat(board.getActivePiece().maxX, equalTo(15));
    }

    @Test
    public void whenSlideLeftLargeNumberOfStepsMinXShouldNotBeNegative() {
        Piece piece = new Piece();
        board.setActivePiece(piece);

        board.getActivePiece().slide(-100);
        board.alignActivePiece();

        assertThat(board.getActivePiece().minX, equalTo(0));

    }

    @Test
    public void whenSlideRightLargeNumberOfStepsMaxXShouldNotExceedBoardColumns() {
        Piece piece = new Piece();
        board.setActivePiece(piece);

        board.getActivePiece().slide(100);
        board.alignActivePiece();

        assertThat(board.getActivePiece().maxX, equalTo(15));
    }

    @Test
    public void whenFullRowOccuredInAnyRowAllSettledBlocksShiftDownward() {
        //Set up piece
        Piece middlePiece = new Piece(new Block[] {
            new Block(0, 27, 0),
            new Block(0, 28, 0),
            new Block(0, 29, 0),
            new Block(0, 30, 0)
        });
        //Set up board state
        int[][] currentBoardState = board.getBoardStates();
        currentBoardState[31] = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
        board.setBoardStates(currentBoardState);
        //Set up settled blocks
        ArrayList<Block> currentSettledBlocks = new ArrayList<>();
        for (int i=1; i<16; i++) {
            currentSettledBlocks.add(new Block(i,31, 0));
        }
        board.setSettledBlocks(currentSettledBlocks);
        //Set up expected
        int[][] expectedSettledBlocks = {
                {0,29},
                {0,30},
                {0,31}};
        //Act
        board.setActivePiece(middlePiece);
        board.nextTurn();

        assertArrayEquals(expectedSettledBlocks, board.getSettledBlocksCoordinates());
    }

    @Test
    public void whenFullRowOccuredInAnyRowBoardInternalMatrixShiftDownward() {
        //Set up piece
        Piece middlePiece = new Piece(new Block[] {
                new Block(0, 27, 0),
                new Block(0, 28, 0),
                new Block(0, 29, 0),
                new Block(0, 30, 0)
        });
        //Set up board state
        int[][] currentBoardState = board.getBoardStates();
        currentBoardState[31] = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
        board.setBoardStates(currentBoardState);
        //Set up settled blocks
        ArrayList<Block> currentSettledBlocks = new ArrayList<>();
        for (int i=1; i<16; i++) {
            currentSettledBlocks.add(new Block(i,31, 0));
        }
        board.setSettledBlocks(currentSettledBlocks);
        //Set up expected
        int[][] expectedBoardState = new int[32][16];
        expectedBoardState[31] = new int[] {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        expectedBoardState[30] = new int[] {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        expectedBoardState[29] = new int[] {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        //Act
        board.setActivePiece(middlePiece);
        board.nextTurn();

        assertArrayEquals(expectedBoardState, board.getBoardStates());
    }

}