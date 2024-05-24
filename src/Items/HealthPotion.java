package Items;

import Game.Player;
import Monsters.Monster;

public class HealthPotion extends Potion {
    // private int healAmount;

    public HealthPotion(String name, int price) {
        super(name, price);
    }

    @Override
    public void useItem(Player player, Monster enemy) {
        if (!player.monsters.isEmpty()) {
            Monster playerMonster = player.monsters.get(0);
            int count=playerMonster.getHp();
            playerMonster.setHp(10+count);
            System.out.println("Player used a Health Potion. HP increased by " + (10+count) + ".");
        }
    }
}
