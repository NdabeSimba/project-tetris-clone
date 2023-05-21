package tetrisv2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tetris extends JPanel {

    private final int[][] tetrominoes = {
            { 1, 1, 1, 1 }, // I-shape
            { 1, 1, 1, 0, 1 }, // L-shape
            { 1, 1, 1, 0, 0, 0, 1 }, // J-shape
            { 1, 1, 0, 0, 1, 1 }, // Z-shape
            { 1, 1, 0, 0, 0, 1, 1 }, // S-shape
            { 1, 1, 1, 0, 1, 0, 0 }, // T-shape
            { 1, 1, 1, 0, 0, 1, 0 } // Square shape
    };

    private final Color[] tetrominoColors = {
            Color.cyan, Color.orange, Color.blue, Color.red, Color.green, Color.magenta, Color.yellow
    };

    private int[][] board;
    private int currentTetromino;
    private int currentX;
    private int currentY;

    public Tetris() {
        board = new int[20][10];
        currentTetromino = (int) (Math.random() * tetrominoes.length);
        currentX = 4;
        currentY = 0;

        setFocusable(true);
        addKeyListener(new TetrisKeyListener());
    }

    private void moveTetromino(int deltaX, int deltaY) {
        if (canMove(deltaX, deltaY)) {
            currentX += deltaX;
            currentY += deltaY;
            repaint();
        } else if (deltaY > 0) {
            placeTetromino();
        }
    }

    private boolean canMove(int deltaX, int deltaY) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tetrominoes[currentTetromino][i * 4 + j] == 1) {
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

        if (canRotate(rotatedTetromino)) {
            // Copy rotatedTetromino into tetrominoes[currentTetromino]
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    tetrominoes[currentTetromino][i * 4 + j] = rotatedTetromino[i][j];
                }
            }
            repaint();
        }
    }

    private boolean canRotate(int[][] rotatedTetromino) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (rotatedTetromino[i][j] == 1) {
                    int newX = currentX + j;
                    int newY = currentY + i;

                    if (newX < 0 || newX >= 10 || newY >= 20 || board[newY][newX] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void placeTetromino() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tetrominoes[currentTetromino][i * 4 + j] == 1) {
                    board[currentY + i][currentX + j] = currentTetromino + 1;
                }
            }
        }
        clearLines();
        currentTetromino = (int) (Math.random() * tetrominoes.length);
        currentX = 4;
        currentY = 0;
    }

    private void clearLines() {
        for (int i = 19; i >= 0; i--) {
            boolean lineIsFull = true;
            for (int j = 0; j < 10; j++) {
                if (board[i][j] == 0) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                for (int k = i; k > 0; k--) {
                    for (int j = 0; j < 10; j++) {
                        board[k][j] = board[k - 1][j];
                    }
                }
                for (int j = 0; j < 10; j++) {
                    board[0][j] = 0;
                }
                i++;
            }
        }
    }

    private boolean gameOver() {
        for (int i = 0; i < 10; i++) {
            if (board[0][i] != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.translate(20, 20);

        // Draw the board
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] != 0) {
                    g.setColor(tetrominoColors[board[i][j] - 1]);
                    g.fillRect(j * 25, i * 25, 25, 25);
                }
            }
        }

        // Draw the current tetromino
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tetrominoes[currentTetromino][i * 4 + j] == 1) {
                    g.setColor(tetrominoColors[currentTetromino]);
                    g.fillRect((currentX + j) * 25, (currentY + i) * 25, 25, 25);
                }
            }
        }
    }

    private class TetrisKeyListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                moveTetromino(-1, 0);
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                moveTetromino(1, 0);
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                moveTetromino(0, 1);
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
        Tetris tetris = new Tetris();
        frame.add(tetris);
        frame.setSize(300, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        while (!tetris.gameOver()) {
            tetris.moveTetromino(0, 1);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
