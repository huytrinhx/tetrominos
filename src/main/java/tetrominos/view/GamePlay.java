package tetrominos.view;

import javax.swing.JFrame;

import tetrominos.models.Board;
import tetrominos.controller.GameController;
import tetrominos.models.Block;

import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;

public class GamePlay extends JFrame {
    private JComponent component;
    private static final int SCALE = 32; // number of pixels per square
    private Board board;

    public Board getBoard() {
        return board;
    }

    public GamePlay(GameController controller) {
        super("Playing...");
        this.board = controller.getBoard();
        int width = board.getWidth();
        int height = board.getHeight();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.component = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
                board.getActivePiece().draw(g, SCALE);
                for (Block b : board.getSettledBlocks()) {
                    b.draw(g, SCALE);
                }
            }
        };
        component.setPreferredSize(new Dimension(width * SCALE, height * SCALE));
        setContentPane(component);
        setLocationRelativeTo(null); // Center the frame
        setLocation(getX() - (width * SCALE) / 2, getY() - (height * SCALE) / 2);
        pack();
        setResizable(true);
        setVisible(true);
    }

    public void display() {
        pack();
        setResizable(false);
        setVisible(true);
    }

    public void listen() {
        Timer timer = new Timer(300, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.nextTurn();
                repaint();
            }

        });

        addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                // System.out.println("key pressed " + e.getKeyCode());
                if (key == KeyEvent.VK_A) {
                    board.getActivePiece().rotateLeft();
                } else if (key == KeyEvent.VK_D) {
                    board.getActivePiece().rotateRight();
                } else if (key == KeyEvent.VK_LEFT) {
                    board.getActivePiece().slide(-1);
                } else if (key == KeyEvent.VK_RIGHT) {
                    board.getActivePiece().slide(1);
                }
                board.alignActivePiece();
                repaint();
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        timer.start();
    }

}
