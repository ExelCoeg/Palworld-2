package Game;
import Monsters.*;
import java.util.*;
public class Player {
    private String name = null;
    public List<Monster> monsters = new ArrayList<>();
    public List<Monster> selectedMonsters = new ArrayList<>();

    public int gold = 50;

    public int getGold() {
        return this.gold;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    public Player(String name){
       this.name = name;
    }
    public Player(String name, List<Monster> monsters){
        this.monsters = new ArrayList<>();
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
       this.name = name;
    }
    public Player(){
        
    }

    public void PrintMonsters(){
        System.out.println();
        for(int i=0;i<monsters.size();i++){
            System.out.println((i+1)+ "." + monsters.get(i).getName());
            System.out.println("Level: " + monsters.get(i).getLevel());
            System.out.println("HP: " + monsters.get(i).getHp());
            System.out.println("EXP: " + monsters.get(i).getExp());
            System.out.println("Element: " + monsters.get(i).getElemen());
            System.out.println();
        }
        System.out.println();
    }
   
   
    
}
