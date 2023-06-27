package tetris1_2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Tetris extends JFrame {
    private JLabel statusbar;

    JLabel getStatusBar() {

        return statusbar;
    }

    public Tetris() {

        initUI();
    }

    // icon
    ImageIcon img = new ImageIcon("/Dev/tetris-android-project/icon.png");

    private void initUI() {

        statusbar = new JLabel(" 0");
        add(statusbar, BorderLayout.SOUTH);

        // statusbar color
        this.setBackground(Color.black);
        statusbar.setBackground(Color.black);
        statusbar.setForeground(Color.white);
        statusbar.setOpaque(true);

        // board add
        var board = new board(this);
        add(board);
        board.start();

        // icon set
        setIconImage(img.getImage());

        // background color

        // title, size set
        setTitle("Tetris");
        setSize(260, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // main
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var game = new Tetris();
            game.setVisible(true);

        });
    }
}