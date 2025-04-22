package net.dinglezz.torgrays_trials.main;

import javax.swing.*;

public class Main {
    public static JFrame window;
    public static Game game;

    public static void main(String[] args) {
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
        window.setVisible(true);

        game.setupGame();
        game.startGameThread();
    }
}
