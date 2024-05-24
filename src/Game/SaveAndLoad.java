package Game;

import Monsters.*;
import java.io.*;
import java.util.*;

public class SaveAndLoad {
    private static final int MAX_SAVES = 5;
    //buat ngesave di folder yang sama dengan game di Palworld/saves
    private static final String SAVE_PATH = System.getProperty("user.dir");

    public static void saveGame(GameState gameState, String fileName) throws IOException {
        File dir = new File(SAVE_PATH);
        File[] files = dir.listFiles((d, name) -> name.startsWith(fileName));
        if (files.length >= MAX_SAVES) {
            System.out.println("Maksimum save files telah tercapai.");
            return;
        }

        String newFileName = fileName + ".txt";
        try (FileOutputStream fileOut = new FileOutputStream(newFileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(gameState);
        }
    }

    public static GameState loadGame(String fileName) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (GameState) in.readObject();
        }
    }

    public static int getNumberOfSaves(String fileName) {
        File dir = new File(SAVE_PATH);
        return (int) dir.listFiles((d, name) -> name.startsWith(fileName)).length;
    }

    // Metode overwriteGame dipindahkan ke dalam kelas SaveAndLoad
    public static void overwriteGame(GameState gameState, String fileName) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(gameState);
        }
    }

    public static void savesList() {
        File dir = new File(SAVE_PATH);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        for(int i=0;i<files.length;i++){
            System.out.println((i+1) + ". " + files[i].getName());
        }
    }

    //Contoh penggunaan
    public static void main(String[] args) {
        // Membuat beberapa monster
        // Monster monsterA = new MonsterApi("Flameling", 1, 100, 0, Monster.elements.API);
    
        // Menambahkan monster ke peta
        // Map<String, Monster> monsters = new HashMap<>();
        // monsters.put("Monster A", monsterA);
       
    
        // Membuat daftar item
        // List<String> items = new ArrayList<>();
        // items.add("Potion");
        // items.add("Elixir");
    
        // Membuat GameState
        // GameState gameState = new GameState("exel",monsters, 1000, new ArrayList<>());
    
        // Menyimpan GameState
        // try {
        //     SaveAndLoad.saveGame(gameState, gameState.getPlayerName());
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        // //Overwrite GameState yang udah ada
        // try {
        //     SaveAndLoad.overwriteGame(gameState, "saveFile-3.txt");
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }
    
}

class GameState implements Serializable {
    private String playerName;
    
    private Map<String, Monster> monsters;
    private int currency;
    private List<String> items;
    
    public GameState(String playerName, Map<String, Monster> monsters, List<String> items) {
        this.playerName = playerName;
        this.monsters = monsters;
        this.items = items;
    }
    
    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public Map<String, Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(Map<String, Monster> monsters) {
        this.monsters = monsters;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }  
}