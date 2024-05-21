package Game;

import Monsters.*;
import Monsters.Monster.elements;
import java.util.Random;

public class Dungeon {
   public Monster GenerateRandomMonster(Monster monster){
        Random rand = new Random();
        int level = rand.nextInt(monster.getLevel() + 2) + 1; // level monster random dari 1 hingga (level player + 2
        int hp = 100;
        int exp = 0;
        elements elemen = elements.values()[rand.nextInt(elements.values().length)];
       return switch (elemen) {
            case API -> new MonsterApi("Random Monster Api", level, hp, exp, Monster.elements.API);
            case ANGIN -> new MonsterAngin("Random Monster Angin", level, hp, exp, Monster.elements.ANGIN);
            case ES -> new MonsterEs("Random Monster Es", level, hp, exp, Monster.elements.ES);
            case TANAH -> new MonsterTanah("Random Monster Tanah", level, hp, exp, Monster.elements.TANAH);
            default -> new MonsterAir("Random Monster Air", level, hp, exp, Monster.elements.AIR);
       };  
   }
   public static void main(String[] args) {
     System.out.println("hi");
 }
}
