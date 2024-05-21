package Game;

import Monsters.*;
public class Homebase {
    public Homebase(){

    }
    public void HealMonster(Monster monster){
        monster.Heal();
    }
    public Monster EvolveMonster(Monster monster, String elemen){
        return monster.Evolve(elemen);
    }
    public static void main(String[] args) {
        System.out.println("hi");
    }
}
