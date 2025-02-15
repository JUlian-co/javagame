import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class ChromeDinosaur extends JPanel implements ActionListener, KeyListener {
    // Spieleinstellungen
    private final int BOARD_WIDTH = 750;
    private final int BOARD_HEIGHT = 250;
    private final int DINO_X = 50;
    private final int JUMP_SPEED = -17;
    private final int GAME_SPEED = -12;
    
    // Spielobjekte
    private Dinosaur dino;
    private ArrayList<Cactus> cacti;
    
    // spiel ereignisse
    private boolean gameOver;
    private int score;
    private Timer gameLoop;
    private Timer cactusSpawner;
    
    // Innere Klasse für game assetss
    private class GameObject {
        int x, y, width, height;
        Image image;
        
        GameObject(int x, int y, int width, int height, String imagePath) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = new ImageIcon(getClass().getResource(imagePath)).getImage();
        }
        
        boolean collidesWith(GameObject other) {
            return x < other.x + other.width &&
                   x + width > other.x &&
                   y < other.y + other.height &&
                   y + height > other.y;
        }
    }
    
    // Dinosaurier Klasse
    private class Dinosaur extends GameObject {
        int velocityY;
        Image runImage, jumpImage, deadImage;
        
        Dinosaur() {
            super(DINO_X, BOARD_HEIGHT - 94, 88, 94, "./img/dino-run.gif");
            runImage = image;
            jumpImage = new ImageIcon(getClass().getResource("./img/dino-jump.png")).getImage();
            deadImage = new ImageIcon(getClass().getResource("./img/dino-dead.png")).getImage();
        }
        
        void jump() {
            if (y == BOARD_HEIGHT - height) {
                velocityY = JUMP_SPEED;
                image = jumpImage;
            }
        }
        
        void update() {
            velocityY += 1; // Gravity
            y += velocityY;
            
            if (y > BOARD_HEIGHT - height) {
                y = BOARD_HEIGHT - height;
                velocityY = 0;
                image = runImage;
            }
        }
    }
    
    // Kaktus  Klasse
    private class Cactus extends GameObject {
        Cactus(int type) {
            super(BOARD_WIDTH, BOARD_HEIGHT - 70, 
                  type == 1 ? 34 : type == 2 ? 69 : 102, 
                  70, 
                  "./img/cactus" + type + ".png");
        }
        
        void move() {
            x += GAME_SPEED;
        }
    }
    
    public ChromeDinosaur() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.lightGray);
        setFocusable(true);
        addKeyListener(this);
        
        initGame();
    }
    
    private void initGame() {
        dino = new Dinosaur();
        cacti = new ArrayList<>();
        gameOver = false;
        score = 0;
        
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
        
        cactusSpawner = new Timer(1500, e -> spawnCactus());
        cactusSpawner.start();
    }
    
    private void spawnCactus() {
        if (!gameOver) {
            double chance = Math.random();
            if (chance > 0.5) {
                cacti.add(new Cactus((int)(Math.random() * 3) + 1));
            }
            if (cacti.size() > 10) {
                cacti.remove(0);
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Dinosaurier
        g.drawImage(dino.image, dino.x, dino.y, dino.width, dino.height, null);
        
        // Kaktus
        for (Cactus cactus : cacti) {
            g.drawImage(cactus.image, cactus.x, cactus.y, cactus.width, cactus.height, null);
        }
        
        // Score
        g.setFont(new Font("Courier", Font.PLAIN, 32));
        g.drawString(gameOver ? "Game Over: " + score : String.valueOf(score), 10, 35);
    }
    
    private void updateGame() {
        if (!gameOver) {
            dino.update();
            
            for (Cactus cactus : cacti) {
                cactus.move();
                if (cactus.collidesWith(dino)) {
                    gameOver = true;
                    dino.image = dino.deadImage;
                    stopGame();
                }
            }
            
            score++;
        }
    }
    
    private void stopGame() {
        gameLoop.stop();
        cactusSpawner.stop();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gameOver) {
                initGame();
            } else {
                dino.jump();
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}
}