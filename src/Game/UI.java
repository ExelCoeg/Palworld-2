package Game;

import Items.*;
import Monsters.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
public class UI {
    private CardLayout cardLayout;
    private JFrame frame;
    private JPanel mainPanel;
    private JLabel playerMonsterHpLabel,enemyMonsterHpLabel, playerActionLabel, enemyActionLabel,stopwatchLabel, playerMonsterInfoLabel, enemyMonsterInfoLabel,potionLabel,goldLabel, homebaseLabel;
    private JTextArea dungeonInfo;
    private Monster monsterDipilih,monsterLawan;
    private Timer adventureTimer;
    private int elapsedTime;
    private JButton saveButton, stopButton,monsterButton;
    private Player player = new Player(null);

    private DefaultListModel<String> monsterListModel;

    private GameState gameState = new GameState();
    int width = 600, height = 425;

    public UI() {
        createUI();
    }

    public void createUI() {
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
        mainPanel.add(createManageMonstersPanel(), "ManageMonsters");

        frame.add(mainPanel);
        frame.setVisible(true);
    }
    private void startAdventure() throws PalworldException{
        try{
            JPanel panel = new JPanel(new BorderLayout());
            stopwatchLabel = new JLabel("Waktu: 0 detik", JLabel.CENTER);
            panel.add(stopwatchLabel, BorderLayout.NORTH);
        
            dungeonInfo = new JTextArea();
            dungeonInfo.setEditable(false);
            panel.add(new JScrollPane(dungeonInfo), BorderLayout.CENTER);
        
            stopButton = new JButton("Hentikan Petualangan");
            stopButton.addActionListener(e -> stopAdventure());
            panel.add(stopButton, BorderLayout.SOUTH);
        
            mainPanel.add(panel, "Adventure");
            cardLayout.show(mainPanel, "Adventure");
        
            elapsedTime = 0;
            adventureTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    elapsedTime++;
                    stopwatchLabel.setText("Waktu: " + elapsedTime + " detik");
                    if (new Random().nextInt(10) < 2) {
                        try {
                            encounterMonster();
                        } catch (Exception d) {
                            try {
                                throw new PalworldException("Failed to encounter monster: " + d.getMessage());
                            } catch (PalworldException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } else {
                        String movementText = getRandomMovementText();
                        dungeonInfo.append(movementText + "\n");
                    }
                }
            });

        }
        catch (Exception e) {
            throw new PalworldException("Failed to start adventure: " + e.getMessage());
        }
          
    
        
