package connectfour;

import javax.swing.*;
import java.awt.*;

/**
 * The Board class models the ROWS-by-COLS game board.
 */
public class Board extends JPanel {
    // Define named constants
    public static final int ROWS = 6;  // ROWS x COLS cells
    public static final int COLS = 7;
    // Define named constants for drawing
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;  // the drawing canvas
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
    public static final int GRID_WIDTH = 8;  // Grid-line's width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Grid-line's half-width
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;  // grid lines
    public static final int Y_OFFSET = 1;  // Fine tune for better display

    // Define properties (package-visible)
    /** Composes of 2D array of ROWS-by-COLS Cell instances */
    Cell[][] cells;

    /** Constructor to initialize the game board */
    public Board() {
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        initGame();
    }

    /** Initialize the game objects (run once) */
    public void initGame() {
        cells = new Cell[ROWS][COLS]; // allocate the array
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                // Allocate element of the array
                cells[row][col] = new Cell(row, col);
                // Cells are initialized in the constructor
            }
        }
    }

    /** Reset the game board, ready for new game */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame(); // clear the cell content
            }
        }
    }

    /**
     *  The given player makes a move on (selectedRow, selectedCol).
     *  Update cells[selectedRow][selectedCol]. Compute and return the
     *  new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */
    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        // Update game board
        cells[selectedRow][selectedCol].content = player;
        cells[selectedRow][selectedCol].resetAnimation(); // Use the new method

        // Check for a win condition
        if (checkWin(player, selectedRow, selectedCol)) {
            soundEffect.WIN.play();
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        } else {
            // Check for DRAW (all cells occupied) or PLAYING.
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    if (cells[row][col].content == Seed.NO_SEED) {
                        return State.PLAYING; // still have empty cells
                    }
                }
            }
            return State.DRAW; // no empty cell, it's a draw
        }
    }

    private boolean checkWin(Seed player, int row, int col) {
        return checkDirection(player, row, col, 1, 0) // Horizontal
                || checkDirection(player, row, col, 0, 1) // Vertical
                || checkDirection(player, row, col, 1, 1) // Diagonal \
                || checkDirection(player, row, col, 1, -1); // Diagonal /
    }

    private boolean checkDirection(Seed player, int row, int col, int deltaRow, int deltaCol) {
        int count = 0;
        // Check in the positive direction
        for (int i = 0; i < 4; i++) {
            int r = row + i * deltaRow;
            int c = col + i * deltaCol;
            if (r >= 0 && r < ROWS && c >= 0 && c < COLS && cells[r][c].content == player) {
                count++;
            } else {
                break;
            }
        }
        // Check in the negative direction
        for (int i = 1; i < 4; i++) {
            int r = row - i * deltaRow;
            int c = col - i * deltaCol;
            if (r >= 0 && r < ROWS && c >= 0 && c < COLS && cells[r][c].content == player) {
                count++;
            } else {
                break;
            }
        }
        return count >= 4;
    }

    /** Paint itself on the graphics canvas, given the Graphics context */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the grid-lines
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH - 1, GRID_WIDTH,
                    GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, 0 + Y_OFFSET,
                    GRID_WIDTH, CANVAS_HEIGHT - 1,
                    GRID_WIDTH, GRID_WIDTH);
        }

        // Draw all the cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);  // ask the cell to paint itself
            }
        }
    }
}