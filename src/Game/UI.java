package Game;

import Monsters.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class UI {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Player player = new Player();
    private JTextArea dungeonInfo;
    private Monster monsterDipilih;
    private Monster monsterLawan;

    int width = 600, height = 400;

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
        mainPanel.add(createHomebasePanel(), "Homebase");
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
    }

    private JPanel createChooseMonsterPanel() {
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
            // Load game logic here
        });
        panel.add(loadGameButton);

        JButton exitButton = new JButton("Exit (Auto Save)");
        exitButton.setBounds(270, 290, 100, 50);
        exitButton.addActionListener(e -> {
            // Save game logic here
            System.exit(0);
        });
        panel.add(exitButton);

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
        JLabel homebaseLabel = new JLabel(("Selamat datang di Homebase! " + player.getName()), JLabel.CENTER);
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
    
        dungeonInfo = new JTextArea();
        dungeonInfo.setEditable(false);
        panel.add(new JScrollPane(dungeonInfo), BorderLayout.CENTER);
    
        JPanel actionPanel = new JPanel(new GridLayout(1, 3));
    
        JButton basicAttackButton = new JButton("Basic Attack");
        basicAttackButton.addActionListener(e -> playerAction(1));
        actionPanel.add(basicAttackButton);
    
        JButton specialAttackButton = new JButton("Special Attack");
        specialAttackButton.addActionListener(e -> playerAction(2));
        actionPanel.add(specialAttackButton);
    
        JButton elementalAttackButton = new JButton("Elemental Attack");
        elementalAttackButton.addActionListener(e -> playerAction(3));
        actionPanel.add(elementalAttackButton);
    
        // Create a new container panel for actionPanel and escape panel
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(actionPanel, BorderLayout.NORTH);
    
        // Create escape panel with Back to Base button
        JPanel escapePanel = new JPanel();
        JButton backButton = new JButton("Back to Base");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Homebase"));
        escapePanel.add(backButton);
    
        containerPanel.add(escapePanel, BorderLayout.SOUTH);
    
        panel.add(containerPanel, BorderLayout.SOUTH);
    
        return panel;
    }
    

    private void playerAction(int action) {
        if (monsterDipilih == null || monsterLawan == null) {
            monsterDipilih = player.monsters.get(0); // Select your monster
            monsterLawan = new MonsterTanah("TanahMonster", 1, 100, 0, Monster.elements.TANAH); // Example opponent
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

        enemyAction();

        if (monsterDipilih.getHp() <= 0) {
            dungeonInfo.append("Monster Anda pingsan!\n");
        } else if (monsterLawan.getHp() <= 0) {
            monsterDipilih.setExp(monsterLawan.getLevel() * 10);
            dungeonInfo.append("Monster lawan pingsan!\n");
            dungeonInfo.append("Anda mendapatkan " + monsterLawan.getLevel() * 10 + " exp!\n");
        }
    }

    private void enemyAction() {
        int aksiMonsterLawan = new Random().nextInt(2);
        switch (aksiMonsterLawan) {
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
    }

    public static void main(String[] args) {
        new UI();
    }
}
