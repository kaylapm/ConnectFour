package connectfour;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

        soundEffect.initGame(); // Pre-load sound files
    }

    private void initWelcomePanel() {
        welcomePanel = new JPanel(new BorderLayout());
        ImageIcon backgroundIcon = new ImageIcon("src/images/welcome.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new GridBagLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Connect Four!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 40);
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundLabel.add(welcomeLabel, gbc);

        JButton startButton = new JButton("Start Game");
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
        gbc.insets = new Insets(500, 150, 10, 50);
        backgroundLabel.add(startButton, gbc);

        JButton musicButton = new JButton(isMusicEnabled ? "Music On" : "Music Off");
        musicButton.addActionListener(e -> {
            isMusicEnabled = !isMusicEnabled;
            musicButton.setText(isMusicEnabled ? "Music On" : "Music Off");
        });
        musicButton.setPreferredSize(new Dimension(100, 200));
        gbc.gridy = 2;
        gbc.insets = new Insets(-250, -350, 10, 10);
        musicButton.setOpaque(false);
        musicButton.setContentAreaFilled(false);
        musicButton.setBorderPainted(false);
        musicButton.setFocusPainted(false);
        backgroundLabel.add(musicButton, gbc);

        welcomePanel.add(backgroundLabel, BorderLayout.CENTER);
    }

    private void initPlayerNamePanel() {
        playerNamePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel player1Label = new JLabel("Player 1 Name:");
        JTextField player1Field = new JTextField(10);
        JLabel player2Label = new JLabel("Player 2 Name:");
        JTextField player2Field = new JTextField(10);

        JButton proceedButton = new JButton("Proceed to Game");
        proceedButton.addActionListener(e -> {
            player1Name = player1Field.getText().trim().isEmpty() ? "Nought" : player1Field.getText().trim();
            player2Name = player2Field.getText().trim().isEmpty() ? "Cross" : player2Field.getText().trim();
            cardLayout.show(mainPanel, "Game");
            newGame();
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        playerNamePanel.add(player1Label, gbc);
        gbc.gridx = 1;
        playerNamePanel.add(player1Field, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        playerNamePanel.add(player2Label, gbc);
        gbc.gridx = 1;
        playerNamePanel.add(player2Field, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        playerNamePanel.add(proceedButton, gbc);
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

        board = new Board();
        initGame();

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (col >= 0 && col < Board.COLS) {
                        for (int rowI = Board.ROWS - 1; rowI >= 0; rowI--) {
                            if (board.cells[rowI][col].content == Seed.NO_SEED) {
                                currentState = board.stepGame(currentPlayer, rowI, col);
                                repaint();
                                if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON) {
                                    String winner = (currentState == State.CROSS_WON) ? player2Name : player1Name;
                                    int response = JOptionPane.showConfirmDialog(
                                            ConnectFour.this,
                                            "Congrats! " + winner + " wins the game! Start a new game?",
                                            "wohoo",
                                            JOptionPane.OK_CANCEL_OPTION,
                                            JOptionPane.INFORMATION_MESSAGE
                                    );
                                    if (response == JOptionPane.OK_OPTION) {
                                        newGame();
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
                }
                repaint();
            }
        });

        panel.add(board, BorderLayout.CENTER); // Add the board to the panel
        return panel;
    }

    private void initGame() {
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
        updateStatusBar();
    }

    private void newGame() {
        board.newGame();
        currentState = State.PLAYING;
        currentPlayer = Seed.CROSS;
        updateStatusBar();
    }

    private void updateStatusBar() {
        if (currentState == State.PLAYING) {
            statusBar.setText((currentPlayer == Seed.CROSS) ? player2Name + "'s Turn" : player1Name + "'s Turn");
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