package tetrominos;

import java.awt.Color;
import java.awt.Graphics;

class Block {

    private Color color;
    private Position position;

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public static Color[] colors = { Color.red, Color.blue, Color.magenta,
            Color.orange, Color.green, Color.cyan, Color.yellow };

    public Block(int x, int y, int colorIndex) {
        this.color = colors[colorIndex];
        this.position = new Position(x, y);
    }

    public Block(int x, int y, Color color) {
        this.color = color;
        this.position = new Position(x, y);
    }

    public void shift(int dx, int dy) {
        this.position.x += dx;
        this.position.y += dy;
    }

    public void draw(Graphics g, int scale) {
        int py = this.position.y * scale + 1;
        int px = this.position.x * scale + 1;
        int size = scale - 2;
        g.setColor(this.color);
        g.fillRect(px, py, size, size);
    }

}