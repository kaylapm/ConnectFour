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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

public class ConnectFour extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Connect Four";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225);
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel welcomePanel;
    private JPanel playerNamePanel;
    private String player1Name = "Nought";
    private String player2Name = "Cross";
    private boolean isMusicEnabled = true;

    private Timer timer;
    private int elapsedTime;
    private Timer animationTimer;

    private Stack<int[]> moveHistory;

    public ConnectFour() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        initWelcomePanel();
        initPlayerNamePanel();
        JPanel gamePanel = createGamePanel();

        mainPanel.add(welcomePanel, "Welcome");
        mainPanel.add(playerNamePanel, "PlayerNames");
        mainPanel.add(gamePanel, "Game");

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "Welcome");
        soundEffect.initGame();
        if (isMusicEnabled) {
            soundEffect.BACKSOUND.loop();
        }
    }

    private void initWelcomePanel() {
        welcomePanel = new JPanel(new BorderLayout());
        ImageIcon backgroundIcon = new ImageIcon("src/images/background.gif");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new GridBagLayout());

        JLabel welcomeLabel = new JLabel(" ", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 40);
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundLabel.add(welcomeLabel, gbc);

        JButton startButton = new JButton(" ");
        styleButton(startButton);
        startButton.setPreferredSize(new Dimension(180, 100));
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "PlayerNames");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        gbc.gridy = 1;
        gbc.insets = new Insets(600, 50, 10, 50);
        backgroundLabel.add(startButton, gbc);


        JButton musicButton = new JButton(isMusicEnabled ? " " : " ");
        musicButton.addActionListener(e -> {
            isMusicEnabled = !isMusicEnabled;
            musicButton.setText(isMusicEnabled ? " " : " ");
            if (isMusicEnabled) {
                soundEffect.BACKSOUND.loop();
            } else {
                soundEffect.BACKSOUND.stop();
            }
        });
        musicButton.setPreferredSize(new Dimension(100, 200));
        gbc.gridy = 2;
        gbc.insets = new Insets(-100, -850, 10, 10);
        musicButton.setOpaque(false);
        musicButton.setContentAreaFilled(false);
        musicButton.setBorderPainted(false);
        musicButton.setFocusPainted(false);
        backgroundLabel.add(musicButton, gbc);

        welcomePanel.add(backgroundLabel, BorderLayout.CENTER);
    }

    private void initPlayerNamePanel() {
        playerNamePanel = new JPanel(new BorderLayout());
        ImageIcon backgroundIcon = new ImageIcon("src/images/player.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new GridBagLayout());

        JLabel player1Label = new JLabel(" ");
        JTextField player1Field = new JTextField(20);
        player1Field.setPreferredSize(new Dimension(200, 50));
        player1Field.setOpaque(false);
        player1Field.setBorder(BorderFactory.createEmptyBorder());
        player1Field.setFont(new Font("Arial", Font.PLAIN, 18)); // Adjust font size as needed


        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.anchor = GridBagConstraints.WEST;
        gbc1.insets = new Insets(20, 20, 10, 10);
        backgroundLabel.add(player1Label, gbc1);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 1;
        gbc2.gridy = 0;
        gbc2.anchor = GridBagConstraints.CENTER;
        gbc2.insets = new Insets(20, 10, 10, 20);
        backgroundLabel.add(player1Field, gbc2);

        JLabel player2Label = new JLabel(" ");
        JTextField player2Field = new JTextField(20);
        player2Field.setPreferredSize(new Dimension(200, 50));
        player2Field.setOpaque(false);
        player2Field.setBorder(BorderFactory.createEmptyBorder());
        player2Field.setFont(new Font("Arial", Font.PLAIN, 18)); // Adjust font size as needed


        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 0;
        gbc3.gridy = 1;
        gbc3.anchor = GridBagConstraints.WEST;
        gbc3.insets = new Insets(10, 20, 20, 10);
        backgroundLabel.add(player2Label, gbc3);

        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.gridx = 1;
        gbc4.gridy = 1;
        gbc4.anchor = GridBagConstraints.CENTER;
        gbc4.insets = new Insets(10, 10, 20, 20); // Adjust margins as needed
        backgroundLabel.add(player2Field, gbc4);

        JButton proceedButton = new JButton(" ");
        proceedButton.addActionListener(e -> {
            player1Name = player1Field.getText().trim().isEmpty() ? "Nought" : player1Field.getText().trim();
            player2Name = player2Field.getText().trim().isEmpty() ? "Cross" : player2Field.getText().trim();
            cardLayout.show(mainPanel, "Game");
            newGame();
            startTimer();
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        proceedButton.setPreferredSize(new Dimension(180, 100));
        proceedButton.setOpaque(false);
        proceedButton.setContentAreaFilled(false);
        proceedButton.setBorderPainted(false);
        proceedButton.setFocusPainted(false);

        GridBagConstraints gbc5 = new GridBagConstraints();
        gbc5.gridx = 0;
        gbc5.gridy = 2;
        gbc5.gridwidth = 2;
        gbc5.anchor = GridBagConstraints.CENTER;
        gbc5.insets = new Insets(20, 0, 0, 0);
        backgroundLabel.add(proceedButton, gbc5);

        playerNamePanel.add(backgroundLabel, BorderLayout.CENTER);
    }
    private JPanel createGamePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        panel.add(statusBar, BorderLayout.PAGE_END);
        panel.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        panel.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        ImageIcon backgroundIcon = new ImageIcon("src/images/play.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new GridBagLayout());

        JPanel boardPanel = new JPanel();
        boardPanel.setOpaque(false);
        boardPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        board = new Board();
        initGame();
        boardPanel.add(board, gbc);

        backgroundLabel.add(boardPanel, gbc);
        panel.add(backgroundLabel, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                soundEffect.EAT_FOOD.play();
                int horizontalOffset = (panel.getWidth() - Board.CANVAS_WIDTH) / 2;
                int mouseX = e.getX() - horizontalOffset;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (col >= 0 && col < Board.COLS) {
                        for (int rowI = Board.ROWS - 1; rowI >= 0; rowI--) {
                            if (board.cells[rowI][col].content == Seed.NO_SEED) {
                                soundEffect.EAT_FOOD.play();
                                board.cells[rowI][col].content = currentPlayer;
                                moveHistory.push(new int[]{rowI, col});
                                currentState = board.stepGame(currentPlayer, rowI, col);
                                repaint();
                                if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON) {
                                    stopTimer();
                                    String winner = (currentState == State.CROSS_WON) ? player2Name : player1Name;
                                    int response = JOptionPane.showConfirmDialog(
                                            ConnectFour.this,
                                            "Congrats! " + winner + " wins the game in " + elapsedTime + " seconds! Start a new game?",
                                            "WooHHooOOo",
                                            JOptionPane.OK_CANCEL_OPTION,
                                            JOptionPane.INFORMATION_MESSAGE
                                    );
                                    if (response == JOptionPane.OK_OPTION) {
                                        newGame();
                                        startTimer();
                                    }
                                }
                                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                                updateStatusBar();
                                break;
                            }
                        }
                    }
                } else {
                    newGame();
                    startTimer();
                }
                repaint();
            }
        });

        animationTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.repaint();
                for (int row = 0; row < Board.ROWS; ++row) {
                    for (int col = 0; col < Board.COLS; ++col) {
                        board.cells[row][col].animate();
                    }
                }
            }
        });
        animationTimer.start();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Add New Game button
        JButton newGameButton = new JButton("New Game");
        styleButton(newGameButton);
        newGameButton.addActionListener(e -> {
            newGame();
            startTimer();
        });
        buttonPanel.add(newGameButton);


        JButton quitButton = new JButton("Quit Game");
        styleButton(quitButton);
        quitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(quitButton);

        JButton undoButton = new JButton("Undo");
        styleButton(undoButton);
        undoButton.addActionListener(e -> undoLastMove());
        buttonPanel.add(undoButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        return panel;
    }

    private void undoLastMove() {
        if (!moveHistory.isEmpty()) {
            int[] lastMove = moveHistory.pop();
            board.cells[lastMove[0]][lastMove[1]].content = Seed.NO_SEED;
            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
            updateStatusBar();
            repaint();
        }
    }

    private void initGame() {
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
        moveHistory = new Stack<>();
        updateStatusBar();
    }

    private void newGame() {
        board.newGame();
        currentState = State.PLAYING;
        currentPlayer = Seed.CROSS;
        elapsedTime = 0;
        moveHistory.clear();
        updateStatusBar();
    }

    private void updateStatusBar() {
        if (currentState == State.PLAYING) {
            String playerPiece = (currentPlayer == Seed.CROSS) ? "Sun" : "Ball";
            String currentPlayerName = (currentPlayer == Seed.CROSS) ? player2Name : player1Name;
            statusBar.setText(currentPlayerName + "'s Turn (" + playerPiece + ") | Time: " + elapsedTime + "s");
        } else if (currentState == State.DRAW) {
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setText(player2Name + " Won! (Sun) Click to play again.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setText(player1Name + " Won! (Ball) Click to play again.");
        }
    }

    private void startTimer() {
        elapsedTime = 0;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                updateStatusBar();
            }
        });
        timer.start();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);

        board.paint(g);

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(player2Name + " Won! Click to play again.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(player1Name + " Won! Click to play again.");
        }
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
    }

    public static void play() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setContentPane(new ConnectFour());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}