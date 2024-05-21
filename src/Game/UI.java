package Game;

import java.awt.*;
import javax.swing.*;
public class UI {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    int width=800, height=600;
    public UI(){
        createUI();
    }
    public void createUI(){
        frame = new JFrame("Palworld Game");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createMainMenuPanel(), "MainMenu");
        mainPanel.add(createNewGamePanel(), "NewGame");
        mainPanel.add(createHomebasePanel(), "Homebase");
        mainPanel.add(createDungeonPanel(), "Dungeon");

        frame.add(mainPanel);
        frame.setVisible(true);
    }
    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null); // Use null layout for absolute positioning

        JLabel welcomeLabel = new JLabel("Selamat datang di Palworld!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Sorts Mill Goudy", Font.BOLD, 32));
        welcomeLabel.setBounds(100, 50, 440, 50);
        panel.add(welcomeLabel);

        JButton newGameButton = new JButton("Start Game");
        newGameButton.setBounds(270, 150, 100, 50);
        newGameButton.addActionListener(e -> cardLayout.show(mainPanel, "NewGame"));
        panel.add(newGameButton);

        JButton loadGameButton = new JButton("Load Game");
        loadGameButton.setBounds(270, 220, 100, 50);
        loadGameButton.addActionListener(e -> {
            // Load game logic here
        });
        panel.add(loadGameButton);

        JButton exitButton = new JButton("Exit (Auto Save)");
        exitButton.setBounds(270, 290, 100, 50);
        exitButton.addActionListener(e -> System.exit(0));
        panel.add(exitButton);

        return panel;
    }

    private JPanel createNewGamePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel("Masukkan nama player: ", JLabel.CENTER);
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200,35));
        

        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        namePanel.add(nameField);

        JButton startButton = new JButton("Mulai");
        startButton.addActionListener(e -> {

            String playerName = nameField.getText();
            if (!playerName.isEmpty()) {
                player.setName(playerName);
                cardLayout.show(mainPanel, "Homebase");
            } else {
                JOptionPane.showMessageDialog(frame, "Nama tidak boleh kosong!");
            }

        });

        panel.add(namePanel, BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createHomebasePanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1));
        JLabel homebaseLabel = new JLabel("Selamat datang di Homebase!", JLabel.CENTER);
        panel.add(homebaseLabel);

        JButton healButton = new JButton("Heal Monster");
        healButton.addActionListener(e -> {
            // Heal monster logic here
        });
        panel.add(healButton);

        JButton evolveButton = new JButton("Evolve Monster");
        evolveButton.addActionListener(e -> {
            // Evolve monster logic here
        });
        panel.add(evolveButton);

        JButton buyItemButton = new JButton("Beli Item");
        buyItemButton.addActionListener(e -> {
            // Buy item logic here
        });
        panel.add(buyItemButton);

        JButton dungeonButton = new JButton("Keluar Homebase");
        dungeonButton.addActionListener(e -> cardLayout.show(mainPanel, "Dungeon"));
        panel.add(dungeonButton);

        return panel;
    }

    private JPanel createDungeonPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel dungeonLabel = new JLabel("Selamat datang di Dungeon!", JLabel.CENTER);
        panel.add(dungeonLabel, BorderLayout.NORTH);

        JTextArea dungeonInfo = new JTextArea();
        dungeonInfo.setEditable(false);
        panel.add(new JScrollPane(dungeonInfo), BorderLayout.CENTER);

        JButton backButton = new JButton("Kembali ke Homebase");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Homebase"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }


}
