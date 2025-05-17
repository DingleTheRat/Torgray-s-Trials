package net.dinglezz.torgrays_trials.main;

import javax.swing.*;
import java.io.*;

public class Main {
    public static JFrame window;
    public static Game game;

    public static void main(String[] args) throws IOException, InterruptedException {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Torgray's Trials");

        game = new Game();
        window.add(game);

        game.config.loadConfig();
        if (game.fullScreen) {
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            window.setUndecorated(true);
        }
        window.pack();

        window.setLocationRelativeTo(null);

        game.setupGame();
        window.setVisible(true);
        game.startGameThread();
    }
}
