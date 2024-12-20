package connectfour;

import java.awt.*;

public class Cell {
    public static final int SIZE = 120;
    public static final int PADDING = SIZE / 5;
    public static final int SEED_SIZE = SIZE - PADDING * 2;

    Seed content;
    int row, col;
    private int animationY; // Y position for animation

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        content = Seed.NO_SEED;
        animationY = -SEED_SIZE; // Start above the cell
    }

    public void newGame() {
        content = Seed.NO_SEED;
        resetAnimation();
    }

    public void paint(Graphics g) {
        int x1 = col * SIZE + PADDING;
        int y1 = row * SIZE + PADDING;
        if (content == Seed.CROSS || content == Seed.NOUGHT) {
            g.drawImage(content.getImage(), x1, animationY, SEED_SIZE, SEED_SIZE, null);
        }
    }

    public void animate() {
        int targetY = row * SIZE + PADDING;
        if (animationY < targetY) {
            animationY += Math.min(10, targetY - animationY); // Adjust speed as needed
        }
    }

    public void resetAnimation() {
        this.animationY = -SEED_SIZE;
    }
}