package Game;

import Items.*;
import Monsters.*;
import java.util.*;

public class Player {
    private String name = null;
    public List<Monster> monsters = new ArrayList<>();
    public List<Potion> potions = new ArrayList<>();

    public List<Monster> getMonsters() {
        return this.monsters;
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
    }

    public List<Potion> getPotions() {
        
        return this.potions;
    }

    public void setPotions(List<Potion> potions) {
        this.potions = potions;
    }

    public List<Monster> selectedMonsters = new ArrayList<>();

    public int gold = 100;

    public int getGold() {
        return this.gold;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    public Player(String name) {
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
       this.name = name;
    }

    public Player(String name, List<Monster> monsters) {
        this.monsters = monsters;
        this.name = name;
    }

    public void addPotion(Potion potion) {
        this.potions.add(potion);
    }
    public void removePotion(Potion potion){
        this.potions.remove(potion);
    }
    public void usePotion(Potion potion, Monster monster){
        if(potion instanceof ElementalPotion elementalPotion){
            elementalPotion.useItem(monster);
        }
        else if (potion instanceof HealthPotion healthPotion){
            healthPotion.useItem(monster);
        }
    }
    
        

    
    

    public void printMonsters() {
        System.out.println();
        for (int i = 0; i < monsters.size(); i++) {
            System.out.println((i + 1) + "." + monsters.get(i).getName());
            System.out.println("Level: " + monsters.get(i).getLevel());
            System.out.println("HP: " + monsters.get(i).getHp());
            System.out.println("EXP: " + monsters.get(i).getExp());
            System.out.println("Element: " + monsters.get(i).getElemen());
            System.out.println();
        }
        System.out.println();
    }
}
