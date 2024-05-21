package Game;

import Monsters.Monster;
import Monsters.MonsterAir;
import Monsters.MonsterAngin;
import Monsters.MonsterApi;
import Monsters.MonsterEs;
import Monsters.MonsterTanah;
import java.awt.*;
import java.io.File;
import javax.swing.*;
public class UI {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Player player = new Player();
    int width=600    , height=400;  
    public UI(){
        createUI();
    }

    public void createUI(){
        
        frame = new JFrame("Palworld Game");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createMainMenuPanel(), "MainMenu");
        mainPanel.add(createNewGamePanel(), "NewGame");
        
        mainPanel.add(createChooseMonsterPanel(), "ChooseMonster");
        mainPanel.add(createDungeonPanel(), "Dungeon");

        frame.add(mainPanel);
        frame.setVisible(true);
    }
    private void chooseMonster(int choice) {
        switch (choice) {
            case 1 -> player.monsters.add(new MonsterApi("Flameling", 1, 100, 0, Monster.elements.API));
            case 2 -> player.monsters.add(new MonsterAngin("Zephyrkin", 1, 100, 0, Monster.elements.ANGIN));
            case 3 -> player.monsters.add(new MonsterAir("Aquabot", 1, 100, 0, Monster.elements.AIR));
            case 4 -> player.monsters.add(new MonsterEs("Frostlet", 1, 100, 0, Monster.elements.ES));
            case 5 -> player.monsters.add(new MonsterTanah("Terrapup", 1, 100, 0, Monster.elements.TANAH));
            default -> JOptionPane.showMessageDialog(frame, "Invalid selection!");
        }
        JOptionPane.showMessageDialog(frame, "Selected: " + player.monsters.get(0).getName());
        cardLayout.show(mainPanel, "Homebase");
        // Proceed to game loop or further game logic
    }
    private JPanel createChooseMonsterPanel(){
        
        JPanel panel = new JPanel(new GridLayout(6, 1));
        JLabel instructions = new JLabel("Select your first monster:", JLabel.CENTER);
        panel.add(instructions);

        String[] monsters = {"Flameling (API)", "Zephyrkin (ANGIN)", "Aquabot (AIR)", "Frostlet (ES)", "Terrapup (TANAH)"};
        for (int i = 0; i < monsters.length; i++) {
            JButton monsterButton = new JButton(monsters[i]);
            int finalI = i;
            monsterButton.addActionListener(e -> chooseMonster(finalI + 1));
            panel.add(monsterButton);
        }


        return panel;
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
            // String[] saves = getSaveFiles();
            // if (saves.length > 0) {
            //     String fileName = (String) JOptionPane.showInputDialog(frame, "Pilih file save:", "Load Game",
            //             JOptionPane.PLAIN_MESSAGE, null, saves, saves[0]);
            //     if (fileName != null) {
            //         try {
            //             currentGameState = SaveAndLoad.loadGame(fileName);
            //             cardLayout.show(mainPanel, "Homebase");
            //         } catch (IOException | ClassNotFoundException ex) {
            //             ex.printStackTrace();
            //             JOptionPane.showMessageDialog(frame, "Failed to load game.", "Error", JOptionPane.ERROR_MESSAGE);
            //         }
            //     }
            // } else {
            //     JOptionPane.showMessageDialog(frame, "No save files found.", "Load Game", JOptionPane.INFORMATION_MESSAGE);
            // }
        });
        panel.add(loadGameButton);

        JButton exitButton = new JButton("Exit (Auto Save)");
        exitButton.setBounds(270, 290, 100, 50);
        exitButton.addActionListener(e -> {
            // if (currentGameState != null) {
            //     try {
            //         SaveAndLoad.saveGame(currentGameState, currentGameState.getPlayerName());
            //     } catch (IOException ex) {
            //         ex.printStackTrace();
            //     }
            // }
            System.exit(0);
        });
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
                mainPanel.add(createHomebasePanel(), "Homebase");
                // System.out.println("Selamat datang " + player.getName()); masih sana sana a
            } else {
                JOptionPane.showMessageDialog(frame, "Nama tidak boleh kosong!");
            }
            cardLayout.show(mainPanel, "ChooseMonster");
        });

        panel.add(namePanel, BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createHomebasePanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1));
        JLabel homebaseLabel = new JLabel(("Selamat datang di Homebase! " + player.getName()), JLabel.CENTER);
        // System.out.println("Selamat datang " + player.getName());
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
    
        private String[] getSaveFiles() {
        File dir = new File(System.getProperty("user.dir"));
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        String[] saveFiles = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            saveFiles[i] = files[i].getName();
        }
        return saveFiles;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UI::new);
        // new UI();
    }
}
