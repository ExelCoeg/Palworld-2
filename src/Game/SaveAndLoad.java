package Game;

import Items.*;
import Monsters.*;
import java.io.*;
import java.util.*;

public class SaveAndLoad {
    private static final int MAX_SAVES = 5;
    private static final String SAVE_PATH = "src/Saves/";

    

    public static void saveGame(GameState gameState, String fileName) throws IOException {
        File dir = new File(SAVE_PATH);
        File[] files = dir.listFiles();
        if (files.length >= MAX_SAVES) {
            System.out.println("Maksimum save files telah tercapai.");
            return;
        }

        String newFileName = fileName + ".txt";
        try (FileOutputStream fileOut = new FileOutputStream(SAVE_PATH + newFileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(gameState);
        }
    }

    public static GameState loadGame(String fileName) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(SAVE_PATH + fileName);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (GameState) in.readObject();
        }
    }

    public static int getNumberOfSaves(String fileName) {
        File dir = new File(SAVE_PATH);
        return (int) dir.listFiles((d, name) -> name.startsWith(fileName)).length;
    }

    public static void overwriteGame(GameState gameState, String fileName) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(SAVE_PATH + fileName);
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
    
}

class GameState implements Serializable {
    private String playerName;
    private List<Monster> monsters;
    private int gold;
    private List<Potion> potions;
    
    public int getGold() {
        return this.gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
    
    public List<Potion> getPotions() {
        return this.potions;
    }

    public void setPotions(List<Potion> potions) {
        this.potions = potions;
    }


    public List<Monster> getMonsters() {
        return this.monsters;
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
    }

    
    public GameState(String playerName,int gold, List<Monster> monsters, List<Potion> potions){//Map<String, Monster> monsters, List<String> items) {
        this.playerName = playerName;
        this.gold = gold;
        this.monsters = monsters;
        this.potions = potions;
    }
    
    public GameState(){
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
}