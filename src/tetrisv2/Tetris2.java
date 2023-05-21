package tetrisv2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tetris2 extends JPanel {

    private final int[][] board;
    private final Color[] tetrominoColors;
    private final int[][] tetrominoes;
    private int currentTetromino;
    private int currentX;
    private int currentY;

    public Tetris2() {
        board = new int[20][10];
        tetrominoColors = new Color[]{
                Color.BLACK, Color.CYAN, Color.YELLOW, Color.ORANGE,
                Color.BLUE, Color.MAGENTA, Color.GREEN, Color.RED
        };
        tetrominoes = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // No Tetromino
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1}, // I Tetromino
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0}, // O Tetromino
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0}, // T Tetromino
                {0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // S Tetromino
                {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0}, // Z Tetromino
                {0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // J Tetromino
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0}  // L Tetromino
        };
        currentTetromino = (int) (Math.random() * tetrominoes.length);
        currentX = 3;
        currentY = 0;

        setFocusable(true);
        addKeyListener(new TetrisKeyListener());
    }

    private boolean canMove(int deltaX, int deltaY) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tetrominoes[currentTetromino][i * 4 + j] != 0) {
                    int newX = currentX + j + deltaX;
                    int newY = currentY + i + deltaY;
                    if (newX < 0 || newX >= 10 || newY >= 20 || board[newY][newX] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void rotate() {
        int[][] rotatedTetromino = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                rotatedTetromino[i][j] = tetrominoes[currentTetromino][j * 4 + 3 - i];
            }
        }

        if (canMove(0, 0) && canMove(1, 0) && canMove(-1, 0) && canMove(0, -1)) {
            tetrominoes[currentTetromino] = rotatedTetromino;
            repaint();
        }
    }

    private void placeTetromino() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tetrominoes[currentTetromino][i * 4 + j] != 0) {
                    board[currentY + i][currentX + j] = currentTetromino;
                }
            }
        }
        currentTetromino = (int) (Math.random() * tetrominoes.length);
        currentX = 3;
        currentY = 0;
    }

    private boolean gameOver() {
        return !canMove(0, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 300, 500);

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                int tetromino = board[i][j];
                if (tetromino != 0) {
                    g.setColor(tetrominoColors[tetromino]);
                    g.fillRect(j * 25, i * 25, 25, 25);
                }
            }
        }

        int[] tetromino = tetrominoes[currentTetromino];
        g.setColor(tetrominoColors[currentTetromino]);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tetromino[i * 4 + j] != 0) {
                    g.fillRect((currentX + j) * 25, (currentY + i) * 25, 25, 25);
                }
            }
        }
    }

    private class TetrisKeyListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (canMove(-1, 0)) {
                    currentX--;
                    repaint();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (canMove(1, 0)) {
                    currentX++;
                    repaint();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (canMove(0, 1)) {
                    currentY++;
                    repaint();
                } else {
                    placeTetromino();
                    if (gameOver()) {
                        // Game over logic here
                    }
                }
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                rotate();
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 500);
        frame.setLocationRelativeTo(null);

        Tetris tetris = new Tetris();
        frame.add(tetris);

        frame.setVisible(true);
    }
}
