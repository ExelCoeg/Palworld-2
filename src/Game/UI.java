package Game;

import Monsters.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
public class UI {
    private CardLayout cardLayout;
    private JFrame frame;
    private JPanel mainPanel;
    private JLabel playerMonsterHpLabel,enemyMonsterHpLabel, playerActionLabel, enemyActionLabel,stopwatchLabel, playerMonsterInfoLabel, //
    enemyMonsterInfoLabel,goldLabelDungeon, goldLabel;
    private JTextArea dungeonInfo;
    private Monster monsterDipilih,monsterLawan;
    private Timer adventureTimer;
    private int elapsedTime;
    
    private Player player = new Player();

    private DefaultListModel<String> monsterListModel;
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
        mainPanel.add(createHomebasePanel(), "HomeBase");
        mainPanel.add(createChooseMonsterPanel(), "ChooseMonster");
        mainPanel.add(createDungeonPanel(), "Dungeon");
        mainPanel.add(createManageMonstersPanel(), "ManageMonsters");
        mainPanel.add(BuyPotion(), "BuyItem");

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
        
            JButton stopButton = new JButton("Hentikan Petualangan");
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
            JButton monsterButton = new JButton(monsters[i]);
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
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel welcomeLabel = new JLabel("Selamat datang di Palworld!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Sorts Mill Goudy", Font.BOLD, 32));
        welcomeLabel.setBounds(80, 50, 440, 50);
        panel.add(welcomeLabel);

        JButton newGameButton = new JButton("Start Game");
        newGameButton.setBounds(250, 150, 100, 50);
        newGameButton.addActionListener(e -> cardLayout.show(mainPanel, "NewGame"));
        panel.add(newGameButton);

        JButton loadGameButton = new JButton("Load Game");
        loadGameButton.setBounds(250, 220, 100, 50);
        loadGameButton.addActionListener(e -> {
            // Load game logic here
        });
        panel.add(loadGameButton);

        JButton exitButton = new JButton("Exit (Auto Save)");
        exitButton.setBounds(250, 290, 100, 50);
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
            monster.setHp(monster.getHp() + 20); // contoh kenaikan HP setiap level up
            monster.setDamage(monster.getDamage() + 5); // contoh kenaikan Attack setiap level up
            JOptionPane.showMessageDialog(frame, monster.getName() + " telah naik ke level " + monster.getLevel() + "!");
        }
    }

    private JPanel createHomebasePanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1));
        JLabel homebaseLabel = new JLabel(("Selamat datang di Homebase! " + player.getName()), JLabel.CENTER);
        panel.add(homebaseLabel);
    
        JButton healButton = new JButton("Heal Monster");
        healButton.addActionListener(e -> {
            for (Monster monster : player.monsters) {
                monster.Heal();
            }
            updateMonsterList();
            JOptionPane.showMessageDialog(frame, "All monsters healed!");
        });
        panel.add(healButton);
    
        JButton evolveButton = new JButton("Evolve Monster");
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
                    } 
                    else {
                        JOptionPane.showMessageDialog(frame, monster.getName() + " needs to be at least level 10 to evolve.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, monster.getName() + " cannot evolve anymore.");
                }
            }
        });
        
        panel.add(evolveButton);
    
        JButton buyItemButton = new JButton("Beli Item");
        buyItemButton.addActionListener(e -> cardLayout.show(mainPanel, "BuyItem")); // Switch to the BuyPotion panel
        panel.add(buyItemButton);
    
        

        JButton dungeonButton = new JButton("Keluar Homebase");
        dungeonButton.addActionListener(e -> chooseMonstersForDungeon());
        panel.add(dungeonButton);
        
    
        JButton manageMonstersButton = new JButton("Manage Monsters");
        mainPanel.add(createManageMonstersPanel(), "ManageMonsters");
        manageMonstersButton.addActionListener(e -> cardLayout.show(mainPanel, "ManageMonsters"));
        panel.add(manageMonstersButton);
    
    
        return panel;
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
                CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
                cardLayout.show(mainPanel, "HomeBase");
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
    
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(actionPanel, BorderLayout.NORTH);
    
        panel.add(containerPanel, BorderLayout.NORTH);
    
        return panel;
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
            updateGoldLabel();
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
                    updateGoldLabel();
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
