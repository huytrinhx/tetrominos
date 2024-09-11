package tetrominos.models;

import java.util.*;
import java.util.stream.Collectors;

public class Board {
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private Piece activePiece;

    public Piece getActivePiece() {
        return activePiece;
    }

    public void setActivePiece(Piece activePiece) {
        this.activePiece = activePiece;
    }

    private int[][] boardStates;

    public int[][] getBoardStates() {
        printBoardStates();
        return boardStates;
    }

    public void setBoardStates(int[][] boardStates) {
        this.boardStates = boardStates;
    }

    private ArrayList<Block> settledBlocks;

    public ArrayList<Block> getSettledBlocks() {
        return settledBlocks;
    }

    public int[][] getSettledBlocksCoordinates() {
        int[][] output = new int[settledBlocks.size()][];
        for (int i = 0; i < settledBlocks.size(); i++) {
            output[i] = new int[] { settledBlocks.get(i).getPosition().x, settledBlocks.get(i).getPosition().y };
        }
        return output;
    }

    public void setSettledBlocks(ArrayList<Block> settledBlocks) {
        this.settledBlocks = settledBlocks;
    }

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        initializeBoardStates();
        this.activePiece = new Piece();
        this.settledBlocks = new ArrayList<Block>();
    }

    private void initializeBoardStates() {
        this.boardStates = new int[height][width];
    }

    public void nextTurn() {
        // Move the active piece down one step. Check for collisions.
        // Check for complete rows that can be destroyed.
        System.out.println("Next turn.");
        this.activePiece.shift(0, 1);
        if (isCollide()) {
            this.settledBlocks.addAll(Arrays.asList(this.activePiece.getBlocks()));
            updateBoardStatesOnCollision();
            clearFullRows(getFullRow());
            updateBoardStatesOnClearingRows();
            this.activePiece = new Piece();
        }
    }

    public void alignActivePiece() {
        this.activePiece.retrieveBoundary();
        alignLeft();
        alignRight();
        alignBottom();
        alignTop();
        this.activePiece.retrieveBoundary();
    }

    private void alignLeft() {
        while (this.activePiece.minX < 0) {
            this.activePiece.shift(1, 0);
        }
    }

    private void alignRight() {
        while (this.activePiece.maxX > this.width - 1) {
            this.activePiece.shift(-1, 0);
        }
    }

    private void alignTop() {
        while (this.activePiece.minY < 0) {
            this.activePiece.shift(0, 1);
        }
    }

    private void alignBottom() {
    }

    private boolean isCollide() {
        for (Block b : this.activePiece.getBlocks()) {
            // Collision is when a block row reach the max row
            // Or the next row is already occupied (value = 1)
            int currentRow = b.getPosition().y;
            int currentCol = b.getPosition().x;
            if (currentRow == this.height - 1 || this.boardStates[currentRow + 1][currentCol] == 1) {
                System.out.println("Collision detected!");
                return true;
            }
        }
        return false;
    }

    private List<Integer> getFullRow() {
        List<Integer> fullRowIndexes = new ArrayList<>();
        for (int i = this.height - 1; i > 0; i--) {
            int currentSum = Arrays.stream(this.boardStates[i]).sum();
            System.out.format("Row %d. Sum: %d \n", i, currentSum);
            if (currentSum == this.width) {
                fullRowIndexes.add(i);
            } else if (currentSum == 0) {
                break;
            }
        }
        System.out.println(fullRowIndexes);
        return fullRowIndexes;
    }

    private void clearFullRows(List<Integer> rowIndexes) {
        // We traverse from lower index to higher index
        // Since getFullRow traverse from bottom rows (highest y) to the top (lower y)
        // We need to reverse the index
        Collections.reverse(rowIndexes);
        for (int rowToRemove : rowIndexes) {
            this.settledBlocks.removeIf(block -> (block.getPosition().y == rowToRemove));
            // Shift down y coordinates of all blocks that have y < rowToRemove
            for (Block b : this.settledBlocks.stream().filter(b -> b.getPosition().y < rowToRemove)
                    .collect(Collectors.toList())) {
                b.shift(0, 1);
            }
        }

    }

    private void updateBoardStatesOnCollision() {
        for (Block b : this.activePiece.getBlocks()) {
            this.boardStates[b.getPosition().y][b.getPosition().x] = 1;
        }
    }

    private void updateBoardStatesOnClearingRows() {
        initializeBoardStates();
        for (Block b : this.settledBlocks) {
            this.boardStates[b.getPosition().y][b.getPosition().x] = 1;
        }
    }

    private void printBoardStates() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                System.out.format("%d,", this.boardStates[i][j]);
            }
            System.out.println("\n");
        }
    }

}