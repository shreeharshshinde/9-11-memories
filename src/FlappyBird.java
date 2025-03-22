import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardwidth = 360;
    int boardheight = 640;

    Image backgroundImg;
    Image topPipeImg;
    Image bottomPipeImg;
    Image birdImg;

    // Pipes
    int pipeX = boardwidth;
    int pipeY = 0;
    int pipewidth = 64; // Scaled by 1/6
    int pipeheight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipewidth;
        int height = pipeheight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;

        }
    }

    // Bird
    int birdX = boardwidth / 8;
    int birdY = boardheight / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if(gameOver) {
            placePipesTimer.stop();
            gameloop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
        }

        if(gameOver) {
            bird.y = birdY;
            velocityY = 0;
            pipes.clear();
            score = 0;
            gameOver = false;
            gameloop.start();
            placePipesTimer.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    // Game Logic
    Bird bird;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;
    int openingspace = boardheight / 4;
    Timer gameloop;
    Timer placePipesTimer;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    boolean gameOver = false;
    double score = 0;


    FlappyBird() {
        setPreferredSize(new Dimension(boardwidth, boardheight));
        setBackground(Color.blue);

        setFocusable(true);
        addKeyListener(this);

        // Load Image
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipesTimer.start();

        gameloop = new Timer(1000/60, this);
        gameloop.start();

    }

    public void placePipes() {
        int randomPipeY =(int)(pipeY - pipeheight / 4 - Math.random() * (pipeheight / 2));
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeheight + openingspace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        System.out.println("Draw");
        g.drawImage(backgroundImg, 0, 0,  boardwidth, boardheight, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        for(int i=0; i<pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver) {
            g.drawString("Game Over: " + String.valueOf((int)score), 10, 35);
        } else {
            g.drawString("Score: " +   String.valueOf((int) score), 10, 35);
        }
    }

    public void move() {
        // Bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        // Pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5; // 0.5 for each pipe as there are two pipes top one and bottom one.
            }

            if(collision(bird, pipe)) {
                gameOver = true;
            }


        }

        if(bird.y > boardheight) {
            gameOver = true;
        }
    }

    public boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
    }
}
