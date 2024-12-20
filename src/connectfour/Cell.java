/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #5
 * 1 - 5026231158 - Kayla Putri Maharani
 * 2 - 5026231170 - Tahiyyah Mufhimah
 * 3 - 5026231206 - Rafael Dimas K
 */

package connectfour;

import java.awt.*;

public class Cell {
    public static final int SIZE = 120;
    public static final int PADDING = SIZE / 5;
    public static final int SEED_SIZE = SIZE - PADDING * 2;

    Seed content;
    int row, col;
    private int animationY;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        content = Seed.NO_SEED;
        animationY = -SEED_SIZE;
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
            animationY += Math.min(10, targetY - animationY);
        }
    }

    public void resetAnimation() {
        this.animationY = -SEED_SIZE;
    }
}