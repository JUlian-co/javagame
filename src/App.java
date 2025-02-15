import javax.swing.*;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Chrome Dino");
        frame.setSize(750, 250);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ChromeDinosaur game = new ChromeDinosaur();
        frame.add(game);
        frame.pack();
        game.requestFocus();
        frame.setVisible(true);
    }
}