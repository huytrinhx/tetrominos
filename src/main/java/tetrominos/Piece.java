package tetrominos;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.*;

public class Piece {
    private Color color;
    private Block[] blocks;
    public int minX;
    public int minY;
    public int maxY;
    public int maxX;

    public Color getColor() {
        return color;
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public Piece() {
        // constructor for piece class will first generate a color
        // then create blocks with chosen color
        setPieceColor();
        setPieceBlocks();
        retrieveBoundary();

    }

    public Piece(Block[] blocks) {
        this.blocks = blocks;
        retrieveBoundary();
        
    }

    public void draw(Graphics g, int scale) {
        for (Block b : this.blocks) {
            b.draw(g, scale);
        }
    }

    public void shift(int dx, int dy) {
        for (Block b : this.blocks) {
            b.shift(dx, dy);
        }
        retrieveBoundary();
    }

    public void setPieceColor() {
        Random random = new Random();
        int colorIndex = random.nextInt(Block.colors.length);
        this.color = Block.colors[colorIndex];
    }

    public void setPieceBlocks() {
        // Each piece has 4 blocks
        // Each block must be orthogonal to each other
        // Meaning it must be in direct left, right, up or down to the adjacent block
        int[][] shape = new int[4][4]; // 4x4 matrix to assist the random generation of blocks
        Block[] blocks = new Block[4];
        int[][] directions = { { 0, 1 }, { 1, 0 }, { -1, 0 }, { 0, -1 } };
        Random random = new Random();
        for (int block = 0; block < 4; block++) {
            // If it's first block we can pick any cell in a 4 x 4 matrix
            if (block == 0) {
                int x = random.nextInt(4);
                int y = random.nextInt(4);
                shape[y][x] = 1;
                blocks[block] = new Block(x, y, this.color);
                continue;
            }
            // Else we randomly pick a direction for our next block orthogonal to the last
            Block lastBlock = blocks[block - 1];
            int lastX = lastBlock.getPosition().x;
            int lastY = lastBlock.getPosition().y;
            boolean isNewBlockGenerated = false;

            while (!isNewBlockGenerated) {
                int[] direction = directions[random.nextInt(4)];
                int x = lastX + direction[0];
                int y = lastY + direction[1];
                if (isLegal(x, y, shape)) {
                    shape[y][x] = 1;
                    blocks[block] = new Block(x, y, this.color);
                    break;
                }
            }
        }
        this.blocks = blocks;
    }

    private boolean isLegal(int newColumn, int newRow, int[][] shape) {
        // it will be illegal if the potential new block is out of our 4 x 4 or has been
        // already occupied
        if (newRow < 0 || newRow > 3 || newColumn < 0 || newColumn > 3 || shape[newRow][newColumn] == 1) {
            return false;
        }
        return true;
    }

    public void retrieveBoundary() {
        // Find minX,maxX, minY, maxY of the current piece
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (Block b : this.blocks) {
            System.out.format("(%d,%d)\t", b.getPosition().x, b.getPosition().y);
            minX = Math.min(minX, b.getPosition().x);
            minY = Math.min(minY, b.getPosition().y);
            maxX = Math.max(maxX, b.getPosition().x);
            maxY = Math.max(maxY, b.getPosition().y);
        }
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        System.out.format("MinX: %s, MinY: %s, MaxX: %s, MaxY: %s\n", minX, minY, maxX, maxY);
    }

    public int[][] getCurrentCordinates() {
        int[][] output = new int[4][2];
        for (int i = 0; i < 4; i++) {
            output[i][0]= blocks[i].getPosition().x;
            output[i][1]= blocks[i].getPosition().y
;        }
        return output;
    }

    public void rotateLeft() {
        System.out.println("Rotate left. 90 degree counter-clockwise.");
        rotate(-Math.PI/2);
    }

    public void rotateRight() {
        System.out.println("\"Rotate right. 90 degree clockwise.\"");
        rotate(Math.PI/2);
    }

    private void rotate(double theta) {
        //Since we're using a non-standard left-handed Cartesian coordinate system
        //meaning x is directed to the right, but y is directed down
        //Positive theta means clockwise rotation
        //And negative theta means counter-clockwise rotation
        double[][] rotationMatrix = {{Math.cos(theta), -Math.sin(theta)}, {Math.sin(theta), Math.cos(theta)}};
        int[][] translated = new int[4][2];
        int[][] rotated = new int[4][2];
        Block[] newPiece = new Block[4];

        // Offset the coordinates of all blocks by min X and min Y
        // This will effectively set the top left block to (0, 0) position - our origin
        for (int i=0; i< blocks.length; i++) {
            translated[i] = new int[] { blocks[i].getPosition().x - minX,
                    (blocks[i].getPosition().y - minY) };
        }
        // Apply rotation
        for (int i=0; i< translated.length; i++) {
            rotated[i][0] = (int)Math.round(translated[i][0] * rotationMatrix[0][0] + translated[i][1] * rotationMatrix[0][1]);
            rotated[i][1] = (int)Math.round(translated[i][0] * rotationMatrix[1][0] + translated[i][1] * rotationMatrix[1][1]);

        }
        // Generate new blocks
        for (int i = 0; i < 4; i++) {
            newPiece[i] = new Block(rotated[i][0] + minX,
                    rotated[i][1] + minY,
                    getColor());
        }

        this.blocks = newPiece;
    }

    public void slide(int dx) {
        System.out.format("Slide %s.", dx);
        // move the active piece one square in the direction dx
        // (which has a value -1 or 1):
        shift(dx, 0);
    }
}