        adventureTimer.start();
    }
    

    private String getRandomMovementText() {
        Random random = new Random();
        int direction = random.nextInt(4);
        String movementText = "";
        switch (direction) {
            case 0:
                movementText = "Anda berjalan ke utara.";
                break;
            case 1:
                movementText = "Anda berjalan ke selatan.";
                break;
            case 2:
                movementText = "Anda berjalan ke barat.";
                break;
            case 3:
                movementText = "Anda berjalan ke timur.";
                break;
        }
        return movementText;
    }
    
    
    private void stopAdventure() {
        if (adventureTimer != null) {
            adventureTimer.stop();
        }
        cardLayout.show(mainPanel, "Homebase");
    }
    
    private void encounterMonster() throws PalworldException {
        try {
            adventureTimer.stop(); 
            selectMonsterForBattle();
            randomMonsterLawan(monsterDipilih);
            updateDungeonPanel();
            dungeonInfo.append("Anda bertemu dengan " + monsterLawan.getName() + "!\n");
            int result = JOptionPane.showConfirmDialog(frame, "Anda bertemu dengan " + monsterLawan.getName() + ". Apakah Anda ingin bertarung?", "Encounter", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                cardLayout.show(mainPanel, "Dungeon");
            } else {
                adventureTimer.start(); 
                cardLayout.show(mainPanel, "Adventure");
            }
            
        } catch (Exception e) {
            throw new PalworldException("Failed to encounter monster: " + e.getMessage());
        }
    }
    private void selectMonsterForBattle() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Pilih satu monster untuk bertarung:"));
    
        JRadioButton[] radioButtons = new JRadioButton[player.selectedMonsters.size()];
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < player.selectedMonsters.size(); i++) {
            Monster monster = player.selectedMonsters.get(i);
            radioButtons[i] = new JRadioButton(monster.getName() + " (HP: " + monster.getHp() + ")");
            group.add(radioButtons[i]);
            panel.add(radioButtons[i]);
        }
    
        int result = JOptionPane.showConfirmDialog(frame, panel, "Pilih Monster", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            for (int i = 0; i < radioButtons.length; i++) {
                if (radioButtons[i].isSelected()) {
                    monsterDipilih = player.selectedMonsters.get(i);
                    if (monsterDipilih.getHp() < 10) {
                        int healOrChoose = JOptionPane.showOptionDialog(frame,
                                "Monster " + monsterDipilih.getName() + " memiliki HP di bawah 10. Anda harus menyembuhkan monster ini atau memilih monster lain.",
                                "Monster HP Rendah",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.WARNING_MESSAGE,
                                null,
                                new String[]{"Heal", "Pilih Monster Lain"},
                                "Heal");
    
                        if (healOrChoose == JOptionPane.YES_OPTION) {
                            monsterDipilih.Heal();
                            JOptionPane.showMessageDialog(frame, monsterDipilih.getName() + " telah disembuhkan!");
                        } else {
                            selectMonsterForBattle();
                            return;
                        }
                    }
                    JOptionPane.showMessageDialog(frame, "Anda memilih " + monsterDipilih.getName());
                    updateDungeonPanel(); // Update the dungeon panel after selecting a monster
                    cardLayout.show(mainPanel, "Dungeon");
                    return;
                }
            }
            JOptionPane.showMessageDialog(frame, "Anda harus memilih satu monster!");
        } else {
            JOptionPane.showMessageDialog(frame, "Pertarungan dibatalkan!");
            cardLayout.show(mainPanel, "Homebase");
        }
    }
    private void chooseMonster(int choice) {
        switch (choice) {
            case 1 -> player.monsters.add(new MonsterApi("Flameling", 1, 100, 100, Monster.elements.API));
            case 2 -> player.monsters.add(new MonsterAngin("Zephyrkin", 1, 100, 0, Monster.elements.ANGIN));
            case 3 -> player.monsters.add(new MonsterAir("Aquabot", 1, 100, 0, Monster.elements.AIR));
            case 4 -> player.monsters.add(new MonsterEs("Frostlet", 1, 100, 0, Monster.elements.ES));
            case 5 -> player.monsters.add(new MonsterTanah("Terrapup", 1, 100, 0, Monster.elements.TANAH));
            default -> JOptionPane.showMessageDialog(frame, "Invalid selection!");
        }
        JOptionPane.showMessageDialog(frame, "Selected: " + player.monsters.get(0).getName());
        cardLayout.show(mainPanel, "Homebase");
    }
    private JPanel createChooseMonsterPanel(){
        JPanel panel = new JPanel(new GridLayout(6, 1));
        JLabel instructions = new JLabel("Select your first monster:", JLabel.CENTER);
        panel.add(instructions);
        

        String[] monsters = {"Flameling (API)", "Zephyrkin (ANGIN)", "Aquabot (AIR)", "Frostlet (ES)", "Terrapup (TANAH)"};
        for (int i = 0; i < monsters.length; i++) {
            monsterButton = new JButton(monsters[i]);
            int finalI = i;
            monsterButton.addActionListener(e -> {

                chooseMonster(finalI + 1);
                updateMonsterList();
            }
            
            );
            panel.add(monsterButton);
        }
        return panel;
    }

    private JPanel createMainMenuPanel() {
        // Create the main panel with BorderLayout
        JPanel panel = new JPanel(new BorderLayout());

        // Load and set the background image
        ImageIcon backgroundIcon = new ImageIcon("Background_homebase.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        panel.add(backgroundLabel);
        backgroundLabel.setLayout(new BorderLayout());

        // Create a sub-panel for buttons with vertical alignment
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // Load the button background image
        ImageIcon buttonIcon = new ImageIcon("Button_wood_texture.png");
        Dimension buttonSize = new Dimension(200, 50);  // Adjusted button size

        // Add the welcome label
        JLabel welcomeLabel = new JLabel("Selamat datang di Palworld!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Sorts Mill Goudy", Font.BOLD, 32));
        welcomeLabel.setForeground(Color.orange);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setMaximumSize(new Dimension(600, 50));  // Adjusted size
        buttonPanel.add(Box.createVerticalStrut(20));  // Spacer
        buttonPanel.add(welcomeLabel);
        buttonPanel.add(Box.createVerticalStrut(30));  // Spacer

        // Add the new game button with text on top
        JButton newGameButton = new JButton("Start Game", buttonIcon);
        newGameButton.setFont(new Font("Sorts Mill Goudy", Font.BOLD, 24));
        newGameButton.setForeground(Color.WHITE);

        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.setPreferredSize(buttonSize);
        newGameButton.setMaximumSize(buttonSize);
        newGameButton.setHorizontalTextPosition(JButton.CENTER);
        newGameButton.setVerticalTextPosition(JButton.CENTER);
        newGameButton.setContentAreaFilled(false);
        newGameButton.setBorderPainted(false);
        newGameButton.setFocusPainted(false);
        newGameButton.addActionListener(e -> cardLayout.show(mainPanel, "NewGame"));
        buttonPanel.add(newGameButton);
        buttonPanel.add(Box.createVerticalStrut(20));  // Spacer

        // Add the load game button with text on top
        JButton loadButton = new JButton("Load Game", buttonIcon);
        loadButton.setFont(new Font("Sorts Mill Goudy", Font.BOLD, 24));
        loadButton.setForeground(Color.WHITE);
        
        loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadButton.setPreferredSize(buttonSize);
        loadButton.setMaximumSize(buttonSize);
        loadButton.setHorizontalTextPosition(JButton.CENTER);
        loadButton.setVerticalTextPosition(JButton.CENTER);
        loadButton.setContentAreaFilled(false);
        loadButton.setBorderPainted(false);
        loadButton.setFocusPainted(false);
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] saveFiles = new File("src/Saves").list((dir, name) -> name.endsWith(".txt"));
                if (saveFiles != null && saveFiles.length > 0) {
                    String selectedFile = (String) JOptionPane.showInputDialog(
                            frame,
                            "Select a save file to load:",
                            "Load Game",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            saveFiles,
                            saveFiles[0]
                    );
                    if (selectedFile != null) {
                        try {
                            GameState loadedGameState = SaveAndLoad.loadGame(selectedFile);
                            player.setName(loadedGameState.getPlayerName());
                            player.setMonsters(loadedGameState.getMonsters());
                            player.setPotions(loadedGameState.getPotions());
                            mainPanel.add(createHomebasePanel(), "Homebase");
                            cardLayout.show(mainPanel, "Homebase");
                        } catch (IOException | ClassNotFoundException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Failed to load the game!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "No save files found!");
                }
            }
        });
        buttonPanel.add(loadButton);
        buttonPanel.add(Box.createVerticalStrut(20));  // Spacer

        // Add the exit button with text on top
        JButton exitButton = new JButton("Exit", buttonIcon);
        exitButton.setFont(new Font("Sorts Mill Goudy", Font.BOLD, 16));
        exitButton.setForeground(Color.WHITE);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setPreferredSize(buttonSize);
        exitButton.setMaximumSize(buttonSize);
        exitButton.setHorizontalTextPosition(JButton.CENTER);
        exitButton.setVerticalTextPosition(JButton.CENTER);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createVerticalStrut(20));  // Spacer

        // Add the button panel to the center of the background label
        backgroundLabel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createNewGamePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel("Masukkan nama player: ", JLabel.CENTER);
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 35));

        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        namePanel.add(nameField);

        JButton startButton = new JButton("Mulai");
        startButton.addActionListener(e -> {
            String playerName = nameField.getText();
            if (!playerName.isEmpty()) {
                player.setName(playerName);
                mainPanel.add(createHomebasePanel(), "Homebase");
                cardLayout.show(mainPanel, "ChooseMonster");
            } else {
                JOptionPane.showMessageDialog(frame, "Nama tidak boleh kosong!");
            }
        });
        panel.add(namePanel, BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.SOUTH);
        return panel;
    }
    private void updateDungeonPanel() {
        playerMonsterHpLabel.setText("Player HP: " + (monsterDipilih != null ? monsterDipilih.getHp() : "N/A"));
        enemyMonsterHpLabel.setText("Enemy HP: " + (monsterLawan != null ? monsterLawan.getHp() : "N/A"));
        playerActionLabel.setText("Player Action: ");
        enemyActionLabel.setText("Enemy Action: ");
        dungeonInfo.setText(""); // Clear previous dungeon info
    
        playerMonsterInfoLabel.setText("Player Monster: " + (monsterDipilih != null ? monsterDipilih.getName() + " (HP: " + monsterDipilih.getHp() + ")" : "N/A"));
        enemyMonsterInfoLabel.setText("Enemy Monster: " + (monsterLawan != null ? monsterLawan.getName() + " (HP: " + monsterLawan.getHp() + ")" : "N/A"));
    }

    private void checkLevelUp(Monster monster) {
        while (monster.getExp() >= 100) {
            monster.setExp(monster.getExp() - 100);
            monster.setLevel(monster.getLevel() + 1);
            monster.setHp(monster.getHp() + 20);
            monster.setDamage(monster.getDamage() + 5);
            JOptionPane.showMessageDialog(frame, monster.getName() + " telah naik ke level " + monster.getLevel() + "!");
        }
    }
    private void updateHomebaseLabel(String playerName) {
        homebaseLabel.setText("Selamat datang di Palworld! " + playerName);
    }
    private JPanel createHomebasePanel() {
        // Create the main panel with a layered layout
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 350)); // Set preferred size for the layered pane
    
        // Load the background image
        ImageIcon backgroundIcon = new ImageIcon("Background_homebase.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight());

        ImageIcon buttonIcon = new ImageIcon("Button_wood_texture.png");
    
        // Create a panel for the buttons and other components
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false); // Make the panel transparent
        panel.setBounds(0, 0, backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight());
    
        // Homebase label
        homebaseLabel = new JLabel("Selamat datang di Homebase! " + player.getName(), JLabel.CENTER);
        homebaseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        homebaseLabel.setFont(new Font("Sorts Mill Goudy", Font.BOLD, 24));
        homebaseLabel.setForeground(Color.ORANGE);
        panel.add(homebaseLabel);
    
        panel.add(Box.createRigidArea(new Dimension(0, 15))); // Add spacing
    
        // Heal Button
        JPanel healPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        healPanel.setOpaque(false); // Make the panel transparent
        JButton healButton = new JButton("Heal Monster");
        healButton.setPreferredSize(new Dimension(180, 30)); // Set button size
        healButton.setIcon(buttonIcon);
        healButton.setVerticalTextPosition(SwingConstants.CENTER);
        healButton.setHorizontalTextPosition(SwingConstants.CENTER);
        healButton.setFont(new Font("Sorts Mill Goudy", Font.BOLD, 16));
        healButton.setForeground(Color.WHITE);
        healButton.addActionListener(e -> {
            for (Monster monster : player.monsters) {
                monster.Heal();
            }
            updateMonsterList();
            JOptionPane.showMessageDialog(frame, "All monsters healed!");
        });
        healPanel.add(healButton);
        panel.add(healPanel);
    
        // Evolve Button
        JPanel evolvePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        evolvePanel.setOpaque(false); // Make the panel transparent
        JButton evolveButton = new JButton("Evolve Monster");
        evolveButton.setIcon(buttonIcon);
        evolveButton.setVerticalTextPosition(SwingConstants.CENTER);
        evolveButton.setHorizontalTextPosition(SwingConstants.CENTER);
        evolveButton.setPreferredSize(new Dimension(180, 30)); // Set button size
        evolveButton.setFont(new Font("Sorts Mill Goudy", Font.BOLD, 16));
        evolveButton.setForeground(Color.WHITE);
        evolveButton.addActionListener(e -> {
            for (Monster monster : player.monsters) {
                if (!monster.evolved) {
                    if (monster.getLevel() >= 10) {
                        String newElement = JOptionPane.showInputDialog(frame,
                                "Enter new element for " + monster.getName() + ":\n" +
                                        "(API, ANGIN, AIR, ES, TANAH), element should be the most logical.");
    
                        if (newElement != null) {
                            Monster evolvedMonster = monster.Evolve(newElement);
                            if (evolvedMonster != null) {
                                player.monsters.remove(monster);
                                player.monsters.add(evolvedMonster);
                                updateMonsterList();
                                JOptionPane.showMessageDialog(frame, "Monster evolved to " + evolvedMonster.getName());
                            } else {
                                JOptionPane.showMessageDialog(frame, "Evolution failed! Invalid element.");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, monster.getName() + " needs to be at least level 10 to evolve.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, monster.getName() + " cannot evolve anymore.");
                }
            }
        });
        evolvePanel.add(evolveButton);
        panel.add(evolvePanel);
    
        // Buy Item Button
        JPanel buyItemPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buyItemPanel.setOpaque(false); // Make the panel transparent
        JButton buyItemButton = new JButton("Beli Item");
        buyItemButton.setIcon(buttonIcon);
        buyItemButton.setVerticalTextPosition(SwingConstants.CENTER);
        buyItemButton.setHorizontalTextPosition(SwingConstants.CENTER);
        buyItemButton.setPreferredSize(new Dimension(180, 30)); // Set button size
        buyItemButton.setFont(new Font("Sorts Mill Goudy", Font.BOLD, 16));
        buyItemButton.setForeground(Color.WHITE);
        buyItemButton.addActionListener(e -> {
            mainPanel.add(BuyPotion(), "BuyItem");
            cardLayout.show(mainPanel, "BuyItem");
        });
        buyItemPanel.add(buyItemButton);
        panel.add(buyItemPanel);
    
        // Dungeon Button
        JPanel dungeonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dungeonPanel.setOpaque(false); // Make the panel transparent
        JButton dungeonButton = new JButton("Keluar Homebase");
        dungeonButton.setIcon(buttonIcon);
        dungeonButton.setVerticalTextPosition(SwingConstants.CENTER);
        dungeonButton.setHorizontalTextPosition(SwingConstants.CENTER);
        dungeonButton.setPreferredSize(new Dimension(180, 30)); // Set button size
        dungeonButton.setFont(new Font("Sorts Mill Goudy", Font.BOLD, 16));
        dungeonButton.setForeground(Color.WHITE);
        dungeonButton.addActionListener(e -> chooseMonstersForDungeon());
        dungeonPanel.add(dungeonButton);
        panel.add(dungeonPanel);
    
        // Manage Monsters Button
        JPanel manageMonstersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        manageMonstersPanel.setOpaque(false); // Make the panel transparent
        JButton manageMonstersButton = new JButton("Manage Monsters");
        manageMonstersButton.setIcon(buttonIcon);
        manageMonstersButton.setVerticalTextPosition(SwingConstants.CENTER);
        manageMonstersButton.setHorizontalTextPosition(SwingConstants.CENTER);
        manageMonstersButton.setPreferredSize(new Dimension(180, 30)); // Set button size
        mainPanel.add(createManageMonstersPanel(), "ManageMonsters");
        manageMonstersButton.addActionListener(e -> cardLayout.show(mainPanel, "ManageMonsters"));
        manageMonstersButton.setFont(new Font("Sorts Mill Goudy", Font.BOLD, 16));
        manageMonstersButton.setForeground(Color.WHITE);
        manageMonstersPanel.add(manageMonstersButton);
        panel.add(manageMonstersPanel);
    
        // Save Button
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        savePanel.setOpaque(false); // Make the panel transparent
        saveButton = new JButton("Save Game");
        saveButton.setIcon(buttonIcon);
        saveButton.setVerticalTextPosition(SwingConstants.CENTER);
        saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
        saveButton.setFont(new Font("Sorts Mill Goudy", Font.BOLD, 16));
        saveButton.setForeground(Color.WHITE);
        saveButton.setPreferredSize(new Dimension(180, 30)); // Set button size
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    GameState gameState = new GameState(player.getName(), player.getGold(), player.monsters, player.getPotions());
                    SaveAndLoad.saveGame(gameState, player.getName());
                    JOptionPane.showMessageDialog(frame, "Game saved successfully!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Failed to save the game!");
                }
            }
        });
        savePanel.add(saveButton);
        panel.add(savePanel);
    
        updateHomebaseLabel(player.getName());
    
        // Add the components to the layered pane
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(panel, JLayeredPane.PALETTE_LAYER);
    
        // Create a new JPanel to add the layered pane and return it
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(layeredPane, BorderLayout.CENTER);
    
        return mainPanel;
    }
    
    
    private JPanel BuyPotion() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Welcome to the Potion Shop!", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(title, BorderLayout.NORTH);

        goldLabel = new JLabel("Gold: " + player.getGold(), JLabel.CENTER); // Inisialisasi goldLabel dengan nilai awal
        panel.add(goldLabel, BorderLayout.SOUTH);

        JPanel potionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
    
        JButton healthPotionButton = new JButton("Buy Health Potion");
        JButton damagePotionButton = new JButton("Buy Damage Potion");
    
        JLabel healthPotionLabel = new JLabel("Price: 50 Gold");
        JLabel damagePotionLabel = new JLabel("Price: 75 Gold");
    
        potionPanel.add(healthPotionLabel);
        potionPanel.add(healthPotionButton);
        potionPanel.add(damagePotionLabel);
        potionPanel.add(damagePotionButton);
    
        panel.add(potionPanel, BorderLayout.CENTER);
    
        JTextArea outputArea = new JTextArea(5, 20);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        panel.add(scrollPane, BorderLayout.EAST);

        // Action listeners for the buttons
        healthPotionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player.getGold() >= 50) {
                    player.addGold(-50);
                    outputArea.append("Health Potion bought for 50 Gold.\n");
                    updateGoldLabel();
                } else {
                    outputArea.append("Not enough gold to buy Health Potion!\n");
                }
            }
        });
    
        damagePotionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player.getGold() >= 75) {
                    player.addGold(75);
                    outputArea.append("Damage Potion bought for 75 Gold.\n");
                    updateGoldLabel();
                } else {
                    outputArea.append("Not enough gold to buy Damage Potion!\n");
                }
            }
        });

        JButton homeBaseButton = new JButton("Return to Home Base");
        homeBaseButton.addActionListener(new ActionListener() {
            @Override
        public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Homebase");
            }
        });
    
        panel.add(homeBaseButton, BorderLayout.WEST);
    
        return panel;
    }
    private void updateGoldLabel() {
        goldLabel.setText("Gold: " + player.getGold());
    }
    private JPanel createDungeonPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel dungeonLabel = new JLabel("Selamat datang di Dungeon!", JLabel.CENTER);
        panel.add(dungeonLabel, BorderLayout.NORTH);
    
        dungeonInfo = new JTextArea();
        dungeonInfo.setEditable(false);
        panel.add(new JScrollPane(dungeonInfo), BorderLayout.CENTER);
    
        JPanel statusPanel = new JPanel(new GridLayout(3, 1));
        playerMonsterHpLabel = new JLabel("Player HP: 100", JLabel.CENTER);
        enemyMonsterHpLabel = new JLabel("Enemy HP: 100", JLabel.CENTER);
        playerActionLabel = new JLabel("Player Action: ", JLabel.CENTER);
        enemyActionLabel = new JLabel("Enemy Action: ", JLabel.CENTER);
        statusPanel.add(playerMonsterHpLabel);
        statusPanel.add(enemyMonsterHpLabel);
        statusPanel.add(playerActionLabel);
        statusPanel.add(enemyActionLabel);
    
        panel.add(statusPanel, BorderLayout.SOUTH);
    
        JPanel monsterInfoPanel = new JPanel(new GridLayout(2, 1));
        playerMonsterInfoLabel = new JLabel("Player Monster: ", JLabel.CENTER);
        enemyMonsterInfoLabel = new JLabel("Enemy Monster: ", JLabel.CENTER);
        monsterInfoPanel.add(playerMonsterInfoLabel);
        monsterInfoPanel.add(enemyMonsterInfoLabel);
    
        panel.add(monsterInfoPanel, BorderLayout.CENTER);
    
        JPanel actionPanel = new JPanel(new GridLayout(1, 5)); // Adjusted grid layout to accommodate more buttons
    
        JButton basicAttackButton = new JButton("Basic Attack");
        basicAttackButton.addActionListener(e -> playerAction(1));
        actionPanel.add(basicAttackButton);
    
        JButton specialAttackButton = new JButton("Special Attack");
        specialAttackButton.addActionListener(e -> playerAction(2));
        actionPanel.add(specialAttackButton);
    
        JButton elementalAttackButton = new JButton("Elemental Attack");
        elementalAttackButton.addActionListener(e -> playerAction(3));
        actionPanel.add(elementalAttackButton);
    
        JButton useHealthPotionButton = new JButton("Use Health Potion");
        useHealthPotionButton.addActionListener(e -> useHealthPotion());
        actionPanel.add(useHealthPotionButton);
    
        JButton useDamagePotionButton = new JButton("Use Damage Potion");
        useDamagePotionButton.addActionListener(e -> useDamagePotion(monsterLawan));
        actionPanel.add(useDamagePotionButton);
    
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(actionPanel, BorderLayout.NORTH);
    
        panel.add(containerPanel, BorderLayout.NORTH);
    
        return panel;
    }
    private Potion searchHealthPotion(){
        for(Potion potion : player.getPotions()){
            if(potion instanceof HealthPotion){
                return potion;
            }
        }
        return null;
    }
    private Potion searchDamagePotion(){
        for(Potion potion : player.getPotions()){
            if(potion instanceof ElementalPotion){
                return potion;
            }
        }
        return null;
    }
    private void useHealthPotion() {
        player.usePotion(searchHealthPotion(), monsterDipilih);
    }
    
    private void useDamagePotion(Monster enemy) {
         player.usePotion(searchDamagePotion(), monsterLawan);
    }
    private void updatePlayerStatus() {
        playerMonsterHpLabel.setText("Player HP: " + player.monsters.get(0).getHp());
    }
    
    private void updateEnemyStatus() {
    }


    private void chooseMonstersForDungeon() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Pilih hingga 3 monster untuk dibawa ke dungeon:"));
    
        JCheckBox[] checkboxes = new JCheckBox[player.monsters.size()];
        for (int i = 0; i < player.monsters.size(); i++) {
            Monster monster = player.monsters.get(i);
            checkboxes[i] = new JCheckBox(monster.getName());
            panel.add(checkboxes[i]);
        }
    
        int result = JOptionPane.showConfirmDialog(frame, panel, "Pilih Monster", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            ArrayList<Monster> selectedMonsters = new ArrayList<>();
            for (int i = 0; i < checkboxes.length; i++) {
                if (checkboxes[i].isSelected()) {
                    selectedMonsters.add(player.monsters.get(i));
                }
            }
    
            if (selectedMonsters.size() > 3) {
                JOptionPane.showMessageDialog(frame, "Anda hanya bisa membawa maksimal 3 monster!");
            } else if (selectedMonsters.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Anda harus memilih setidaknya 1 monster!");
            } else {
                player.selectedMonsters = selectedMonsters;
                JOptionPane.showMessageDialog(frame, "Anda membawa " + selectedMonsters.size() + " monster ke dungeon.");
                
                try{
                    startAdventure(); // Mulai petualangan setelah memilih monster
                }
                catch (PalworldException e){
                    JOptionPane.showMessageDialog(frame, e.getMessage());
                }
            }
        }
    }

    private JPanel createManageMonstersPanel() {
     
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Manage Your Monsters", JLabel.CENTER);
        panel.add(label, BorderLayout.NORTH);

        monsterListModel = new DefaultListModel<>();
        for (Monster monster : player.monsters) {
            monsterListModel.addElement(monster.getName());
        }

        JList<String> monsterList = new JList<>(monsterListModel);
        monsterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(monsterList);

        JTextArea monsterDetails = new JTextArea();
        monsterDetails.setEditable(false);

        monsterList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = monsterList.getSelectedIndex();
                if (index >= 0) {
                    Monster selectedMonster = player.monsters.get(index);
                    monsterDetails.setText("Name: " + selectedMonster.getName() +
                            "\nHP: " + selectedMonster.getHp() +
                            "\nLevel: " + selectedMonster.getLevel() +
                            "\nElement: " + selectedMonster.getElemen().toString());
                }
            }
        });
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, new JScrollPane(monsterDetails));
        splitPane.setDividerLocation(200);
        panel.add(splitPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Base");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Homebase"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }
    private void updateMonsterList() {
        monsterListModel.clear();
        for (Monster monster : player.monsters) {
            monsterListModel.addElement(monster.getName() + " (HP: " + monster.getHp() + ")");
        }
    }
    private void randomMonsterLawan(Monster monsterDipilih){
        Random rand = new Random();
        int randomMonster = rand.nextInt(5) + 1;
        switch(randomMonster){
            case 1 -> monsterLawan = new MonsterApi("Random Monster Api", 1, 100, 0, Monster.elements.API);
            case 2 -> monsterLawan = new MonsterAngin("Random Monster Angin", 1, 100, 0, Monster.elements.ANGIN);
            case 3 -> monsterLawan = new MonsterAir("Random Monster Air", 1, 100, 0, Monster.elements.AIR);
            case 4 -> monsterLawan = new MonsterEs("Random Monster Es", 1, 100, 0, Monster.elements.ES);
            case 5 -> monsterLawan = new MonsterTanah("Random Monster Tanah", 1, 100, 0, Monster.elements.TANAH);
        }
        monsterLawan.setLevel(rand.nextInt(monsterDipilih.getLevel(),monsterDipilih.getLevel() + 1));
        monsterLawan.setHp(100);
    }
    private void playerAction(int action) {
        if (monsterDipilih == null || monsterLawan == null) {
            monsterDipilih = player.monsters.get(0);
        }
        switch (action) {
            case 1 -> {
                monsterDipilih.BasicAttack(monsterLawan);
                dungeonInfo.append("Monster " + monsterDipilih.getName() + " menyerang " + monsterLawan.getName() + " dengan Basic Attack\n");
            }
            case 2 -> {
                monsterDipilih.SpecialAttack(monsterLawan);
                dungeonInfo.append("Monster " + monsterDipilih.getName() + " menyerang " + monsterLawan.getName() + " dengan Special Attack\n");
            }
            case 3 -> {
                if (monsterDipilih instanceof ElementalMonster elementalMonster) {
                    elementalMonster.ElementalAttack(monsterLawan);
                    dungeonInfo.append("Monster " + monsterDipilih.getName() + " menyerang " + monsterLawan.getName() + " dengan Elemental Attack\n");
                } else {
                    dungeonInfo.append("Monster tidak memiliki serangan elemen\n");
                }
            }
            default -> dungeonInfo.append("Pilihan tidak valid!\n");
        }
    
        updateHpLabels();
        int randomAction = new Random().nextInt(3); 
        enemyAction(randomAction); 
        updateHpLabels(); 
        
        if (monsterDipilih.getHp() <= 0) {
            JOptionPane.showMessageDialog(frame, "Anda Kalah dan " + monsterDipilih.getName() + " pingsan\n");
            updateMonsterList();
            cardLayout.show(mainPanel,"Homebase");
        } 
    
        else if (monsterLawan.getHp() <= 0) {
            monsterDipilih.setExp(monsterDipilih.getExp() + monsterLawan.getLevel() * 10);
            player.addGold(monsterLawan.getLevel() * 10);   
            JOptionPane.showMessageDialog(frame, "Anda Menang dan " + monsterDipilih.getName() + " mendapatkan " + //
            monsterLawan.getLevel() * 10 + " exp\n" + //
            "Anda juga mendapatkan " + monsterLawan.getLevel() * 10 + " gold\n");
            checkLevelUp(monsterDipilih); // Tambahkan ini untuk memeriksa level up
            int result = JOptionPane.showConfirmDialog(frame, "Anda mengalahkan " + monsterLawan.getName() + //
            ". Apakah Anda ingin menangkap monster ini?", "Capture", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                String monsterName = JOptionPane.showInputDialog(frame, "Berikan nama untuk monster baru ini:");
                if (monsterName != null && !monsterName.isEmpty()) {
                    monsterLawan.setName(monsterName);
                    player.monsters.add(monsterLawan);
                    updateMonsterList();
                    JOptionPane.showMessageDialog(frame, monsterName + " telah ditambahkan ke koleksi monster Anda!");
                }
            }
            
            cardLayout.show(mainPanel, "Homebase");
        }
        String actionString = getActionString(action);
        updateActionLabels(actionString, "");
}
    
    private void enemyAction(int action) {
        switch (action) {
            case 0 -> {
                monsterLawan.BasicAttack(monsterDipilih);
                dungeonInfo.append("Monster " + monsterLawan.getName() + " menyerang " + monsterDipilih.getName() + " dengan Basic Attack\n");
            }
            case 1 -> {
                monsterLawan.SpecialAttack(monsterDipilih);
                dungeonInfo.append("Monster " + monsterLawan.getName() + " menyerang " + monsterDipilih.getName() + " dengan Special Attack\n");
            }
            case 2 -> {
                if (monsterLawan instanceof ElementalMonster elementalMonster) {
                    elementalMonster.ElementalAttack(monsterDipilih);
                    dungeonInfo.append("Monster " + monsterLawan.getName() + " menyerang " + monsterDipilih.getName() + " dengan Elemental Attack\n");
                }
            }
        }
    
        updateHpLabels(); 
        String actionString = getActionString(action); 
        updateActionLabels("", actionString);
    }
    
    private String getActionString(int action) {
        switch (action) {
            case 1:
                return "Basic Attack";
            case 2:
                return "Special Attack";
            case 3:
                return "Elemental Attack";
            default:
                return "Invalid Action";
        }
    }

    private void updateHpLabels() {
        playerMonsterHpLabel.setText("Player HP: " + monsterDipilih.getHp());
        enemyMonsterHpLabel.setText("Enemy HP: " + monsterLawan.getHp());
    }

    private void updateActionLabels(String playerAction, String enemyAction) {
        playerActionLabel.setText("Player Action: " + playerAction);
        enemyActionLabel.setText("Enemy Action: " + enemyAction);
    }

   
    public static void main(String[] args) {
        SwingUtilities.invokeLater(UI::new);
    }
}